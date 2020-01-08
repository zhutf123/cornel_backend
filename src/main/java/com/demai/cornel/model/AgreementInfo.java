package com.demai.cornel.model;

import lombok.Data;

import java.util.Date;
import java.util.Set;

/**
* @Author binz.zhang
* @Date: 2020-01-08    17:14
*/
@Data
public class AgreementInfo {
    /**
    * 自增ID
    */
    private Integer id;

    /**
    * 协议ID
    */
    private String agreementId;

    /**
    * 状态位 0 无效 1有效
    */
    private Integer status;

    /**
    * 创建时间
    */
    private Date createTime;

    /**
    * 更新时间
    */
    private Date updateTime;

    /**
    * 协议名称
    */
    private String agreementName;

    /**
    * 适用那些页面
    */
    private Set<String> adapt;

    /**
    * 协议内容
    */
    private String agreement;

}