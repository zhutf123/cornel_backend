package com.demai.cornel.demeManager.vo;

import com.demai.cornel.model.Commodity;
import com.demai.cornel.purcharse.vo.GetSystemOfferResp;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author binz.zhang
 * @Date: 2020-03-30    15:21
 */
@Data
public class AdminGetSysOffResp {
    private String offerId; //报价ID
    private String commodityId; // 商品ID
    private BigDecimal price;     //价格
    private String unitPrice; //价格单位
    private String unitWeight="吨"; //价格单位
    private String commodityName;  //商品名称
}
