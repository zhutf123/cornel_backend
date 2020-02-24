/**
 * Copyright (c) 2019 dm.com. All Rights Reserved.
 */
package com.demai.cornel.dao;

import java.sql.Date;
import java.util.List;

import com.demai.cornel.purcharse.vo.resp.BuyerGelLorryListResp;
import org.apache.ibatis.annotations.Param;

import com.demai.cornel.model.OrderInfo;
import com.demai.cornel.vo.order.GetOrderInfoResp;
import com.demai.cornel.vo.task.ArriveDepDriverResp;
import com.demai.cornel.vo.task.GetOrderListResp;

/**
 * Create By zhutf 19-10-6 下午1:20
 */
public interface OrderInfoDao {
    int update(OrderInfo orderInfo);

    /**
     * 物理删除订单信息 慎用
     *
     * @param orderId
     */
    void deleteOrder(@Param("orderId") String orderId);

    int save(OrderInfo orderInfo);

    int updateShipmentStatusByOldStatus(OrderInfo orderInfo);

    List<GetOrderListResp> getOrderInfoByOrderTypeAndUserId(@Param("userId") String userId,
            @Param("orderType") Long orderType, @Param("orderId") String orderId, @Param("pgSize") Integer pgSize);

    List<GetOrderListResp> getOrderInfoBySupplier(@Param("supplierId") String supplierId,
            @Param("orderType") Long orderType, @Param("orderId") String orderId, @Param("pgSize") Integer pgSize);

    List<GetOrderListResp> getOrderInfoByTaskByDelivery(@Param("deliverId") String supplierId,
            @Param("orderType") Long orderType, @Param("orderId") String orderId, @Param("pgSize") Integer pgSize);

    GetOrderListResp getOrderInfoByOrderIdOrVerifyCode(@Param("role") String role,
            @Param("towerId") String supplierId, @Param("orderId") String orderId,
            @Param("verifyCode") String verifyCode);


    GetOrderListResp getOrderInfoByUserAndOrderId(@Param("userId") String userId, @Param("orderId") String orderId);

    int updateOrderStatus(@Param("orderId") String orderId, @Param("status") long status,
            @Param("userId") String userId,@Param("oldStatus")long oldStatus);

    ArriveDepDriverResp getOrderStatusAndVerCodeByOrderId(@Param("orderId") String orderId);

    OrderInfo getOrderInfoByOrderId(@Param("orderId") String orderId);

    String getDriverUserId(@Param("orderId") String orderId);

    int updateStatusAndSendOutTime(@Param("orderId") String orderId, @Param("sendOutTime") Date sendOutTime,
            @Param("status") long status,@Param("oldStatus")long oldStatus);

    List<String> getRuningOrderIdInnerTask(@Param("taskId") String taskId, @Param("userId") String userId);


    String getSendOutCode(@Param("orderID")String orderId);

    String getReceiveCode(@Param("orderID")String orderId);


    List<OrderInfo> getOrderInfosByOrderIds(@Param("orderIds") List<String> orderId);


    List<BuyerGelLorryListResp> buyerGetLorryList(@Param("deliverIds") List<String> supplierId);


}