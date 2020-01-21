package com.demai.cornel.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

/**
 * @Author binz.zhang
 * @Date: 2019-12-18 15:42
 */
@Data public class TaskInfoReq {

    /**
     * taskId : 12312 title : 黑龙江丰收农场一级玉米 dep : 黑龙江省鹤岗市xxxx arr : 青岛琅琊码头 depGis : 116.40,39.90 arrGis : 104.07,30.67
     * startValidDate : 2019-11-07 endValidDate : 2019-11-10 lorryInfo :
     * [{"lorryId":123123,"plateNumber":"黑A123456","carryWeight":"30","OverCarryWight":"120","unitWeight":"吨","defaultSelect":false}]
     * distance : 500.0 price : 50 unitPrice : 元 unitWeight : 吨 starttime : [{"time":"2019-11-07
     * 8:00-12:00","count":5},{"time":"2019-11-07 12:00-14:00","count":3},{"time":"2019-11-07 15:00-17:00","count":2}]
     * status : 0
     */

    private String taskId;
    private String title;
    private String dep;
    private String arr;
    private String depGis;
    private String arrGis;
    private String startValidDate;
    private String endValidDate;
    private BigDecimal distance;
    private BigDecimal unitWeightPrice;
    private String unitPrice;
    private String unitWeight;
    private String unitDistance;
    private String driverName;
    private Long status;
    private List<LorryInfoBean> lorryInfo;
    private List<StartTime> startTime;

    public TaskInfoReq(TaskInfo taskInfo) {
        this.taskId = taskInfo.getTaskId();
        this.title = taskInfo.getTitle();
        this.dep = taskInfo.getDep();
        this.arr = taskInfo.getArr();
        this.depGis = taskInfo.getDepGis();
        this.arrGis = taskInfo.getArrGis();
        this.startValidDate = taskInfo.getStartTime();
        this.endValidDate = taskInfo.getEndTime();
        this.distance = taskInfo.getDistance();
        this.unitWeightPrice = taskInfo.getUnitWeightPrice();
        this.status = taskInfo.getStatus();
        this.unitPrice = taskInfo.getUnitPrice();
        this.unitDistance = taskInfo.getUnitDistance();
        this.unitWeight = taskInfo.getUnitWeight();
    }

    public TaskInfoReq() {
    }

    @Data public static class LorryInfoBean {
        /**
         * lorryId : 123123 plateNumber : 黑A123456 carryWeight : 30 OverCarryWight : 120 unitWeight : 吨 defaultSelect :
         * false
         */

        private int lorryId;
        private String plateNumber;
        private BigDecimal carryWeight;
        private BigDecimal overCarryWight;
        private String unitWeight;
        private boolean defaultSelect;
        private Integer carStatus;

    }

    @Data @Builder public static class StartTime {
        /**
         * lorryId : 123123 plateNumber : 黑A123456 carryWeight : 30 OverCarryWight : 120 unitWeight : 吨 defaultSelect :
         * false
         */

        private String time;
        private Integer count;

    }

}
