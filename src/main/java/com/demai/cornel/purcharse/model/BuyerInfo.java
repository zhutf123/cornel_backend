package com.demai.cornel.purcharse.model;

import lombok.Data;

import java.util.Date;
import java.util.Set;

/**
 * @Author binz.zhang
 * @Date: 2020-02-12    17:18
 */
@Data
public class BuyerInfo {
    /**
     * 自增ID
     */
    private Integer id;

    /**
     * userid
     */
    private String userId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 身份证号
     */
    private String idCard;

    /**
     * 证件类型 0 身份证
     */
    private Short idType;

    /**
     * 手机号
     */
    private Set<String> mobile;

    /**
     * 公司ID
     */
    private String companyId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 常用地址
     */
    private Set<String> frequentlyLocation;

    /**
     * 0 无效 1有效
     */
    private Integer status;


}