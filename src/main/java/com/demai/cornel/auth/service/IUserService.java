package com.demai.cornel.auth.service;

import org.springframework.stereotype.Component;

import java.util.List;

/**
 *
 */
@Component
public interface IUserService {
    /**
     * 根据userID 获取权限列表
     * @param userId
     * @return
     */
     List<String> getUserAuthCode(String userId);

    /**
     * 根据userID 获取用户角色
     * @param userId
     * @return
     */
     String getUserRoleByUserId(String userId);

    /**
     * 根据tel获取userID
     * @param tel
     * @return
     */
     String getUserIdByTel(String tel);

    /**
     * 根据openID 获取userID
     * @param openId
     * @return
     */
     String getUserIdByOpenId(String openId);

}
