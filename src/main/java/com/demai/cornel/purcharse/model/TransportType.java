package com.demai.cornel.purcharse.model;

import com.demai.cornel.dmEnum.IEmus;
import com.demai.cornel.model.ImgInfo;
import lombok.Data;

import java.util.Arrays;

/**
 * @Author binz.zhang
 * @Date: 2020-03-31    16:59
 */
@Data public class TransportType {
    /**
     * 自增ID
     */
    private Integer id;

    /**
     * 运输方式 货运、空运、水运
     */
    private String transportType;

    /**
     * uuid
     */
    private String transportId;

    /**
     * 0 无效 1有效
     */
    private Short status;

    public static enum TRANSPORT_TYPE_ENUM {
        DIRECT("direct", "院内交货"), MOTOR("motor", "汽运"), RAIL("rail", "K车"), WATER("water", "水运"), Open_top_car(
                "top_car", "敞顶车"), OTHER("other", "其他");
        private String type;
        private String expr;

        private TRANSPORT_TYPE_ENUM(String type, String expr) {
            this.type = type;
            this.expr = expr;
        }

        public String getType() {
            return type;
        }

        public String getExpr() {
            return expr;
        }
    }

    public static TRANSPORT_TYPE_ENUM typeOf(String type) {
        return Arrays.stream(TRANSPORT_TYPE_ENUM.values()).filter(X -> X.getType().equalsIgnoreCase(type)).findAny()
                .orElse(null);
    }

    public static TRANSPORT_TYPE_ENUM exparOf(String expr) {
        return Arrays.stream(TRANSPORT_TYPE_ENUM.values()).filter(X -> X.getExpr().equalsIgnoreCase(expr)).findAny()
                .orElse(null);
    }
}