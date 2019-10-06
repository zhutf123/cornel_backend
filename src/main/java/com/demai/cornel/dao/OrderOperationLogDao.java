/**
 * Copyright (c) 2015 Qunar.com. All Rights Reserved.
 */
package com.demai.cornel.dao;

import com.demai.cornel.model.OrderOperationLog;

/**
 * Create By zhutf 19-10-6 下午1:08
 */
public interface OrderOperationLogDao {
    public void update(OrderOperationLog orderOperationLog);

    public void save(OrderOperationLog orderOperationLog);
}
