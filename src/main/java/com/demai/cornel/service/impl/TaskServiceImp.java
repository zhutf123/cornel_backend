package com.demai.cornel.service.impl;

import com.demai.cornel.constant.ContextConsts;
import com.demai.cornel.dao.DistOrderInfoDao;
import com.demai.cornel.dao.LorryInfoDao;
import com.demai.cornel.dao.OrderInfoDao;
import com.demai.cornel.dao.TaskInfoDao;
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
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Author binz.zhang
 * @Date: 2019-12-17    12:02
 */
@Service
@Slf4j
public class TaskServiceImp implements ITaskService {
    @Resource
    private DistOrderInfoDao distOrderInfoDao;
    @Resource
    private LorryInfoDao lorryInfoDao;

    @Resource
    private TaskInfoDao taskInfoDao;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private OrderInfoDao orderInfoDao;

    private static final String ORDER_LOCK_FORMAT = "lock:task:%s";
    private static final String TASK_REST_WEIGHT_FORMAT = "REST:task:%s";
    private static final String TASK_ERROR_WEIGHT_FORMAT = "CONTINUE:task";

    @Override
    public List<DistTaskOrderReq> getDistTaskList(String userId, Integer curId, Integer pgsize) {
        Preconditions.checkNotNull(userId);
        LorryInfo lorryInfo = lorryInfoDao.getDefaultLorryByUserId(userId);
        if (lorryInfo == null) {
            lorryInfo = lorryInfoDao.getAvailableLorryByUserId(userId);
        }
        BigDecimal estimatedWeight = (lorryInfo == null) ? new BigDecimal(50) : lorryInfo.getCarryWeight();
        List<DistTaskOrderReq> distTaskOrderReqs = distOrderInfoDao.getDistOrderListByUserID(userId, curId, pgsize);
        if (distTaskOrderReqs == null) {
            return Collections.emptyList();
        }
        distTaskOrderReqs.stream().forEach(x -> {
            if (x.getUnitDistance() == null) x.setUnitDistance("km");
            if (x.getUnitPrice() == null) x.setUnitPrice("元");
            if (x.getUnitWeight() == null) x.setUnitWeight("吨");
            x.setIncome(x.getPrice().multiply(estimatedWeight));
        });
        return distTaskOrderReqs;
    }

    @Override
    public TaskInfoReq getTaskInfo(String userId, String taskId) {
        Preconditions.checkNotNull(userId);
        Preconditions.checkNotNull(taskId);
        List<TaskInfoReq.LorryInfoBean> lorryInfos = lorryInfoDao.getAllLorrySimpleInfoByUserId(userId);
        if (lorryInfos == null) {
            lorryInfos = Collections.EMPTY_LIST;
        }
        TaskInfo taskInfo = taskInfoDao.selectTaskInfoByTaskId(taskId);
        if (taskInfo == null) {
            return null;
        }
        TaskInfoReq taskInfoReq = new TaskInfoReq(taskInfo);
        taskInfoReq.setLorryInfo(lorryInfos);
        taskInfoReq.setStarttime(taskInfo.getSubTaskTime());
        return taskInfoReq;
    }

