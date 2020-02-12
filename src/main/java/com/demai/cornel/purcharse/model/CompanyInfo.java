package com.demai.cornel.purcharse.model;

import lombok.Data;

import java.sql.Timestamp;
import java.util.Date;

/**
* @Author binz.zhang
* @Date: 2020-02-12    13:16
*/
@Data
public class CompanyInfo {
    /**
    * 自增ID
    */
    private Integer id;

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
    private Timestamp createTime;

    /**
    * 更新时间
    */
    private Timestamp updateTime;

    /**
    * 状态 1 有效 0无效
    */
    private Integer status;

    /**
    * 联系人
    */
    private String contactUser;

    /**
    * 联系电话
    */
    private String contactTel;

}