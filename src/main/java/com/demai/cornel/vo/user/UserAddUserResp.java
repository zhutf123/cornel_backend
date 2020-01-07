package com.demai.cornel.vo.user;

import com.demai.cornel.dmEnum.IEmus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author binz.zhang
 * @Date: 2020-01-07    13:56
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserAddUserResp {
    private String userId;
    private String userName;
    private String idCard;
    private String idType;
    private String mobile;
    private Integer status;

    public static enum CODE_ENUE implements IEmus {
        NO_USER(-1, "用户不存在，请先注册"),
        SUCCESS(0, "请求成功"),
        NETWORK_ERROR(1, "网络异常，请稍后重试"),
        PARAM_ERROR(2, "网络异常，请稍后重试");

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
