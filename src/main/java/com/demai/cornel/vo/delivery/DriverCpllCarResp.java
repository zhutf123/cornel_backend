package com.demai.cornel.vo.delivery;

import com.demai.cornel.dmEnum.IEmus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author binz.zhang
 * @Date: 2020-01-19    16:11
 */
@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class DriverCpllCarResp {
    private String lorryId;
    private Integer optResult;
    public static enum STATUS implements IEmus {
        SUCCESS(0, "成功"), PARAM_ERROR(1, "参数缺失"),CAR_EXIST(2, "车辆已经存在"), SERVICE_ERROR(3, "服务异常");

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