    @Override
    public JsonResult saveTask(TaskSaveVO taskSaveVO) {

        Boolean lock = stringRedisTemplate.opsForValue().setIfAbsent(String.format(ORDER_LOCK_FORMAT, taskSaveVO.getTaskId()), "1", 2, TimeUnit.SECONDS);
        if (null == lock || !lock) {
            return JsonResult.successStatus(TaskSaveVO.CODE_ENUE.ORDER_FAIL);
        }
        TaskSaveResp taskSaveRep = new TaskSaveResp();

        TaskInfo taskInfo = taskInfoDao.selectTaskInfoByTaskId(taskSaveVO.getTaskId());
        String redisWeight = stringRedisTemplate.opsForValue().get(String.format(TASK_REST_WEIGHT_FORMAT, taskSaveVO.getTaskId()));
        BigDecimal restWeight = null;
        if (Strings.isNullOrEmpty(redisWeight)) {
            stringRedisTemplate.opsForValue().set(String.format(TASK_REST_WEIGHT_FORMAT, taskSaveVO.getTaskId()), taskInfo.getUnacceptWeight().toString(), 2, TimeUnit.HOURS);
            restWeight = new BigDecimal(stringRedisTemplate.opsForValue().get(String.format(TASK_REST_WEIGHT_FORMAT, taskSaveVO.getTaskId())));
        }
        if (restWeight == null) {
            stringRedisTemplate.delete(String.format(ORDER_LOCK_FORMAT, taskSaveVO.getTaskId()));
            return JsonResult.successStatus(TaskSaveVO.CODE_ENUE.TASK_CODE_ERROR);
        }
        if (restWeight.compareTo(taskSaveVO.getCarryWeight()) == -1) {
            stringRedisTemplate.delete(String.format(ORDER_LOCK_FORMAT, taskSaveVO.getTaskId()));
            taskSaveRep.setStatus(TaskSaveResp.CODE_ENUE.WEIGHT_ERROR.getValue());
            taskSaveRep.setRestWeight(restWeight);
            return JsonResult.success(taskSaveRep);
        }
        restWeight = restWeight.subtract(taskSaveVO.getCarryWeight());
        stringRedisTemplate.opsForValue().set(String.format(TASK_REST_WEIGHT_FORMAT, taskSaveVO.getTaskId()), restWeight.toString(), 2, TimeUnit.HOURS);
        stringRedisTemplate.delete(String.format(ORDER_LOCK_FORMAT, taskSaveVO.getTaskId()));

        LorryInfo lorryInfo = lorryInfoDao.getLorryByLorryID(taskSaveVO.getLarryId());

        // 车辆是否有效
        if (lorryInfo == null || !lorryInfo.getStatus().equals(1)) {
            stringRedisTemplate.opsForList().leftPush(String.format(TASK_ERROR_WEIGHT_FORMAT, taskSaveVO.getTaskId()), taskSaveVO.getCarryWeight().toString());
            return JsonResult.successStatus(TaskSaveVO.CODE_ENUE.MSG_CODE_ERROR);
        }
        // 提交重量超过限定的最大载重
        BigDecimal maxCarryWeight = new BigDecimal(String.valueOf(ContextConsts.LORRY_OVER_WEIGHT_FACTOR));
        if (taskSaveVO.getCarryWeight().compareTo(lorryInfo.getCarryWeight().multiply(maxCarryWeight)) == 1) {
            stringRedisTemplate.opsForList().leftPush(String.format(TASK_ERROR_WEIGHT_FORMAT, taskSaveVO.getTaskId()), taskSaveVO.getCarryWeight().toString());
            taskSaveRep.setStatus(TaskSaveResp.CODE_ENUE.WEIGHT_ERROR.getValue());
            taskSaveRep.setRestWeight(maxCarryWeight);
            return JsonResult.success(taskSaveRep);
        }
        // 提交的重量大于订单剩余的重量
        if (restWeight.compareTo(taskSaveVO.getCarryWeight()) == -1) {
            stringRedisTemplate.opsForList().leftPush(String.format(TASK_ERROR_WEIGHT_FORMAT, taskSaveVO.getTaskId()), taskSaveVO.getCarryWeight().toString());
            taskSaveRep.setStatus(TaskSaveResp.CODE_ENUE.WEIGHT_ERROR.getValue());
            taskSaveRep.setRestWeight(restWeight);
            return JsonResult.success(taskSaveRep);
        }
        OrderInfo orderInfo = new OrderInfo();
        try {
            if (taskInfoDao.updateTaskUnDistWeight(taskSaveVO.getCarryWeight(), taskSaveVO.getTaskId()) != 1) {
                stringRedisTemplate.opsForList().leftPush(String.format(TASK_ERROR_WEIGHT_FORMAT, taskSaveVO.getTaskId()), taskSaveVO.getCarryWeight().toString());
                return JsonResult.successStatus(TaskSaveVO.CODE_ENUE.NETWORK_ERROR);
            }
            //todo 这一块的定义再想想
            orderInfo.setTaskId(taskSaveVO.getTaskId());
            orderInfo.setUserId(UserHolder.getValue(CookieAuthUtils.KEY_USER_NAME));
            orderInfo.setLorryId(lorryInfo.getId());
            orderInfo.setDistance(taskInfo.getDistance());
            orderInfo.setUnit(taskInfo.getUnit());
            orderInfo.setSendOutUserId(taskInfo.getSendOutUserId());
            orderInfo.setReceiverUserId(taskInfo.getReceiverUserId());
            orderInfo.setCarryWeight(taskSaveVO.getCarryWeight());
            orderInfo.setAcceptTime(new Date(System.currentTimeMillis()));
            orderInfo.setStatus(OrderInfo.STATUS_ENUE.ORDER_INIT.getValue());
            orderInfo.setReceiveCode(GenerateCodeUtils.generateRandomCode(4));
            orderInfo.setSendOutCode(GenerateCodeUtils.generateRandomCode(4));
            if (orderInfoDao.save(orderInfo) != 1) {
                log.error("save the order into db fail order info is [{}]", JacksonUtils.obj2String(orderInfo));
                stringRedisTemplate.opsForList().leftPush(String.format(TASK_ERROR_WEIGHT_FORMAT, taskSaveVO.getTaskId()), taskSaveVO.getCarryWeight().toString());
                return JsonResult.successStatus(TaskSaveVO.CODE_ENUE.NETWORK_ERROR);
            }
        } catch (Exception e) {
            log.error("save the order error order info is [{}]", JacksonUtils.obj2String(orderInfo), e);
            stringRedisTemplate.opsForList().leftPush(String.format(TASK_ERROR_WEIGHT_FORMAT, taskSaveVO.getTaskId()), taskSaveVO.getCarryWeight().toString());
            return JsonResult.successStatus(TaskSaveVO.CODE_ENUE.NETWORK_ERROR);
        }
        taskSaveRep.setStatus(TaskSaveResp.CODE_ENUE.SUCCESS.getValue());
        return JsonResult.success(taskSaveRep);
    }

    @Override
    public List<GetOrderListResp> getOrderList(GetOrderListReq getOrderListReq, String userId) {
        if (getOrderListReq == null || getOrderListReq.getOrderTyp() == null || Strings.isNullOrEmpty(userId)) {
            return null;
        }
        return orderInfoDao.getOrderInfoByOrderTypeAndUserId(userId,getOrderListReq.getOrderTyp(),getOrderListReq.getOrderId(),getOrderListReq.getPgSize());
    }
}
