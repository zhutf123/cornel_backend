package com.demai.cornel.purcharse.vo.resp;

import com.demai.cornel.model.Commodity;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author binz.zhang
 * @Date: 2020-02-20    17:05
 */
@Data
public class BuyerCommitListResp extends Commodity {
    private BigDecimal quote; //报价
    private String unitPrice; //价格单位
    private String receiveStartTime;//预估收货时间
    private String receiveEndTime;//预估收货结束时间
}
