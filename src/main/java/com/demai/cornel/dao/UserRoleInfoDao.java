/**
 * Copyright (c) 2019 dm.com. All Rights Reserved.
 */
package com.demai.cornel.dao;

import com.demai.cornel.model.RoleInfo;
import com.demai.cornel.model.UserRoleInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Create By zhutf 19-10-6 下午1:18
 * @author tfzhu
 */
public interface UserRoleInfoDao {
    /**
     * 更新用户角色权限
     * @param userRoleInfo
     */
    void update(UserRoleInfo userRoleInfo);

    /**
     * 保存用户角色权限
     * @param userRoleInfo
     */
    void save(UserRoleInfo userRoleInfo);

    /**
     * 根据用户id获取用户的角色信息
     * @param userId
     * @return
     */
    List<UserRoleInfo> getRolesByUserId(@Param("userId") String userId);
    
}
