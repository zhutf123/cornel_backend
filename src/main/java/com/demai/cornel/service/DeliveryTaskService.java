/**
 * Copyright (c) 2015 Qunar.com. All Rights Reserved.
 */
package com.demai.cornel.service;

import com.demai.cornel.dao.OrderInfoDao;
import com.demai.cornel.model.OrderInfo;
import com.demai.cornel.util.DateFormatUtils;
import com.demai.cornel.util.DateUtils;
import com.demai.cornel.util.StringUtil;
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
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.sql.Timestamp;
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
     * @param deliveryId
     * @param param
     */
    public Collection<SupplierTaskListResp> getTaskOrderListByStatus(String deliveryId, GetOrderListReq param) {
        List<GetOrderListResp> orderListResp = orderInfoDao
                .getOrderInfoByTaskByDelivery(deliveryId, param.getOrderType(), param.getOrderId(), param.getPgSize());
        if (CollectionUtils.isEmpty(orderListResp)) {
            log.info("delivery query order list is empty deliveryId:{} param:{}", deliveryId, JsonUtil.toJson(param));
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
     * @param deliveryId
     * @param param
     */
    public List<GetOrderListResp> getTaskOrderListByStatusV2(String deliveryId, GetOrderListReq param) {
        List<GetOrderListResp> orderListResp = orderInfoDao
                .getOrderInfoByTaskByDelivery(deliveryId, param.getOrderType(), param.getOrderId(), param.getPgSize());
        if (CollectionUtils.isEmpty(orderListResp)) {
            log.info("delivery query order list is empty towerId:{} param:{}", deliveryId, JsonUtil.toJson(param));
            return Lists.newArrayList();
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
        GetOrderListResp result = orderInfoDao
                .getOrderInfoByOrderIdOrVerifyCode("delivery", deliveryId, param.getOrderId(),
                        StringUtil.isNotEmpty(param.getVerifyCode()) ?
                                param.getVerifyCode().toUpperCase() :
                                param.getVerifyCode());
        if (result == null) {
            result = new GetOrderListResp();
        }
        return result;
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
        orderInfo.setReceiverUserId(sendOutId);
        orderInfo.setDeliveryReceiveTime(DateFormatUtils.formatDateTime(new java.sql.Date(System.currentTimeMillis())));
        orderInfo.setStatus(OrderInfo.STATUS_ENUE.ORDER_DELIVERY.getValue());
        orderInfo.setOldStatus(OrderInfo.STATUS_ENUE.ORDER_ARRIVE_ARR.getValue());
        int num = orderInfoDao.updateShipmentStatusByOldStatus(orderInfo);
        if (log.isDebugEnabled()) {
            log.debug("supplier start shipment update order num is zero");
        }
        if (num == 0) {
            return OperationOrderResp.builder().sendOutTime(DateFormatUtils.formatDateTime(new Date()))
                    .success(Boolean.FALSE)
                    .opResult(OperationOrderResp.DELIVERY_RESP_STATUS_ENUE.OPERATION_ERROR.getValue()).orderId(orderId)
                    .orderStatus(orderInfo.getStatus()).build();
        }
        orderInfo = orderInfoDao.getOrderInfoByOrderId(orderId);
        return OperationOrderResp.builder().sendOutTime(DateFormatUtils.formatDateTime(new Date())).success(Boolean.TRUE)
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
        orderInfo.setReceiverUserId(Sets.newHashSet(supplierId));
        orderInfo.setSuccWeight(new BigDecimal(param.getRealWeight()));
        orderInfo.setStatus(OrderInfo.STATUS_ENUE.ORDER_DELIVERY_OVER.getValue());
        orderInfo.setOldStatus(OrderInfo.STATUS_ENUE.ORDER_DRIVER_CONFIRM_OVER.getValue());
        orderInfo.setLetOutTime(DateFormatUtils.formatDateTime(new java.sql.Date(System.currentTimeMillis())));

        int num = orderInfoDao.updateShipmentStatusByOldStatus(orderInfo);
        if (log.isDebugEnabled()) {
            log.debug("supplier shipment over update order num is zero");
        }
        if (num == 0) {
            return OperationOrderResp.builder().sendOutTime(orderInfo.getLetOutTime())
                    .success(Boolean.FALSE).orderId(param.getOrderId()).orderStatus(orderInfo.getStatus())
                    .opResult(OperationOrderResp.DELIVERY_RESP_STATUS_ENUE.OPERATION_ERROR.getValue()).build();
        }
        orderInfo = orderInfoDao.getOrderInfoByOrderId(param.getOrderId());
        return OperationOrderResp.builder().sendOutTime(orderInfo.getLetOutTime()).success(Boolean.TRUE)
                .opResult(OperationOrderResp.DELIVERY_RESP_STATUS_ENUE.SUCCESS.getValue()).orderId(param.getOrderId())
                .orderStatus(orderInfo.getStatus()).realWeight(orderInfo.getSuccWeight().longValue()).build();
    }


    /**
     * 司机确认卸货完成  接货人开始录入接货信息
     * @param userId
     * @param param
     * @return
     */
    public OperationOrderResp driverConfirmDelivery(String userId, OperationOrderReq param) {
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setOrderId(param.getOrderId());
        orderInfo.setUserId(userId);
        orderInfo.setStatus(OrderInfo.STATUS_ENUE.ORDER_DRIVER_CONFIRM_OVER.getValue());
        orderInfo.setOldStatus(OrderInfo.STATUS_ENUE.ORDER_DELIVERY.getValue());
        int num = orderInfoDao.updateShipmentStatusByOldStatus(orderInfo);
        if (log.isDebugEnabled()) {
            log.debug("delivery over update order num is zero");
        }
        if (num == 0) {
            return OperationOrderResp.builder().sendOutTime(DateFormatUtils.formatDateTime(new Date()))
                    .success(Boolean.FALSE)
                    .opResult(OperationOrderResp.SUPPLIER_RESP_STATUS_ENUE.OPERATION_ERROR.getValue())
                    .orderId(param.getOrderId()).orderStatus(orderInfo.getStatus()).build();
        }
        orderInfo = orderInfoDao.getOrderInfoByOrderId(param.getOrderId());
        return OperationOrderResp.builder().sendOutTime(DateFormatUtils.formatDateTime(new Date())).success(Boolean.TRUE)
                .opResult(OperationOrderResp.SUPPLIER_RESP_STATUS_ENUE.SUCCESS.getValue()).orderId(param.getOrderId())
                .realWeight(orderInfo.getCarryWeight().longValue()).orderStatus(orderInfo.getStatus()).build();
    }
}
