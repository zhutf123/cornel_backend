package com.demai.purcharse.model;

import java.util.Date;

/**
* @Author binz.zhang
* @Date: 2020-02-11    21:27
*/
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
    private String mobile;

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
    private String frequentlyAddress;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public Short getIdType() {
        return idType;
    }

    public void setIdType(Short idType) {
        this.idType = idType;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getFrequentlyAddress() {
        return frequentlyAddress;
    }

    public void setFrequentlyAddress(String frequentlyAddress) {
        this.frequentlyAddress = frequentlyAddress;
    }
}