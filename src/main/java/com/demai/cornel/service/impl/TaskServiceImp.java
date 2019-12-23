package com.demai.cornel.service.impl;

import com.demai.cornel.auth.service.IUserService;
import com.demai.cornel.constant.ContextConsts;
import com.demai.cornel.dao.*;
import com.demai.cornel.holder.UserHolder;
import com.demai.cornel.model.*;
import com.demai.cornel.service.ITaskService;
import com.demai.cornel.util.CookieAuthUtils;
import com.demai.cornel.util.GenerateCodeUtils;
import com.demai.cornel.util.JacksonUtils;
import com.demai.cornel.vo.JsonResult;
import com.demai.cornel.vo.task.GetOrderListReq;
import com.demai.cornel.vo.task.GetOrderListResp;
import com.demai.cornel.vo.task.TaskSaveResp;
import com.demai.cornel.vo.task.TaskSaveVO;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Author binz.zhang
 * @Date: 2019-12-17 12:02
 */
@Service @Slf4j public class TaskServiceImp {
    @Resource private DistOrderInfoDao distOrderInfoDao;
    @Resource private LorryInfoDao lorryInfoDao;

    @Resource private TaskInfoDao taskInfoDao;
    @Resource private StringRedisTemplate stringRedisTemplate;
    @Resource private OrderInfoDao orderInfoDao;
    @Resource private UserInfoDao userInfoDao;
    private static final String ORDER_LOCK_FORMAT = "lock:task:%s";
    private static final String TASK_REST_WEIGHT_FORMAT = "REST:task:%s";
    private static final String TASK_ERROR_WEIGHT_FORMAT = "CONTINUE:task";

    /**
     * 司机侧查询task 列表
     *
     * @param userId
     * @param curId
     * @param pgsize
     * @return
     */
    public List<DistTaskOrderReq> getDistTaskList(String userId, Integer curId, Integer pgsize) {
        LorryInfo lorryInfo = lorryInfoDao.getDefaultLorryByUserId(userId);
        if (lorryInfo == null) {
            lorryInfo = lorryInfoDao.getAvailableLorryByUserId(userId);
        }
        BigDecimal estimatedWeight = (lorryInfo == null) ? new BigDecimal(50) : lorryInfo.getCarryWeight();
        List<DistTaskOrderReq> distTaskOrderReqs = distOrderInfoDao.getDistOrderListByUserID(userId, curId, pgsize);
        if (distTaskOrderReqs == null) {
            log.debug("get dist order info is null");
            return Collections.emptyList();
        }
        distTaskOrderReqs.stream().forEach(x -> {
            x.setIncome(x.getUnitWeightPrice().multiply(estimatedWeight));
            if (x.getOrderStatus() != null) {
                x.setTaskStatus(DistTaskOrderReq.STATUS_ENUE.TASK_CANCEL.getValue());
            } else if (!x.getTaskStatus().equals(TaskInfo.STATUS_ENUE.TASK_ING.getValue())
                    || getUnweightByTaskId(x.getTaskId()).compareTo(ContextConsts.MIN_CARRY_WEIGHT) == -1) {
                x.setTaskStatus(DistTaskOrderReq.STATUS_ENUE.TASK_REVIEW_SUCCESS.getValue());
            } else {
                x.setTaskStatus(DistTaskOrderReq.STATUS_ENUE.TASK_INIT.getValue());
            }
        });
        return distTaskOrderReqs;
    }

    /**
     * 司机侧获取task info
     *
     * @param userId
     * @param taskId
     * @return
     */
    public DriverTaskResp getTaskInfo(String userId, String taskId) {
        Preconditions.checkNotNull(userId);
        Preconditions.checkNotNull(taskId);
        List<TaskInfoReq.LorryInfoBean> lorryInfos = lorryInfoDao.getAllLorrySimpleInfoByUserId(userId);
        if (lorryInfos == null) {
            log.warn("getTaskInfo>> get lorry info null ");
            lorryInfos = Collections.EMPTY_LIST;
        }
        TaskInfo taskInfo = taskInfoDao.selectTaskInfoByTaskId(taskId);
        if (taskInfo == null) {
            log.warn("getTaskInfo>> get task info null task id is [{taskId}]");
            return null;
        }
        TaskInfoReq taskInfoReq = new TaskInfoReq(taskInfo);
        taskInfoReq.setDriverName(userInfoDao.getUserNameByUserId(userId));
        taskInfoReq.setLorryInfo(lorryInfos);
        List<TaskInfoReq.StartTime> startTimes = new ArrayList<>(taskInfo.getSubTaskTime().size());
        if (taskInfo.getSubTaskTime() != null) {
            taskInfo.getSubTaskTime().keySet().forEach(x -> {
                startTimes.add(TaskInfoReq.StartTime.builder().time(x).count(taskInfo.getSubTaskTime().get(x)).build());

            });
        }
        taskInfoReq.setStartTime(startTimes);
        DriverTaskResp driverTaskResp = new DriverTaskResp();
        BeanUtils.copyProperties(taskInfoReq,driverTaskResp);
        if(taskInfo.getUndistWeight().compareTo(ContextConsts.MIN_CARRY_WEIGHT)<0 || startTimes.size()<=0){
            driverTaskResp.setTaskStatus(DistTaskOrderReq.STATUS_ENUE.TASK_REVIEW_SUCCESS.getValue());
            return driverTaskResp;
        }
        List<String> recOrder = orderInfoDao.getOrderIdInnerTask(taskId,userId);
        if(recOrder!=null && recOrder.size()>0){
            driverTaskResp.setTaskStatus(DistTaskOrderReq.STATUS_ENUE.TASK_CANCEL.getValue());
            return driverTaskResp;
        }
        driverTaskResp.setTaskStatus(DistTaskOrderReq.STATUS_ENUE.TASK_INIT.getValue());
        return driverTaskResp;
    }

    /**
     * 查询盯订单的剩余未接单重量
     *
     * @return
     */
    public BigDecimal getUnweightByTaskId(String taskId) {
        String restWeight = stringRedisTemplate.opsForValue().get(String.format(TASK_REST_WEIGHT_FORMAT, taskId));
        return (Strings.isNullOrEmpty(restWeight)) ?
                taskInfoDao.getTaskUnacceptWeight(taskId) :
                new BigDecimal(restWeight);
    }
}
