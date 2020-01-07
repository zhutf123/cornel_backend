package com.demai.cornel.vo.supplier;

import com.demai.cornel.dmEnum.IEmus;
import com.demai.cornel.model.Commodity;
import com.demai.cornel.vo.user.UserLoginResp;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author binz.zhang
 * @Date: 2020-01-07    12:25
 */
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class SupplierInfoResp {
    private Integer status;
    private String userId;
    private String userName;
    private String mobile;
    private String idCard;
    private String serviceMobile;
    List<TowerInfo> towerInfos;

    @Data public static class TowerInfo {
        List<Commodity> commoditys;
        private String towerId;
        private String locationArea;
        private String locationDetail;
        private String location;
    }

    public static enum CODE_ENUE implements IEmus {
        NO_USER(-1, "用户不存在，请先注册"), SUCCESS(0, "请求成功"), ORDER_FAIL(7, "抢单失败"), NETWORK_ERROR(1,
                "网络异常，请稍后重试"), OPENID_ERROR(2, "重新选择时间"), MSG_CODE_ERROR(3, "车辆状态无效重选车辆"), TASK_CODE_ERROR(4,
                "任务失效重新选择订单"), WEIGHT_CODE_ERROR(5, "超重请重新选择重量"), WEIGHT_TASK_CODE_ERROR(6, "订单剩余重量小于提交重量");

        private int value;
        private String expr;

        private CODE_ENUE(int value, String expr) {
            this.value = value;
            this.expr = expr;
        }

        @Override public int getValue() {
            return value;
        }

        @Override public String getExpr() {
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
