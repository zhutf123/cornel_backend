package com.demai.cornel.demeManager.vo;

import com.demai.cornel.dmEnum.IEmus;
import com.demai.cornel.purcharse.model.FreightInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author binz.zhang
 * @Date: 2020-03-31    18:33
 */

@Builder @AllArgsConstructor @Data public class AdminGetSaleDetail extends AdminGetSaleList {

    private Integer optStatus;
    private List<AdminGetOutStackInfo.OtherInfo> freightAndIncome;

    public static enum STATUS_ENUE implements IEmus {
        PARAM_ERROR(-1, "参数错误"), SUCCESS(0, "请求成功"), ORDER_INVALID(1, "订单无效"), USER_ERROR(2, "用户无权限"), SERVER_ERROR(3,
                "服务器异常");

        private int value;
        private String expr;

        private STATUS_ENUE(int value, String expr) {
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

//    public AdminGetSaleDetail() {
//        this.setStoreId("");
//        this.setFromLocation("");
//        this.setBuyingPrice(new BigDecimal(0));
//        this.setCapitalCost(new BigDecimal(0));
//        this.setFreightId("");
//        this.setTransportType("");
//        this.setFreightPrice(new BigDecimal(0));
//        this.setEsInCome(new BigDecimal(0));
//        this.optStatus = STATUS_ENUE.SUCCESS.getValue();
//        this.setReviewUser("");
//    }
}
