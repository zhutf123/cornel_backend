package com.demai.cornel.purcharse.vo;

import com.demai.cornel.model.Commodity;
import com.demai.cornel.vo.quota.GerQuoteListResp;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author binz.zhang
 * @Date: 2020-02-12    14:23
 */
@Data public class GetSystemOfferResp {
    private String offerId; //报价ID
    private String commodityId; // 商品ID
    private BigDecimal price;     //价格
    private String unitPrice; //价格单位
    private String unitWeight="吨"; //价格单位
    private String commodityName;  //商品名称
    private String location;//发货地
    private BigDecimal minBuyWeight;//最少购买量
    private List<Commodity.Properties> properties; //商品属性
    private List<GetSystemOfferResp.Detail> detail; //报价详情
    @JsonIgnore private List<String> notice; //注意事项
    @JsonIgnore private Map<String, String> commodityProperties;


    public void setCommodityProperties(Map<String, String> commodityProperties) {
        this.commodityProperties = commodityProperties;
        if (commodityProperties != null) {
            this.properties = new ArrayList<>(commodityProperties.size());
            commodityProperties.keySet().forEach(x -> {
                properties.add(new Commodity.Properties(x, commodityProperties.get(x)));
            });
        }
    }
    @Data public static class Detail {
        private String title;
        private Object values;

        public Detail(String title, Object values) {
            this.title = title;
            this.values = values;
        }
    }
}
