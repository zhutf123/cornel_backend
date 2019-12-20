package com.demai.cornel.model;

import lombok.Data;
import org.apache.kafka.common.protocol.types.Field;

import java.math.BigDecimal;
import java.sql.Date;

/**
 * @Author binz.zhang
 * @Date: 2019-12-17    15:42
 */
@Data
public class DistTaskOrderReq {
    private Long id;
    private String taskId;
    private String title;
    private String dep;
    private String arr;
    private Date startTime;
    private Date endTime;
    private BigDecimal distance;
    private String unitDistance;
    private BigDecimal price;
    private BigDecimal weight;
    private String unitPrice;
    private String unitWeight;
    private BigDecimal income;
    private Long taskStatus;
    private Long orderStatus;  //司机对这个单子的状态 0 未接单 1 已接单 2 无法接单


}
