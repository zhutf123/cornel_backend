package com.demai.cornel.model;

import com.demai.cornel.dmEnum.IEmus;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

/**
* @Author binz.zhang
* @Date: 2019-12-30    15:23
*/
@Data
public class DryTower {

    /**
     * 烘干塔ID
     */
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
    * 联系人
    */
    private String contactsName;

    /**
    * 手机号
    */
    private String contactMobile;

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
    private Date createTime;

    /**
    * 状态 1 正在使用中 0 失效
    */
    private Integer status;

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
    * 绑定人
    */
    private String bindUserId;

    /**
    * 联系人身份证号
    */
    private String userIdCard;

    /**
     * 默认标志位 1是默认烘干塔 0是非默认
     */
    private Integer defaultFlag;

    public static enum REGISTER_STATUS implements IEmus {
        SUCCESS(0, "注册成功"),
        PARAM_ERROR(1,"参数错误"),
        SERVICE_ERROR(2, "服务异常");

        private int value;
        private String expr;

        private REGISTER_STATUS(int value, String expr) {
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