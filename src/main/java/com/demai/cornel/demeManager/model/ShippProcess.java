package com.demai.cornel.demeManager.model;

import com.demai.cornel.dmEnum.IEmus;
import com.demai.cornel.model.ReviewModel;
import lombok.Data;

import java.util.Arrays;

/**
 * @Author binz.zhang
 * @Date: 2020-04-15    17:45
 */
@Data public class ShippProcess {
    private String key;
    private Integer value;

    public ShippProcess(String key, Integer value) {
        this.key = key;
        this.value = value;
    }

    public enum TYPE implements IEmus {
        PAY_DELIVE(1, "先付款后交货"), DELIVE_PAY(2, "先交货后付款");
        private int value;
        private String expr;

        private TYPE(int value, String expr) {
            this.value = value;
            this.expr = expr;
        }

        @Override public int getValue() {
            return value;
        }

        @Override public String getExpr() {
            return expr;
        }

        public static TYPE keyOf(int key) {
            return Arrays.stream(TYPE.values()).filter(X -> X.getValue() == key).findAny()
                    .orElse(null);
        }

        public static TYPE exparOf(String value) {
            return Arrays.stream(TYPE.values())
                    .filter(X -> X.getExpr().equalsIgnoreCase(value)).findAny().orElse(null);
        }
    }
}
