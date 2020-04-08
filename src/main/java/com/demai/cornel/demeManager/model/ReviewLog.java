package com.demai.cornel.demeManager.model;

import java.sql.Timestamp;
import java.util.Date;

import com.demai.cornel.dmEnum.IEmus;
import lombok.Data;

/**
* @Author binz.zhang
* @Date: 2020-04-08    18:30
*/
@Data
public class ReviewLog {
    /**
    * 自增ID
    */
    private Integer id;

    /**
    * 审核员
    */
    private String operatorUser;

    /**
    * 操作时间
    */
    private Timestamp operatorTime;

    /**
    * 1 是业务人员审批。2是财务人员审批
    */
    private Integer operatorType;

    /**
    * 审核员修改可那些内容
    */
    private String changeContent;

    /**
    * 审批意见
    */
    private String reviewOpt;

    /**
    * 审核订单ID
    */
    private String orderId;

    /**
    * 审核动作 拒绝还是同意还是继续修改
    */
    private Integer optType;




    public static enum OPERATOR_TYPE_ENUM implements IEmus {
        business(1, "业务人员"), Financial(2, "财务人员");
        private int value;
        private String expr;

        private OPERATOR_TYPE_ENUM(int value, String expr) {
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