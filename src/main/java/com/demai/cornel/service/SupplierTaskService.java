/**
 * Copyright (c) 2015 Qunar.com. All Rights Reserved.
 */
package com.demai.cornel.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.demai.cornel.model.OrderInfo;
import com.demai.cornel.vo.order.OperationOrderReq;
import com.demai.cornel.vo.order.OperationOrderResp;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.demai.cornel.dao.OrderInfoDao;
import com.demai.cornel.vo.delivery.DeliveryTaskListResp;
import com.demai.cornel.vo.order.GetOrderInfoReq;
import com.demai.cornel.vo.supplier.SupplierTaskListResp;
import com.demai.cornel.vo.task.GetOrderListReq;
import com.demai.cornel.vo.task.GetOrderListResp;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import lombok.extern.slf4j.Slf4j;

/**
 * Create By tfzhu 2019/12/19 8:08 AM 烘干塔服务接口
 */
@Service
@Slf4j
public class SupplierTaskService {

    @Resource
    private OrderInfoDao orderInfoDao;

    /**
     * 根据用户烘干塔用户id 订单状态查询任务订单
     * 
     * @param supplierId
     * @param param
     */
    public Collection<SupplierTaskListResp> getTaskOrderListByStatus(String supplierId, GetOrderListReq param) {
        List<GetOrderListResp> orderListResp = orderInfoDao.getOrderInfoBySupplier(supplierId, param.getOrderTyp(),
                param.getOrderId(), param.getPgSize());
        if (CollectionUtils.isEmpty(orderListResp)) {
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
     * @param supplierId
     * @param param
     */
    public GetOrderListResp getTaskOrderInfoByOrderIdOrVerifyCode(String supplierId, GetOrderInfoReq param) {
        return orderInfoDao.getOrderInfoByOrderIdOrVerifyCode("supplier", supplierId,
                param.getOrderId(), param.getVerifyCode());
    }

    /**
     * 烘干塔开始装货
     * @param supplierId
     * @param orderId
     * @return
     */
    public OperationOrderResp shipmentStart(String supplierId, String orderId) {
        OrderInfo orderInfo = new OrderInfo();
//        orderInfoDao.

        return null;
    }


    /**
     * 烘干塔装货完成
     * @param supplierId
     * @param param
     * @return
     */
    public OperationOrderResp shipmentOver(String supplierId, OperationOrderReq param) {
        OrderInfo orderInfo = new OrderInfo();
        //        orderInfoDao.

        return null;
    }

}
