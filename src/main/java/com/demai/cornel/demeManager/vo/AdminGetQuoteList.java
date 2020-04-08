package com.demai.cornel.demeManager.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author binz.zhang
 * @Date: 2020-04-08    14:27
 */
@Data public class AdminGetQuoteList {
    private String company;
    private String tower_id;
    private List<orderInfo> tower_info;

    @Data public  class orderInfo {
        private long count;
        private Integer status;
        private String unit_price="元";
        private String unit_weight="吨";
        private BigDecimal weight;
        private BigDecimal total;
    }
}
