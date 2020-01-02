package com.demai.cornel.vo.quota;

import com.demai.cornel.model.Commodity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author binz.zhang
 * @Date: 2020-01-02    10:59
 */
@Data public class GerQuoteListResp {
    private String quoteId; //报价ID
    private String commodityId; // 商品ID
    private String quote;     //价格
    private String unitPrice; //价格单位
    private String commodityName;  //商品名称
    @JsonIgnore private Map<String, String> commodityProperties;

    private List<Commodity.Properties> properties; //商品属性

    public void setCommodityProperties(Map<String, String> commodityProperties) {
        this.commodityProperties = commodityProperties;
        if (commodityProperties != null) {
            this.properties = new ArrayList<>(commodityProperties.size());
            commodityProperties.keySet().forEach(x -> {
                properties.add(new Commodity.Properties(x, commodityProperties.get(x)));
            });
        }
    }

    public void setProperties(List<Commodity.Properties> properties) {
        this.properties = properties;
        if (properties != null) {
            this.commodityProperties = properties.stream()
                    .collect(Collectors.toMap(Commodity.Properties::getTitle, Commodity.Properties::getValue));
        }
    }
}
