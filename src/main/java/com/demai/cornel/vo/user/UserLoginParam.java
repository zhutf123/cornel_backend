/**
 * Copyright (c) 2015 Qunar.com. All Rights Reserved.
 */
package com.demai.cornel.vo.user;

import lombok.Data;

import java.io.Serializable;

/**
 * Create By zhutf 19-10-31 下午11:06
 */
@Data
public class UserLoginParam implements Serializable {

    /** 用户手机号 */
    private String phone;
    /** 微信登录code */
    private String jscode;

}
