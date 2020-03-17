package com.demai.cornel.vo.quota;

import com.demai.cornel.model.ImgInfoReq;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author binz.zhang
 * @Date: 2020-03-09    15:06
 */
@Data
public class SystemQuoteV2Req extends SystemQuoteReq {
    private String warehouseTime;//烘干塔货物入库的时间
    private Integer cargoStatus;// 货物状态
    private BigDecimal wetWeight;//湿粮重量
    private BigDecimal loanPrice; //贷款金额
    private List<ImgInfoReq> imgs;
}
