package com.demai.cornel.purcharse.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

/**
 * @Author binz.zhang
 * @Date: 2020-02-20    00:00
 */
@Data public class CompanyInfo {
    /**
     * 自增ID
     */
    @JsonIgnore private Integer id;

    /**
     * 公司ID
     */
    private String companyId;

    /**
     * 公司名称
     */
    private String companyName;

    /**
     * 创建时间
     */
    @JsonIgnore private Timestamp createTime;

    /**
     * 更新时间
     */
    @JsonIgnore private Timestamp updateTime;

    /**
     * 状态 1 有效 0无效
     */
    @JsonIgnore private Integer status;

    /**
     * 联系人
     */
    @JsonIgnore private String contactUser;

    /**
     * 联系电话
     */
    @JsonIgnore private String contactTel;

    /**
     * 主营项目
     */
    private String mainProject;

    /**
     * 主要需求
     */
    private String mainReq;

    /**
     * 日消耗粮食
     */
    private BigDecimal dailyConsume;

    /**
     * 地址
     */
    private String locationId;

    private String location;

    private String userId;

    private String licenseUrl;
}