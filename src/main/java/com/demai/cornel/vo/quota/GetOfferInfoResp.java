package com.demai.cornel.vo.quota;

import com.demai.cornel.model.ImgInfoReq;
import com.demai.cornel.model.LoanInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

/**
 * @Author binz.zhang
 * @Date: 2020-01-06    15:45
 */
@Data public class GetOfferInfoResp extends GetOfferListResp {
    private String userName;
    private String mobile;
    private String location;
    private Integer cargoStatus;
    @JsonIgnore
    private Set<String> loanId;
    private BigDecimal wetWeight;
    private BigDecimal loanPrice;
    private List<ImgInfoReq> imgInfo;
    private List<LoanInfoSimple> loanInfo;
}
