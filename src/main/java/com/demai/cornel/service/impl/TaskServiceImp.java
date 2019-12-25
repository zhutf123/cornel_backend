package com.demai.cornel.service.impl;

import com.demai.cornel.constant.ContextConsts;
import com.demai.cornel.dao.*;
import com.demai.cornel.model.*;
import com.demai.cornel.service.ITaskService;
import com.demai.cornel.service.OrderService;
import com.demai.cornel.util.CookieAuthUtils;
import com.demai.cornel.util.GenerateCodeUtils;
import com.demai.cornel.util.JacksonUtils;
import com.demai.cornel.util.json.JsonUtil;
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
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
    @Resource private OrderService orderService;
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
            log.warn("get default lorry result is null user id is [{}]",userId);
            lorryInfo = lorryInfoDao.getAvailableLorryByUserId(userId);
        }
        BigDecimal estimatedWeight = (lorryInfo == null) ? new BigDecimal(50) : lorryInfo.getCarryWeight();
        List<DistTaskOrderReq> distTaskOrderReqs = distOrderInfoDao.getDistOrderListByUserID(userId, curId, pgsize);
        if (distTaskOrderReqs == null) {
            log.debug("get dist order info is null,user id is [{}]",userId);
            return Collections.emptyList();
        }

        distTaskOrderReqs.stream().forEach(x -> {
            x.setIncome(x.getUndistWeight().multiply(estimatedWeight));
            x.setRestWeight(x.getUndistWeight());
            x.setTaskStatus(checkTaskStatus(x, userId));

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
        BeanUtils.copyProperties(taskInfoReq, driverTaskResp);
        // 校验当前用户对task的状态
        driverTaskResp.setTaskStatus(checkTaskStatus(taskInfo, userId));
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

    /**
     * 校验当前用户对task的状态         TASK_INIT(0, "未接单"), ACCEPT(1, "已接单"), TASK_REVIEW_SUCCESS(2, "已闭仓");
     * @param taskInfo
     * @param userID
     * @return
     */
    Long checkTaskStatus(TaskInfo taskInfo, String userID) {
        if (taskInfo == null || !taskInfo.getStatus().equals(TaskInfo.STATUS_ENUE.TASK_ING.getValue())
                || taskInfo.getUndistWeight().compareTo(ContextConsts.MIN_CARRY_WEIGHT) < 0
                || orderService.getAvailableSelectTime(taskInfo).size() <= 0) {
            log.info("check task id [{}] status  is 2 ",taskInfo.getTaskId());
            return DistTaskOrderReq.STATUS_ENUE.TASK_REVIEW_SUCCESS.getValue();
        }
        List<String> runningOrder = orderInfoDao.getRuningOrderIdInnerTask(taskInfo.getTaskId(), userID);
        if (runningOrder != null && runningOrder.size() > 0) {
            log.info("check task id [{}] user id is [{}] status is 1 ",taskInfo.getTaskId(),userID);
            return DistTaskOrderReq.STATUS_ENUE.ACCEPT.getValue();
        }
        log.info("check task id [{}] user id is [{}] status is 0",taskInfo.getTaskId(),userID);
        return DistTaskOrderReq.STATUS_ENUE.TASK_INIT.getValue();
    }

    Long checkTaskStatus(DistTaskOrderReq taskInfoRe, String userID) {
        TaskInfo taskInfo = taskInfoDao.selectTaskInfoByTaskId(taskInfoRe.getTaskId());
        return checkTaskStatus(taskInfo, userID);
    }

    String formatTime(Timestamp date){
        try {
            if (date == null) {
                return null;
            }
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return simpleDateFormat.format(new Date(date.getTime()));
        }catch (Exception e){

            return null;
        }
    }
}
