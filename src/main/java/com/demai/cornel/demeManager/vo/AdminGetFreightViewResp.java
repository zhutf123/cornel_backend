package com.demai.cornel.demeManager.vo;

import com.hp.gagawa.java.elements.B;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author binz.zhang
 * @Date: 2020-04-03    16:23
 */
@Data
public class AdminGetFreightViewResp {
    private String company;//烘干塔名称
    //private String locationId;//位置ID
    private BigDecimal averPrice;//平均运费
    private BigDecimal minPrice;//最小运费
    private String towerId;// 烘干塔ID
    private String location;
}
