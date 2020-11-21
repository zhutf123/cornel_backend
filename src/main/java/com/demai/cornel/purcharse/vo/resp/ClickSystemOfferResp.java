package com.demai.cornel.purcharse.vo.resp;

import com.demai.cornel.dmEnum.IEmus;
import com.demai.cornel.model.Commodity;
import com.demai.cornel.vo.quota.ClickSystemQuoteResp;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author binz.zhang
 * @Date: 2020-02-12    17:09
 */

@Data @Builder @AllArgsConstructor @NoArgsConstructor public class ClickSystemOfferResp {
    private String userName;  //联系人名字
    private String mobile = "";    //手机号
    private Commodity commodity; //货品
    private BigDecimal minBuyWeight;  //出货量
    private BigDecimal price;  //单位价格
    private String unitWeight; //出货量单位
    private String unitPrice; //价格单位
    private Integer status;
    private List<String> receiveLocation; //收货地址

    private String receiveStartTime;//预计收货时间
    private String receiveEndTime;//预计收货时间

    public static enum STATUS_ENUE implements IEmus {
        PARAM_ERROR(-1, "参数错误"), SUCCESS(0, "请求成功"), USER_ERROR(1, "无权限"), COMMODITY_ERROR(3, "商品已失效");

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
