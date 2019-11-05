/**
 * Copyright (c) 2015 Qunar.com. All Rights Reserved.
 */
package com.demai.cornel.vo.user;

import java.io.Serializable;

import lombok.Data;

/**
 * Create By zhutf 19-10-31 下午11:06
 */
@Data
public class UserLoginSendMsgParam implements Serializable {

    /** 用户手机号 */
    private String phone;

}
