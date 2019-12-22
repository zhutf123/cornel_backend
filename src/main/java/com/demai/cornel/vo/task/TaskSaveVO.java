package com.demai.cornel.vo.task;

import com.demai.cornel.dmEnum.IEmus;
import com.demai.cornel.vo.user.UserLoginResp;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author binz.zhang
 * @Date: 2019-12-18    19:06
 */
@Data public class TaskSaveVO {

    /**
     * taskId : 123123
     * carryWeight : 150
     * larryId : 123123
     * selectTime : 2019-11-07 8:00-12:00
     */

    private String taskId;
    private BigDecimal carryWeight;
    private int larryId;
    private String selectTime;
    private String userId;

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

}
