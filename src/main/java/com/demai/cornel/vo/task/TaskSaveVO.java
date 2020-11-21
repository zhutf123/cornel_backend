package com.demai.cornel.vo.task;

import com.demai.cornel.dmEnum.IEmus;
import com.demai.cornel.vo.user.UserLoginResp;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author binz.zhang
 * @Date: 2019-12-18    19:06
 */
@Data public class TaskSaveVO {

    /**
     * taskId : 123123
     * carryWeight : 150
     * larryId : 123123
     * selectTime : 2019-11-07 8:00-12:00
     */

    private String taskId;
    private BigDecimal carryWeight;
    private String larryId;
    private String selectTime;
    private String userId;



}
