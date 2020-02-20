package com.demai.cornel.purcharse.vo.resp;

import com.demai.cornel.dmEnum.IEmus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author binz.zhang
 * @Date: 2020-02-20    15:33
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OptPurchaseResp {
    private String purchaseId;
    private Integer optStatus;
    private Integer purchaseStatus;
    public static enum STATUS_ENUE implements IEmus {
        PARAM_ERROR(-1, "参数错误"),
        SUCCESS(0, "请求成功"),
        SERVER_ERROR(2, "网络异常，请稍后重试"),
        PURCHASE_INVALID(3,"更新失败 订单已失效"),
        USER_ERROR(4, "用户无权限修改"),
        SHELVES_ERROR(4,"已经成交不可下架或修改"),
        DELETE_ERROR(5,"已经删除不可删除或修改"),
        DEAL_ERROR(6,"已经成交不可删除或修改");


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
