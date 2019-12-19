/**
 * Copyright (c) 2015 Qunar.com. All Rights Reserved.
 */
package com.demai.cornel.vo.supplier;

import com.demai.cornel.dmEnum.IEmus;
import com.demai.cornel.vo.delivery.DeliveryTaskListResp;
import lombok.Data;

/**
 * Create By tfzhu  2019/12/19  7:36 AM
 */
@Data
public class SupplierTaskListResp {
    /***
     * 任务id
     */
    private String taskId;
    /***
     *任务标题
     */
    private String title;
    /***
     *出发地
     */
    private String dep;
    /***
     *目的地
     */
    private String arr;
    /***
     *出发地经纬度
     */
    private String depGis;
    /***
     *目的地经纬度
     */
    private String arrGis;
    /***
     *行程距离
     */
    private Double distance;
    /***
     *单位价格  50priceUnit/unit   50元/吨
     */
    private Double unitPrice;
    /***
     *货品单位
     */
    private String unit;
    /***
     *价格单位  元
     */
    private String priceUnit;

    /***
     *订单信息
     */
    private DeliveryTaskListResp.DaileryTaskOrderInfo orderInfo;

    public static enum STATUS_ENUE implements IEmus {
        NO_USER(-1, "用户不存在，请先注册"),
        SUCCESS(0, "请求成功"),
        NETWORK_ERROR(1, "网络异常，请稍后重试"),
        OPENID_ERROR(2, "opendId获取失败"),
        MSG_CODE_ERROR(3, "短信验证码错误");

        private int value;
        private String expr;

        private STATUS_ENUE(int value, String expr) {
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
