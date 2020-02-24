package com.demai.cornel.purcharse.vo.resp;

import com.demai.cornel.dmEnum.IEmus;
import com.demai.cornel.vo.task.OrderTaskLorryInfoRespBase;
import com.demai.cornel.vo.user.UserLoginResp;
import lombok.Data;

import javax.naming.ldap.PagedResultsControl;

/**
 * @Author binz.zhang
 * @Date: 2020-02-24    20:12
 */
@Data
public class BuyerGelLorryListResp extends OrderTaskLorryInfoRespBase {

    private Integer optStatus;

    public static enum CODE_ENUE implements IEmus {
        NO_USER(-1, "用户无权限查看"),
        SUCCESS(0, "请求成功"),
        NETWORK_ERROR(1, "网络异常，请稍后重试"),
        ORDER_INVALID(2, "订单失效"),
        PARAM_ERROR(3, "参数不合法");

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
