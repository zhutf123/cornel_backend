package com.demai.cornel.model;

import com.demai.cornel.dmEnum.IEmus;
import lombok.Data;

import java.util.Arrays;

/**
 * @Author binz.zhang
 * @Date: 2020-03-23    16:36
 */
@Data public class ReviewModel {
    private Integer errCode;// 错误码
    private String desc;

    /***
     * 出去权限 用户的角色
     */
    public static enum TOWER_SUP_ORDER_ERR implements IEmus {
        TIME_ERR(1 << 0, "入库时间"),
        WEIGHT_ERR(1 << 1, "货物重量"),
        PRICE_ERR(1 << 2, "价格有误"),
        OTHER_ERR(1 << 4, "其他");

        private int value;
        private String expr;

        private TOWER_SUP_ORDER_ERR(int value, String expr) {
            this.value = value;
            this.expr = expr;
        }

        @Override public int getValue() {
            return value;
        }

        @Override public String getExpr() {
            return expr;
        }

        public static TOWER_SUP_ORDER_ERR keyOf(int key) {
            return Arrays.stream(TOWER_SUP_ORDER_ERR.values()).filter(X -> X.getValue() == key).findAny().orElse(null);
        }

        public static TOWER_SUP_ORDER_ERR exparOf(String value) {
            return Arrays.stream(TOWER_SUP_ORDER_ERR.values()).filter(X -> X.getExpr().equalsIgnoreCase(value))
                    .findAny().orElse(null);
        }
    }


    public static enum SALE_ORDER_ERR implements IEmus {
        STORE_ERR(1 << 0, "库存不足"),
        DE_TIME_ERR(1 << 1, "运输时间"),
        OTHER_ERR(1 << 2, "其他原因");

        private int value;
        private String expr;

        private SALE_ORDER_ERR(int value, String expr) {
            this.value = value;
            this.expr = expr;
        }

        @Override public int getValue() {
            return value;
        }

        @Override public String getExpr() {
            return expr;
        }

        public static SALE_ORDER_ERR keyOf(int key) {
            return Arrays.stream(SALE_ORDER_ERR.values()).filter(X -> X.getValue() == key).findAny().orElse(null);
        }

        public static SALE_ORDER_ERR exparOf(String value) {
            return Arrays.stream(SALE_ORDER_ERR.values()).filter(X -> X.getExpr().equalsIgnoreCase(value))
                    .findAny().orElse(null);
        }
    }
}
