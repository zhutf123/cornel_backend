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
        private Long count;
        private Integer status;
        private String unit_price="元";
        private String unit_weight="吨";
        private BigDecimal weight;
        private BigDecimal total;

        public orderInfo(Long count, Integer status,BigDecimal weight,
                BigDecimal total) {
            this.count = count;
            this.status = status;
            this.unit_price = unit_price;
            this.unit_weight = unit_weight;
            this.weight = weight;
            this.total = total;
        }
    }

}
