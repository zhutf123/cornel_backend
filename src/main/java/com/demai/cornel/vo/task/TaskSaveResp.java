package com.demai.cornel.vo.task;

import com.demai.cornel.dmEnum.IEmus;
import com.demai.cornel.util.BigDecimalUtil;
import com.demai.cornel.vo.user.UserLoginResp;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.HashMap;

/**
 * @Author binz.zhang
 * @Date: 2019-12-20    14:48
 */

@Data
@Builder
@NoArgsConstructor
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
    private String weightUnit = "吨";
    private String verCode;
    private HashMap<String, Integer> selectTime;
    private String driverName;

    public static enum CODE_ENUE implements IEmus {
        NO_USER(-1, "用户不存在，请先注册"), SUCCESS(0, "请求成功"), ORDER_FAIL(7, "抢单失败"), NETWORK_ERROR(1,
                "网络异常，请稍后重试"), OPENID_ERROR(2, "重新选择时间"), MSG_CODE_ERROR(3, "车辆状态无效重选车辆"), TASK_CODE_ERROR(4,
                "任务失效重新选择订单"), WEIGHT_CODE_ERROR(5, "超重请重新选择重量"), WEIGHT_TASK_CODE_ERROR(6, "订单剩余重量小于提交重量");

        private int value;
        private String expr;

        private CODE_ENUE(int value, String expr) {
            this.value = value;
            this.expr = expr;
        }

        @Override public int getValue() {
            return value;
        }

        @Override public String getExpr() {
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

    public static enum INNER_CODE_ENUE implements IEmus {
        SUCCESS(0, "抢单成功"), WEIGHT_ERROR(1, "抢单失败-修改抢单重量"), ORDER_ERROR(2, "抢单失败-修改接单时间");

        private int value;
        private String expr;

        private INNER_CODE_ENUE(int value, String expr) {
            this.value = value;
            this.expr = expr;
        }

        @Override public int getValue() {
            return value;
        }

        @Override public String getExpr() {
            return expr;
        }

    }
}
