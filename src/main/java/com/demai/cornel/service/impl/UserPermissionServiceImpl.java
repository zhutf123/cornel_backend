/**
 * Copyright (c) 2019 dm.com. All Rights Reserved.
 */
package com.demai.cornel.service.impl;

import com.demai.cornel.model.RoleInfo;
import com.demai.cornel.service.IUserPermissionService;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Create By zhutf 19-10-6 下午5:38
 */
@Service
public class UserPermissionServiceImpl implements IUserPermissionService {
    private final Logger logger = LoggerFactory.getLogger(UserPermissionServiceImpl.class);

    @Override
    public RoleInfo getRoleInfoByMobile(@Param("moble") String mobile) {
        return null;
    }

    @Override
    public RoleInfo getRoleInfoByopendId(@Param("openId") String opendId) {
        return null;
    }
}
