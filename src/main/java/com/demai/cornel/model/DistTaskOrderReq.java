package com.demai.cornel.model;

import com.demai.cornel.util.DateUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.apache.kafka.common.protocol.types.Field;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Set;

/**
 * @Author binz.zhang
 * @Date: 2019-12-17    15:42
 */
@Data public class DistTaskOrderReq {
    private Long id;
    private String taskId;
    private String title;
    private String dep;
    private String arr;
    private String startTime;
    private String endTime;
    private BigDecimal distance;
    private String unitDistance;
    private BigDecimal unitWeightPrice;
    private BigDecimal weight;
    private String unitPrice;
    private String unitWeight;
    private BigDecimal income;
    private Long taskStatus;
    private String driverName;
    private BigDecimal restWeight;
    private BigDecimal undistWeight;
    private Set<String> supplierMobileSet;
    private Set<String> receiverMobileSet;
    private String supplierMobile;
    private String receiverMobile;

    @JsonIgnore private Long orderStatus;  //司机对这个单子的状态 0 未接单 1 已接单 2 无法接单


    public static enum STATUS_ENUE {

        TASK_INIT(0, "未接单"), ACCEPT(1, "已接单"), TASK_REVIEW_SUCCESS(2, "已闭仓");
        private Integer value;
        private String expr;

        private STATUS_ENUE(Integer value, String expr) {
            this.value = value;
            this.expr = expr;
        }

        public long getValue() {
            return value;
        }

        public String getExpr() {
            return expr;
        }

    }
}
