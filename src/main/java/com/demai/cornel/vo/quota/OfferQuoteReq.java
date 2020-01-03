package com.demai.cornel.vo.quota;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * @Author binz.zhang
 * @Date: 2019-12-31    13:46
 */
@Data @Builder public class OfferQuoteReq {
    /**
     * 主营品种ID
     **/
    private String commodityId;
    /**
     * 报价金额
     **/
    private BigDecimal quote;
    /**
     * 金额单位
     **/
    private String unitPrice = "元";
    /**
     * 出货重量
     **/
    private BigDecimal shipmentWeight;
    /**
     * 出货重量单位
     **/
    private String unitWeight = "吨";
    /**
     * 出货开始时间
     **/
    private String startTime;
    /**
     * 出货截止时间
     **/
    private String endTime;
    /**
     * 出货位置
     **/
    private String location;

    /**
     * 烘干塔ID
     */
    private String towerId;

    /**
     * 是否接受议价 1接受议价 0 不接受议价
     */
    private Integer bargainStatus;

    /**
     * 联系电话
     */
    private String mobile;
}
