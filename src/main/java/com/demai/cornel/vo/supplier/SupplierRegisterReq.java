package com.demai.cornel.vo.supplier;

import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @Author binz.zhang
 * @Date: 2019-12-30    15:15
 */
@Data public class SupplierRegisterReq {
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
     * 绑定人
     */
    private String bindUserId;

    /**
     * 联系人身份证号
     */
    private String userIdCard;

    private List<TowerInfo> towerInfos;

    @Data public static class TowerInfo {
        /**
         * 主营品种
         */
        private Set<String> commodityId;

        private String towerId;
        /**
         * 总的位置
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

    }
}
