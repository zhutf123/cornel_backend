package com.demai.cornel.purcharse.vo.resp;

import com.demai.cornel.dmEnum.IEmus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author binz.zhang
 * @Date: 2020-02-17    12:29
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data public class GetLocationResp {

    private Integer status;
    private List<LocationEntiy> locations;
    @Data public static class LocationEntiy {
        private String locationId;
        private String location;
        private String locationArea;
        private String locationDetail;
        private Integer defaultFlag = 0;
    }
    public static enum STATUS_ENUE implements IEmus {
        PARAM_ERROR(-1, "参数错误"), SUCCESS(0, "请求成功"), SERVER_ERROR(1, "网络异常，请稍后重试"), USER_ERROR(2, "用户无效");

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
}
