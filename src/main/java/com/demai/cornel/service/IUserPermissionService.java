/**
 * Copyright (c) 2015 Qunar.com. All Rights Reserved.
 */
package com.demai.cornel.service;

import com.demai.cornel.model.RoleInfo;
import org.apache.ibatis.annotations.Param;

/**
 * Create By zhutf  19-10-6  下午5:37
 */
public interface IUserPermissionService {

    /**
     * 根据电话获取用户角色
     * @param mobile
     * @return
     */
    public RoleInfo getRoleInfoByMobile(@Param("moble")String mobile);


    /**
     * 根据电话获取用户角色
     * @param opendId
     * @return
     */
    public RoleInfo getRoleInfoByopendId(@Param("openId")String opendId);

}
