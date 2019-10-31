/**
 * Copyright (c) 2015 Qunar.com. All Rights Reserved.
 */
package com.demai.cornel.vo.WeChat;

import java.io.Serializable;
import java.util.Map;

import com.demai.cornel.dmEnum.IEmus;
import com.google.common.collect.Maps;
import lombok.Data;

/**
 * Create By zhutf 19-10-6 下午9:19
 */
@Data
public class WechatCode2SessionResp implements Serializable {

    private String openid;
    private String session_key;
    private String unionid;
    /***
     * 值 说明 -1 系统繁忙，此时请开发者稍候再试 0 请求成功 40029 code 无效 45011 频率限制，每个用户每分钟100次
     */
    private Integer errcode;
    private String errmsg;

    public static enum CODE_ENUE implements IEmus {
        FALSE(-1, "系统繁忙，此时请开发者稍候再试"),
        SUCCESS(0, "请求成功"),
        INVALID_CODE(40029, "code 无效"),
        MANY_REQUEST(45011, "频率限制，每个用户每分钟100次");

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
