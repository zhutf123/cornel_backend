package com.demai.cornel.purcharse.vo.resp;

import com.demai.cornel.dmEnum.IEmus;
import lombok.Data;

/**
 * @Author binz.zhang
 * @Date: 2020-02-22    23:09
 */
@Data
public class GetSaleDetailResp extends GetSaleOrderListResp {
    private String contactUserName;
    private String contactMobile;
    private String receiveLocationId;
    private String receiveLocation;

    private Integer optResult;
    public static enum STATUS_ENUE implements IEmus {
        PARAM_ERROR(-1, "参数错误"), SUCCESS(0, "请求成功"),
        SERVER_ERROR(1, "网络异常，请稍后重试"),
        USER_ERROR(2, "用户权限"),
        purcahse_INVALID(3,"订单无效");

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
