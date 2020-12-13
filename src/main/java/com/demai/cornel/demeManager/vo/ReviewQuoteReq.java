package com.demai.cornel.demeManager.vo;

import com.demai.cornel.dmEnum.IEmus;
import com.demai.cornel.model.QuoteInfo;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author binz.zhang
 * @Date: 2020-03-08    14:23
 */
@Data public class ReviewQuoteReq {
    private String quoteId;
    private Integer errCode;
    private String errDesc;
    private String warehouseTime;//出库时间
    private BigDecimal shipmentWeight;//出库时间
    private BigDecimal quote; //报价
    private Integer operaType;

    public static enum OPERA_TYPE implements IEmus {
        APPROVEL(1, "通过审批"), REJECT(2, "拒绝审批"), EDIT(2, "修改订单状态");
        private int value;
        private String expr;

        private OPERA_TYPE(int value, String expr) {
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
