package com.demai.cornel.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.Map;
import java.util.Set;

import com.demai.cornel.dmEnum.IEmus;
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
    private Set<String> openId;
    private Integer role;
    private String gender;
    private String birthday;
    private String headImg;
    private String nickName;
    private Integer idType;
    private String idCard;
    private String termValidity;
    private Set<String> mobile;
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

    public Set<String> getMobile() {
        return mobile;
    }

    /***
     * 出去权限 用户的角色
     */
    public static enum ROLE_ENUE implements IEmus {
        DRIVER(1, "司机"),
        SUPPLIER(2, "烘干塔"),
        DELIVERY(3, "港口"),
        ADMIN(4, "系统管理员"),
        OPERATOR(5, "操作员");

        private int value;
        private String expr;

        private ROLE_ENUE(int value, String expr) {
            this.value = value;
            this.expr = expr;
        }

        @Override
        public int getValue() {
            return value;
        }

        @Override
        public String getExpr() {
            return expr;
        }

    }
}