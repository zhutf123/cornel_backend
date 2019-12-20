/**
 * Copyright (c) 2019 dm.com. All Rights Reserved.
 */
package com.demai.cornel.dao;

import com.demai.cornel.model.OrderInfo;
import com.demai.cornel.vo.order.GetOrderInfoResp;
import com.demai.cornel.vo.task.ArriveDepDriverResp;
import com.demai.cornel.vo.task.GetOrderListResp;
import org.apache.ibatis.annotations.Param;

import java.util.List;

import java.util.List;

/**
 * Create By zhutf 19-10-6 下午1:20
 */
public interface OrderInfoDao {
    public void update(OrderInfo orderInfo);

    public int save(OrderInfo orderInfo);

    //public List<OrderInfo> getOrderInfoByTaskUser(OrderInfo orderInfo);

    List<GetOrderListResp> getOrderInfoByOrderTypeAndUserId(@Param("userId") String userId, @Param("orderType") Integer orderType,
                                                            @Param("orderId") String orderId, @Param("pgSize") Integer pgSize);

    GetOrderInfoResp getOrderInfoByUserAndOrderId(@Param("userId") String userId, @Param("orderId") String orderId);


    int updateOrderStatus(@Param("orderId") String orderId, @Param("status") long status, @Param("userId") String userId);


    ArriveDepDriverResp getOrderStatusAndVerCodeByOrderId(@Param("orderId") String orderId);
}
