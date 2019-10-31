/**
 * Copyright (c) 2015 Qunar.com. All Rights Reserved.
 */
package com.demai.cornel.vo.user;

import java.io.Serializable;

import com.demai.cornel.dmEnum.IEmus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Create By zhutf 19-10-31 下午11:50
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginResp implements Serializable {
    /** 微信openId */
    private String openId;
    /** 用户id */
    private String userId;
    /** 用户角色信息 */
    private Integer role;
    /** 用户code */
    private Integer code;

    public static enum CODE_ENUE implements IEmus {
        NO_USER(-1, "用户不存在，请先注册"),
        SUCCESS(0, "请求成功"),
        NETWORK_ERROR(1, "网络异常，请稍后重试"),
        OPENID_ERROR(2, "opendId获取失败");

        private int value;
        private String expr;

        private CODE_ENUE(int value, String expr) {
            this.value = value;
            this.expr = expr;
        }

        @Override
        public int getValue() {
            return value;
        }

        @Override
        public String getExpr() {
            return expr;
        }

    }
}
