package com.demai.cornel.vo.uploadfile;

import com.demai.cornel.dmEnum.IEmus;
import com.demai.cornel.vo.user.UserLoginResp;
import lombok.Builder;
import lombok.Data;

/**
 * @Author binz.zhang
 * @Date: 2020-01-17    17:41
 */
@Data @Builder public class UploadResp {
    private Integer optResult;
    private String url;
    public static enum CODE_ENUE implements IEmus {
        SUCCESS(0, "请求成功"),
        PARAM_ERROR(1, "参数错误"),
        SERVER_ERROR(2, "服务器异常");

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
