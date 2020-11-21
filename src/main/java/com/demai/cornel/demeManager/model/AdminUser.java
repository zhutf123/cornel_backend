package com.demai.cornel.demeManager.model;

import lombok.Data;

import java.util.Set;

/**
* @Author binz.zhang
* @Date: 2020-03-08    12:12
*/
@Data
public class AdminUser {
    /**
    * 自增ID
    */
    private Integer id;

    /**
    * userID
    */
    private String userId;

    /**
    * 名字
    */
    private String userName;

    /**
    * 手机号
    */
    private Set<String> mobile;

    /**
    * 状态0 无效 1有效
    */
    private Integer status;
}