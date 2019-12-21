/**
 * Copyright (c) 2019 dm.com. All Rights Reserved.
 */
package com.demai.cornel.dao;

import com.demai.cornel.model.OrderInfo;
import com.demai.cornel.vo.task.GetOrderListResp;
import org.apache.ibatis.annotations.Param;

import java.util.List;

import java.util.List;

/**
 * Create By zhutf 19-10-6 下午1:20
 */
public interface OrderInfoDao {
    public void update(OrderInfo orderInfo);

    public void save(OrderInfo orderInfo);

    List<GetOrderListResp> getOrderInfoByOrderTypeAndUserId(@Param("userId")String userId, @Param("orderType")Long orderType,
                                                            @Param("orderId")String orderId,@Param("pgSize")Integer pgSize);

    List<GetOrderListResp> getOrderInfoBySupplier(@Param("supplierId") String supplierId,
            @Param("orderType") Long orderType, @Param("orderId") String orderId, @Param("pgSize") Integer pgSize);


    List<GetOrderListResp> getOrderInfoByTaskByDelivery(@Param("supplierId") String supplierId,
            @Param("orderType") Long orderType, @Param("orderId") String orderId, @Param("pgSize") Integer pgSize);

}
