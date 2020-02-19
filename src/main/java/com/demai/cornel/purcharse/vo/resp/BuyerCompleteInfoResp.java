package com.demai.cornel.purcharse.vo.resp;

import com.demai.cornel.dmEnum.IEmus;
import com.demai.cornel.purcharse.model.BuyerInfo;
import com.demai.cornel.purcharse.model.CompanyInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author binz.zhang
 * @Date: 2020-02-20    00:11
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BuyerCompleteInfoResp {
    private BuyerInfo userInfo;
    private CompanyInfo companyInfo;
    private Integer status;
    public static enum STATUS implements IEmus {
        SUCCESS(0, "成功"), PARAM_ERROR(1, "参数缺失"), SERVICE_ERROR(2, "服务异常"),USERR_ERROR(3, "用户不存在");

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
