/**
 * Copyright (c) 2015 Qunar.com. All Rights Reserved.
 */
package com.demai.cornel.vo.supplier;

import com.demai.cornel.dmEnum.IEmus;
import com.demai.cornel.vo.delivery.DeliveryTaskListResp;
import com.demai.cornel.vo.task.TaskInfoRespBase;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Create By tfzhu  2019/12/19  7:36 AM
 */
@Data
public class SupplierTaskListResp extends TaskInfoRespBase implements Serializable {
    
    /***
     *订单信息
     */
    private List<DeliveryTaskListResp.DeliveryTaskOrderInfo> orderInfo;

    public static enum STATUS_ENUE implements IEmus {
        NO_USER(-1, "用户不存在，请先注册"),
        SUCCESS(0, "请求成功"),
        NETWORK_ERROR(1, "网络异常，请稍后重试"),
        OPENID_ERROR(2, "opendId获取失败"),
        MSG_CODE_ERROR(3, "短信验证码错误");

        private int value;
        private String expr;

        private STATUS_ENUE(int value, String expr) {
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
