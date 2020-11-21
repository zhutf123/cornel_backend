package com.demai.cornel.vo.tower;

import com.demai.cornel.dmEnum.IEmus;
import lombok.Builder;
import lombok.Data;

/**
 * @Author binz.zhang
 * @Date: 2020-01-03    15:52
 */
@Data
@Builder
public class TowerOperaResp {


    private Integer status;
    private String towerId;

    public static enum OPERATION_STATUS implements IEmus {
        SUCCESS(0, "操作成功"),
        NO_TOWER(1, "未查到对应烘干塔"),
        PARAM_ERROR(2, "参数错误"),
        AUTH_ERROR(3, "无权操作");

        private int value;
        private String expr;

        private OPERATION_STATUS(int value, String expr) {
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
