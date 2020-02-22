package com.demai.cornel.purcharse.vo.resp;

import com.demai.cornel.dmEnum.IEmus;
import lombok.Data;

/**
 * @Author binz.zhang
 * @Date: 2020-02-21    22:40
 */
@Data
public class GetPurchaseDetailResp extends GetPurchaseListResp {
    private Integer optStatus;
    private String locationId;
    private String location;

    private String locationArea;
    private String locationDetail;

    public static enum STATUS_ENUE implements IEmus {
        PARAM_ERROE(-1, "参数错误"), SUCCESS(0, "请求成功"), SERVER_ERROR(1, "网络异常，请稍后重试"), PURCHASE_INVALID(2,
                "未查询到订单"), NO_AUTH(3, "无权限查看");

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
