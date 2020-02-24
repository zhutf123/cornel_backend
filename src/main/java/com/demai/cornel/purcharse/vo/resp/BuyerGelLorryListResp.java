package com.demai.cornel.purcharse.vo.resp;

import com.demai.cornel.dmEnum.IEmus;
import com.demai.cornel.model.Commodity;
import com.demai.cornel.vo.task.OrderTaskLorryInfoRespBase;
import com.demai.cornel.vo.user.UserLoginResp;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.naming.ldap.PagedResultsControl;
import java.util.Set;

/**
 * @Author binz.zhang
 * @Date: 2020-02-24    20:12
 */
@Data
public class BuyerGelLorryListResp {
    private String deliverOrderId;
    private String driverUserName;
    private String driverUserId;
    private String driverMobile;
    private String receiveLocation;
    private String plateNumber;

    @JsonIgnore
    private Set<String> driverMobileSet;
    private String product;

    private String carryWeight;
    private String lorryId;
    private String esArrTime;//预估到达时间
    private Integer status;
    private String unitWeight;
}
