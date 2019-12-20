package com.demai.cornel.vo.task;

import com.demai.cornel.dmEnum.IEmus;
import com.demai.cornel.util.BigDecimalUtil;
import com.demai.cornel.vo.user.UserLoginResp;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author binz.zhang
 * @Date: 2019-12-20    14:48
 */
@Data
public class TaskSaveResp {

    /**
     * status : 0
     * orderId : 123123123
     * restWeight : 50.0
     * weightUnit : 吨
     */

    private int status;
    private String orderId;
    private BigDecimal restWeight;
    private String weightUnit;
    public static enum CODE_ENUE implements IEmus {
        NO_USER(-1, "用户不存在，请先注册"),
        SUCCESS(0, "抢单成功"),
        WEIGHT_ERROR(1, "请修改抢单重量"),
        ORDER_ERROR(2, "接单失败");

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

        public static UserLoginResp.CODE_ENUE getByValue(Integer value) {
            for (UserLoginResp.CODE_ENUE x : UserLoginResp.CODE_ENUE.values()) {
                if (x.getValue() == value) {
                    return x;
                }
            }
            return null;
        }

    }
}
