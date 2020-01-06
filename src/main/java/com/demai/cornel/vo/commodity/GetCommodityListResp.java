package com.demai.cornel.vo.commodity;

import com.demai.cornel.model.Commodity;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author binz.zhang
 * @Date: 2020-01-06    13:31
 */
@Data public class GetCommodityListResp extends Commodity {
    private BigDecimal quote; //报价
    private String unitPrice; //价格单位
    private String unitWeight;//出货量的单位
    private BigDecimal shipmentWeight;//出货量
}
