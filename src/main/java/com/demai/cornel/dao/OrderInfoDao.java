/**
 * Copyright (c) 2019 dm.com. All Rights Reserved.
 */
package com.demai.cornel.dao;

import java.util.List;

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

    int save(OrderInfo orderInfo);

    int updateShipmentStatusByOldStatus(OrderInfo orderInfo);

    List<GetOrderListResp> getOrderInfoByOrderTypeAndUserId(@Param("userId")String userId, @Param("orderType")Long orderType,
                                                            @Param("orderId")String orderId,@Param("pgSize")Integer pgSize);

    List<GetOrderListResp> getOrderInfoBySupplier(@Param("supplierId") String supplierId, @Param("orderType")Long orderType,
            @Param("orderId") String orderId, @Param("pgSize") Integer pgSize);

    List<GetOrderListResp> getOrderInfoByTaskByDelivery(@Param("supplierId") String supplierId,
            @Param("orderType") Long orderType, @Param("orderId") String orderId, @Param("pgSize") Integer pgSize);

    GetOrderListResp getOrderInfoByOrderIdOrVerifyCode(@Param("role") String role, @Param("supplierId") String supplierId,
            @Param("orderId") String orderId, @Param("verifyCode") String verifyCode);

    List<GetOrderListResp> getOrderInfoByOrderTypeAndUserId(@Param("userId") String userId, @Param("orderType") Integer orderType,
                                                            @Param("orderId") String orderId, @Param("pgSize") Integer pgSize);

    GetOrderInfoResp getOrderInfoByUserAndOrderId(@Param("userId") String userId, @Param("orderId") String orderId);


    int updateOrderStatus(@Param("orderId") String orderId, @Param("status") long status, @Param("userId") String userId);

    ArriveDepDriverResp getOrderStatusAndVerCodeByOrderId(@Param("orderId") String orderId);
}
