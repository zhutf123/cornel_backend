/**
 * Copyright (c) 2015 Qunar.com. All Rights Reserved.
 */
package com.demai.cornel.dao;

import com.demai.cornel.model.UserAclInfo;

/**
 * Create By zhutf 19-10-6 下午1:08
 */
public interface UserAclInfoDao {
    public void update(UserAclInfo userAclInfo);

    public void save(UserAclInfo userAclInfo);
}
