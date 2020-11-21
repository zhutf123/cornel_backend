package com.demai.cornel.vo.supplier;

import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Set;

/**
 * @Author binz.zhang
 * @Date: 2020-01-07    18:23
 */
@Data
public class AddDryTowerReq {

    /**
     * 自增ID
     */
    private Integer id;

    /**
     * 公司名称
     */
    private String company;

    /**
     * 主营品种
     */
    private Set<String> commodityId;

    /**
     * 位置
     */
    private String location;
    /**
     * 位置 所在区域
     */
    private String locationArea;
    /**
     * 详细位置
     */
    private String locationDetail;

    /**
     * 占地大小
     */
    private BigDecimal area;

    /**
     * 出货量
     */
    private BigDecimal shipmentWeight;

    /**
     * 创建时间
     */
    private Timestamp createTime;

    /**
     * 库存能力
     */
    private BigDecimal capacityStore;

    /**
     * 同时装载车次
     */
    private Integer loadLorryNum;

    /**
     * 装载一车耗时
     */
    private BigDecimal loadLorryCost;

    /**
     * 联系人
     */
    private String contactsName;

    /**
     * 联系电话
     */
    private String contactMobile;

    /**
     * 默认标志位 1是默认烘干塔 0是非默认
     */
    private Integer defaultFlag;

    private String userIdCard;





}
