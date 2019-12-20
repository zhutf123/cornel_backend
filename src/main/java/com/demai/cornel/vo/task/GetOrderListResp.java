package com.demai.cornel.vo.task;

import com.demai.cornel.dmEnum.IEmus;
import com.demai.cornel.vo.user.UserLoginResp;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author binz.zhang
 * @Date: 2019-12-19    21:28
 */
@Data
public class GetOrderListResp {


    /**
     * orderId : 1231232
     * taskId : 12312
     * title : 黑龙江丰收农场一级玉米
     * dep : 黑龙江省鹤岗市xxxx
     * arr : 青岛琅琊码头
     * depGis : xxxxx,xxxx
     * arrGis : xxxxx,xxxx
     * startValidDate : 2019-11-07
     * endValidDate : 2019-11-10
     * supplierMobile : 13269712016
     * lorryId : 123123
     * plateNumber : 黑A123456
     * carryWeight : 30
     * OverCarryWight : 120
     * verifyCode : 5555
     * serviceMobile : 010-123456789
     * distance : 500.0
     * orderWeight : 80
     * unitPrice : 50
     * unit : 吨
     * priceUnit : 元
     * startTime : 2019-11-07 12:00-13:00
     * status : 2
     */

    private String orderId;
    private String taskId;
    private String title;
    private String dep;
    private String arr;
    private String depGis;
    private String arrGis;
    private String startValidDate;
    private String endValidDate;
    private String supplierMobile;
    private int lorryId;
    private String plateNumber;
    private BigDecimal carryWeight;
    private BigDecimal overCarryWight;
    private String verifyCode;
    private String serviceMobile;
    private BigDecimal distance;
    private String orderWeight;
    private int unitPrice;
    private String unit;
    private String priceUnit;
    private String startTime;
    private int status;
    private String unitDistance;


    public static enum CODE_ENUE implements IEmus {
        NO_USER(-1, "用户不存在，请先注册"),
        SUCCESS(0, "请求成功"),
        NETWORK_ERROR(1, "网络异常，请稍后重试"),
        OPENID_ERROR(2, "任务状态已更新，进入任务列表页"),
        PARAM_ERROR(3, "参数不合法");

        private int value;
        private String expr;

        private CODE_ENUE(int value, String expr) {
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

        public static UserLoginResp.CODE_ENUE getByValue(Integer value) {
            for (UserLoginResp.CODE_ENUE x : UserLoginResp.CODE_ENUE.values()) {
                if (x.getValue() == value) {
                    return x;
                }
            }
            return null;
        }

    }
}
