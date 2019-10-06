/**
 * Copyright (c) 2015 Qunar.com. All Rights Reserved.
 */
package com.demai.cornel.dao;

import com.demai.cornel.model.OrderInfo;

/**
 * Create By zhutf 19-10-6 下午1:20
 */
public interface OrderInfoDao {
    public void update(OrderInfo orderInfo);

    public void save(OrderInfo orderInfo);
}
