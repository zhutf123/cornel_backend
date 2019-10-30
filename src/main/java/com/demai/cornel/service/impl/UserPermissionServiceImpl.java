/**
 * Copyright (c) 2019 dm.com. All Rights Reserved.
 */
package com.demai.cornel.service.impl;

import com.demai.cornel.model.RoleInfo;
import com.demai.cornel.service.IUserPermissionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Create By zhutf 19-10-6 下午5:38
 */
public class UserPermissionServiceImpl implements IUserPermissionService {
    private final Logger logger = LoggerFactory.getLogger(UserPermissionServiceImpl.class);

    @Override
    public RoleInfo getRoleInfoByMobile(String mobile) {
        return null;
    }

    @Override
    public RoleInfo getRoleInfoByopendId(String opendId) {
        return null;
    }
}
