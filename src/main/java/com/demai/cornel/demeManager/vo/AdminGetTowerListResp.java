package com.demai.cornel.demeManager.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Set;

/**
 * @Author binz.zhang
 * @Date: 2020-03-08    19:46
 */
@Data
public class AdminGetTowerListResp {

    private String towerId;

    /**
     * 自增ID
     */
    private Integer id;

    /**
     * 公司名称
     */
    private String company;



    /**
     * 位置
     */
    private String location;
    /**
     * 位置 所在区域
     */

    private String bindUserId;

    private String userName;

    private String mobile;

}
