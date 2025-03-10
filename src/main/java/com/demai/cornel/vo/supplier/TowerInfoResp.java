package com.demai.cornel.vo.supplier;

import com.demai.cornel.dmEnum.IEmus;
import com.demai.cornel.model.Commodity;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

/**
 * @Author binz.zhang
 * @Date: 2020-01-07    19:23
 */
@Data
public class TowerInfoResp {

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
     * 主营品种
     */
    private List<Commodity> commoditys;

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
     * 默认标志位 1是默认烘干塔 0是非默认
     */
    private Integer defaultFlag;

    /**
     * 烘干塔联系人ID
     */
    private Set<String> contactUserId;

    private Integer optStatus;

    public static enum REGISTER_STATUS implements IEmus {
        SUCCESS(0, "成功"),
        NO_RESULT(1,"未查到"),
        SERVICE_ERROR(3, "联系人信息错误");


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
