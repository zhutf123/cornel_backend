package com.demai.cornel.demeManager.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.Set;

/**
 * @Author binz.zhang
 * @Date: 2020-03-30    15:38
 */
@Data
public class AdminGetBuyerResp {
    private String userId;
    private String userName;
    @JsonIgnore
    private Set<String> mobile;
    private String location;
    private String company;
    private String buyerMobile;

    public void setMobile(Set<String> mobile) {
        this.mobile = mobile;
        if(mobile!=null && mobile.size()>0){
            this.buyerMobile = mobile.iterator().next();
        }
    }
}
