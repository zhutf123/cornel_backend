package com.demai.cornel.constant;

import java.math.BigDecimal;

/**
 * Created by binz.zhang on 2018/12/28.
 * <p>
 * 系统常量
 */
public class ContextConsts {
    //todo 这儿的cookie的名字待定
    public static final String COOKIE_CKEY_NAME = "ckey";

    public static final String MDC_URI = "mdc_uri";

    public static final String MDC_TRACE_ID = "mdc_trace_id";

    public static final String MDC_USER = "mdc_user";

    public static final String MDC_START_TIME = "mdc_start_time";

    public static final Integer LORRY_OVER_WEIGHT_FACTOR = 4; //货车超重系数


    public static final BigDecimal MIN_CARRY_WEIGHT = new BigDecimal(30);
    public static final BigDecimal MIN_SHIPMENT_WEIGHT = new BigDecimal(30);
}
