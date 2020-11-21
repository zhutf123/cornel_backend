package com.demai.cornel.model;

import com.demai.cornel.dmEnum.IEmus;
import lombok.Data;

import java.sql.Timestamp;

/**
 * @Author binz.zhang
 * @Date: 2020-01-19    15:18
 */
@Data public class CarTypeInfo {
    private Long id;
    private String name;
    private String carDesc;
    private Timestamp createTime;
    private Integer status;

    public static enum STATUS implements IEmus {
        DELETE(0, "已删除"), EXIST(1, "可用");

        private int value;
        private String expr;

        private STATUS(int value, String expr) {
            this.value = value;
            this.expr = expr;
        }

        @Override public int getValue() {
            return 0;
        }

        @Override public String getExpr() {
            return null;
        }
    }
}
