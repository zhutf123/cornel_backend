package com.demai.cornel.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo implements Serializable {
    private static final long serialVersionUID = 5964570121199514889L;
    private Long id;
    private String name;
    private String userId;
    private String[] openId;
    private String gender;
    private String birthday;
    private String headImg;
    private String nickName;
    private Integer idType;
    private String idCard;
    private String termValidity;
    private String mobile;
    private String lorry;
    private Integer orderNums;
    private BigDecimal score;
    private BigDecimal distance;
    private String punish;
    private String urgentNameF;
    private String urgentMobileF;
    private String urgentNameS;
    private String urgentMobileS;
    private Integer status;
    private Map<String, String> extInfo;
    private Date lastLoginTime;
    private Date createTime;
    private Date operateTime;

}