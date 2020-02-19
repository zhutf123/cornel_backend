package com.demai.cornel.purcharse.vo.resp;

import com.demai.cornel.dmEnum.IEmus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author binz.zhang
 * @Date: 2020-02-19    23:56
 */
@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class BuyerCplCompanyResp {

        private String companyId;
        private Integer optResult;

        public static enum STATUS implements IEmus {
            SUCCESS(0, "成功"), PARAM_ERROR(1, "参数缺失"), SERVICE_ERROR(2, "服务异常"),PHONE_ERROR(3, "手机号格式错误");

            private int value;
            private String expr;

            private STATUS(int value, String expr) {
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
