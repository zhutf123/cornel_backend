package com.demai.cornel.vo.quota;

import com.demai.cornel.dmEnum.IEmus;
import com.demai.cornel.model.Commodity;
import com.demai.cornel.model.DryTower;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author binz.zhang
 * @Date: 2020-01-02    14:34
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClickSystemQuoteResp {
    private String userName;  //联系人名字
    private String mobile;    //手机号
    private Commodity commodity; //货品
    private BigDecimal shipmentWeight;  //出货量
    private BigDecimal quote;
    private String unitWeight; //出货量单位
    private String unitPrice; //价格单位
    private Integer status;
    private Integer cargoStatus = 1;// 货物状态 1现货 2等待货物入库
    private List<DryTowerInfo> dryTowerInfo;

    @Data public static class DryTowerInfo {
        private String towerId;
        private String location;
        private String locationArea;
        private String locationDetail;

        public DryTowerInfo(String towerId, String location,String locationArea,String locationDetail) {
            this.towerId = towerId;
            this.location = location;
            this.locationArea = locationArea;
            this.locationDetail = locationDetail;
        }

        public DryTowerInfo() {
        }
    }

    public static enum STATUS_ENUE implements IEmus {
        PARAM_ERROR(-1, "参数错误"), SUCCESS(0, "请求成功"), USER_ERROR(1, "用户角色不是烘干塔"), DRY_TOWER_ERROR(2,
                "未查询到有效的烘干塔信息"), COMMODITY_ERROR(3, "商品已失效");

        private int value;
        private String expr;

        private STATUS_ENUE(int value, String expr) {
            this.value = value;
            this.expr = expr;
        }

        @Override public int getValue() {
            return value;
        }

        @Override public String getExpr() {
            return expr;
        }

    }
}
