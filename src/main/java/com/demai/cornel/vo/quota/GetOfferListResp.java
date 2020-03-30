package com.demai.cornel.vo.quota;

import com.demai.cornel.config.ServiceMobileConfig;
import com.demai.cornel.model.Commodity;
import com.demai.cornel.model.QuoteInfo;
import com.demai.cornel.model.ReviewOptResp;
import com.demai.cornel.util.TimeStampUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.postgresql.jdbc.TimestampUtils;
import org.springframework.beans.factory.annotation.Value;

import javax.xml.ws.Service;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author binz.zhang
 * @Date: 2020-01-06    14:32
 */
@Data public class GetOfferListResp {

    /**
     *
     */

    private String commodityId;
    /**
     * 商品名称
     */
    private String commodityName;
    /**
     * 商品属性
     */
    private List<Commodity.Properties> properties;

    @JsonIgnore private Map<String, String> commodityProperties;

    /**
     * 报价ID
     */
    private String quoteId;

    /**
     * 报价金额
     */
    private BigDecimal quote;

    /**
     * 金额单位
     */
    private String unitPrice;

    /**
     * 出货量
     */
    private BigDecimal shipmentWeight;

    /**
     * 出货重量单位
     */
    private String unitWeight;

    /**
     * 出仓时间
     */
    private String startTime;

    /**
     * 更新时间
     */
    private String endTime;

    /**
     * 报价的状态 1 有效 0 无效
     */
    private Integer status;

    private String serviceMobile;
    private Timestamp warehouseTime;
    private String showWarehouseTime;
    private Integer cargoStatus;
    private Integer showLoan=0;//1 是显示放贷的情况 0 是不显示
    @JsonIgnore private Set<String>loanId;
    private List<LoanInfoSimple> loanInfo;
    private ReviewOptResp reviewInfo;
    @JsonIgnore
    private HashMap<String,String> reviewOpt;
    public void setWarehouseTime(Timestamp warehouseTime) {
        this.warehouseTime = warehouseTime;
        this.showWarehouseTime = TimeStampUtil.timeStampConvertString("yyyy-MM-dd",warehouseTime);
    }

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
