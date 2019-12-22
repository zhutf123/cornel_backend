/**
 * Copyright (c) 2015 Qunar.com. All Rights Reserved.
 */
package com.demai.cornel.service;

import com.demai.cornel.dao.OrderInfoDao;
import com.demai.cornel.model.OrderInfo;
import com.demai.cornel.util.DateFormatUtils;
import com.demai.cornel.util.DateUtils;
import com.demai.cornel.util.json.JsonUtil;
import com.demai.cornel.vo.delivery.DeliveryTaskListResp;
import com.demai.cornel.vo.order.GetOrderInfoReq;
import com.demai.cornel.vo.order.OperationOrderReq;
import com.demai.cornel.vo.order.OperationOrderResp;
import com.demai.cornel.vo.supplier.SupplierTaskListResp;
import com.demai.cornel.vo.task.GetOrderListReq;
import com.demai.cornel.vo.task.GetOrderListResp;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * Create By tfzhu  2019/12/19  8:07 AM
 * 接货人服务接口
 */
@Service @Slf4j public class DeliveryTaskService {

    @Resource private OrderInfoDao orderInfoDao;

    /**
     * 根据用户烘干塔用户id 订单状态查询任务订单
     *
     * @param supplierId
     * @param param
     */
    public Collection<SupplierTaskListResp> getTaskOrderListByStatus(String supplierId, GetOrderListReq param) {
        List<GetOrderListResp> orderListResp = orderInfoDao
                .getOrderInfoByTaskByDelivery(supplierId, param.getOrderTyp(), param.getOrderId(), param.getPgSize());
        if (CollectionUtils.isEmpty(orderListResp)) {
            log.info("delivery query order list is empty supplierId:{} param:{}", supplierId, JsonUtil.toJson(param));
            return null;
        }
        Map<String, SupplierTaskListResp> taskOrderInfo = Maps.newHashMap();
        orderListResp.stream().forEach(order -> {
            if (taskOrderInfo.keySet().contains(order.getTaskId())) {
                buildTaskOrderInfo(taskOrderInfo.get(order.getTaskId()), order);
            } else {
                taskOrderInfo.put(order.getTaskId(), buildTaskOrderInfo(null, order));
            }
        });
        return taskOrderInfo.values();
    }

    /**
     * 根据用户烘干塔用户id 订单状态查询任务订单
     *
     * @param supplierId
     * @param param
     */
    public List<GetOrderListResp> getTaskOrderListByStatusV2(String supplierId, GetOrderListReq param) {
        List<GetOrderListResp> orderListResp = orderInfoDao
                .getOrderInfoByTaskByDelivery(supplierId, param.getOrderTyp(), param.getOrderId(), param.getPgSize());
        if (CollectionUtils.isEmpty(orderListResp)) {
            log.info("delivery query order list is empty supplierId:{} param:{}", supplierId, JsonUtil.toJson(param));
            return null;
        }

        return orderListResp;
    }

    /***
     * 根据查询的订单构建烘干塔返回的数据
     *
     * @param order 数据库返回的订单信息
     * @return
     */
    private SupplierTaskListResp buildTaskOrderInfo(SupplierTaskListResp resp, GetOrderListResp order) {
        DeliveryTaskListResp.DeliveryTaskOrderInfo orderInfo = new DeliveryTaskListResp.DeliveryTaskOrderInfo();
        BeanUtils.copyProperties(order, orderInfo);
        if (resp != null) {
            resp.getOrderInfo().add(orderInfo);
        } else {
            resp = new SupplierTaskListResp();
            BeanUtils.copyProperties(order, resp);
            resp.setOrderInfo(Lists.newArrayList(orderInfo));
        }
        return resp;
    }

    /**
     * 根据用户烘干塔用户id 订单状态查询任务订单
     *
     * @param deliveryId
     * @param param
     */
    public GetOrderListResp getTaskOrderInfoByOrderIdOrVerifyCode(String deliveryId, GetOrderInfoReq param) {
        return orderInfoDao
                .getOrderInfoByOrderIdOrVerifyCode("delivery", deliveryId, param.getOrderId(), param.getVerifyCode());
    }

    /**
     * 接货人开始装货
     *
     * @param supplierId
     * @param orderId
     * @return
     */
    public OperationOrderResp deliveryStart(String supplierId, String orderId) {
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setOrderId(orderId);
        Set<String> sendOutId = new HashSet<>();
        sendOutId.add(supplierId);
        orderInfo.setSendOutUserId(sendOutId);
        orderInfo.setReceiveTime(DateFormatUtils.formatDateTime(new Date()));
        orderInfo.setStatus(OrderInfo.STATUS_ENUE.ORDER_DELIVERY.getValue());
        orderInfo.setOldStatus(OrderInfo.STATUS_ENUE.ORDER_ARRIVE_ARR.getValue());
        int num = orderInfoDao.updateShipmentStatusByOldStatus(orderInfo);
        if (log.isDebugEnabled()) {
            log.debug("supplier start shipment update order num is zero");
        }
        if (num == 0) {
            return OperationOrderResp.builder().opOverTime(DateFormatUtils.formatDateTime(new Date()))
                    .success(Boolean.FALSE)
                    .opResult(OperationOrderResp.DELIVERY_RESP_STATUS_ENUE.OPERATION_ERROR.getValue()).orderId(orderId)
                    .orderStatus(orderInfo.getStatus()).build();
        }
        return OperationOrderResp.builder().opOverTime(DateFormatUtils.formatDateTime(new Date())).success(Boolean.TRUE)
                .orderId(orderId).opResult(OperationOrderResp.DELIVERY_RESP_STATUS_ENUE.SUCCESS.getValue())
                .orderStatus(orderInfo.getStatus()).build();
    }

    /**
     * 接货人装货完成
     *
     * @param supplierId
     * @param param
     * @return
     */
    public OperationOrderResp deliveryOver(String supplierId, OperationOrderReq param) {
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setOrderId(param.getOrderId());
        Set<String> sendOutId = new HashSet<>(1);
        sendOutId.add(supplierId);
        orderInfo.setSendOutUserId(sendOutId);
        orderInfo.setSuccWeight(new BigDecimal(param.getRealWeight()));
        orderInfo.setStatus(OrderInfo.STATUS_ENUE.ORDER_DELIVERY_OVER.getValue());
        orderInfo.setOldStatus(OrderInfo.STATUS_ENUE.ORDER_DRIVER_CONFIRM_OVER.getValue());
        int num = orderInfoDao.updateShipmentStatusByOldStatus(orderInfo);
        if (log.isDebugEnabled()) {
            log.debug("supplier shipment over update order num is zero");
        }
        if (num == 0) {
            return OperationOrderResp.builder().opOverTime(DateFormatUtils.formatDateTime(new Date()))
                    .success(Boolean.FALSE).orderId(param.getOrderId()).orderStatus(orderInfo.getStatus())
                    .opResult(OperationOrderResp.DELIVERY_RESP_STATUS_ENUE.OPERATION_ERROR.getValue()).build();
        }
        return OperationOrderResp.builder().opOverTime(DateFormatUtils.formatDateTime(new Date())).success(Boolean.TRUE)
                .opResult(OperationOrderResp.DELIVERY_RESP_STATUS_ENUE.SUCCESS.getValue()).orderId(param.getOrderId())
                .orderStatus(orderInfo.getStatus()).realWeight(orderInfo.getSuccWeight().longValue()).build();
    }
}
