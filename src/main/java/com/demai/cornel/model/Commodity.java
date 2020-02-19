package com.demai.cornel.model;

import com.demai.cornel.dmEnum.IEmus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author binz.zhang
 * @Date: 2019-12-27    16:21
 * 商品类
 */
@Data public class Commodity {
    @JsonIgnore
    private Long id;
    /**
     * 商品名称
     **/
    private String name;
    /**
     * 商品ID
     **/
    private String commodityId;
    /**
     * 商品的状态 1 有效 0 无效
     **/
    private Integer status;
    /**
     * 商品标志位1 是表明是系统商品 0表示是用户自定义商品
     **/
    @JsonIgnore
    private Integer systemFlag;
    /**
     * 商品绑定用户 当该商品是用户自定义的商品是该字段表明了商品是那个用户创建的
     **/
    @JsonIgnore
    private String bindUserId;
    @JsonIgnore private Map<String, String> commodityProperties;
    private List<Properties> properties;

    @Data public static class Properties {
        private String title;
        private String value;

        public Properties(String title, String properties) {
            this.title = title;
            this.value = properties;
        }

        @Override public String toString() {
            return title + " " + value;
        }
    }

    public void setCommodityProperties(Map<String, String> commodityProperties) {
        this.commodityProperties = commodityProperties;
        if (commodityProperties != null) {
            this.properties = new ArrayList<>(commodityProperties.size());
            commodityProperties.keySet().forEach(x -> {
                properties.add(new Properties(x, commodityProperties.get(x)));
            });
        }
    }

    public void setProperties(List<Commodity.Properties> properties) {
        this.properties = properties;
        if (properties != null) {
            this.commodityProperties = properties.stream()
                    .collect(Collectors.toMap(Properties::getTitle, Properties::getValue));
        }
    }

    public static enum COMMODITY_SYSTEM_STATUS implements IEmus {
        SYSTEM(1, "系统商品"), CUSTOM(0, "用户自定义商品");

        private Integer value;
        private String expr;

        private COMMODITY_SYSTEM_STATUS(Integer value, String expr) {
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
