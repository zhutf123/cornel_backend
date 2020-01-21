package com.demai.cornel.service;

import com.demai.cornel.constant.ContextConsts;
import com.demai.cornel.dao.*;
import com.demai.cornel.holder.UserHolder;
import com.demai.cornel.model.LorryInfo;
import com.demai.cornel.model.OrderInfo;
import com.demai.cornel.model.TaskInfo;
import com.demai.cornel.model.TaskInfoReq;
import com.demai.cornel.model.UserInfo;
import com.demai.cornel.service.impl.TaskServiceImp;
import com.demai.cornel.util.*;
import com.demai.cornel.vo.JsonResult;
import com.demai.cornel.vo.order.ArriveArrResp;
import com.demai.cornel.vo.order.GetOrderInfoResp;
import com.demai.cornel.vo.order.OperationOrderResp;
import com.demai.cornel.vo.task.*;
import com.google.common.base.Strings;
import com.sun.org.apache.regexp.internal.RE;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @Author binz.zhang
 * @Date: 2019-12-20    20:31
 */
@Slf4j @Service public class OrderService {
    @Resource private LorryInfoDao lorryInfoDao;
    @Resource private TaskInfoDao taskInfoDao;
    @Resource private StringRedisTemplate stringRedisTemplate;
    @Resource private OrderInfoDao orderInfoDao;
    private static final String ORDER_LOCK_FORMAT = "lock:task:%s";
    @Resource UserInfoDao userInfoDao;

    public List<GetOrderListResp> getOrderList(GetOrderListReq getOrderListReq, String userId) {
        if (getOrderListReq == null || getOrderListReq.getOrderType() == null || Strings.isNullOrEmpty(userId)) {
            return Collections.EMPTY_LIST;
        }
        List<GetOrderListResp> getOrderListResps = orderInfoDao
                .getOrderInfoByOrderTypeAndUserId(userId, getOrderListReq.getOrderType(), getOrderListReq.getOrderId(),
                        getOrderListReq.getPgSize());
        if (getOrderListReq == null) {
            return Collections.EMPTY_LIST;
        }
        getOrderListResps.stream().forEach(x -> {
            if (CollectionUtils.isNotEmpty(x.getSupplierMobileSet())) {
                x.setSupplierMobile(x.getSupplierMobileSet().iterator().next());
            }
            if (CollectionUtils.isNotEmpty(x.getReceiverMobileSet())) {
                x.setReceiverMobile(x.getReceiverMobileSet().iterator().next());
            }
        });
        return getOrderListResps;
    }

    /**
     * 司机侧生成订单
     *
     * @param taskSaveVO
     * @return
     */
    public JsonResult saveOrder(TaskSaveVO taskSaveVO) {

        Boolean lock = stringRedisTemplate.opsForValue()
                .setIfAbsent(String.format(ORDER_LOCK_FORMAT, taskSaveVO.getTaskId()), "1", 5, TimeUnit.SECONDS);

        if (null == lock || !lock) {
            log.info("save order fail due to task lock task id is [{}]", taskSaveVO.getTaskId());
            return JsonResult.successStatus(TaskSaveResp.CODE_ENUE.ORDER_FAIL.getValue());
        }
        TaskSaveResp taskSaveRep = new TaskSaveResp();

        taskSaveRep.setDriverName(userInfoDao.getUserNameByUserId(UserHolder.getValue(CookieAuthUtils.KEY_USER_NAME)));

        TaskInfo taskInfo = taskInfoDao.selectTaskInfoByTaskId(taskSaveVO.getTaskId());
        // 校验task-- 0 可以接单 ;;  1 task 无效 或已经完成了;; 2 任务剩余重量小于提交接单的重量;;  3 该时间段不可选了
        Integer checkTaskStatus = checkTaskAvailable(taskInfo, taskSaveVO);
        // 校验task-- 订单失效
        if (checkTaskStatus.equals(1)) {
            stringRedisTemplate.delete(String.format(ORDER_LOCK_FORMAT, taskSaveVO.getTaskId()));
            log.debug("save order fail due to task Invalid task id is [{}] ", taskSaveVO.getTaskId());
            return JsonResult.successStatus(TaskSaveResp.CODE_ENUE.TASK_CODE_ERROR.getValue());
        }
        // 校验task-- 剩余重量小于提交重量
        if (checkTaskStatus.equals(2)) {
            log.debug("save order fail due to task reset weight [{}] less than order weight [{}]",
                    taskInfo.getUndistWeight().toString(), taskSaveVO.getCarryWeight().toString());
            stringRedisTemplate.delete(String.format(ORDER_LOCK_FORMAT, taskSaveVO.getTaskId()));
            taskSaveRep.setStatus(TaskSaveResp.INNER_CODE_ENUE.WEIGHT_ERROR.getValue());
            taskSaveRep.setRestWeight(taskInfo.getUndistWeight());
            return JsonResult.success(taskSaveRep);
        }
        // 校验task-- 校验时间段是否可选
        if (checkTaskStatus.equals(3)) {
            log.debug("save order fail due to select time not selectable");
            stringRedisTemplate.delete(String.format(ORDER_LOCK_FORMAT, taskSaveVO.getTaskId()));
            taskSaveRep.setStatus(TaskSaveResp.INNER_CODE_ENUE.ORDER_ERROR.getValue());
            taskSaveRep.setSelectTime(getAvailableSelectTime(taskInfo));
            return JsonResult.success(taskSaveRep);
        }
        // 提交重量小于最低接货重量
        if (checkTaskStatus.equals(4)) {
            log.debug("save order fail due to select time not selectable");
            stringRedisTemplate.delete(String.format(ORDER_LOCK_FORMAT, taskSaveVO.getTaskId()));
            taskSaveRep.setStatus(TaskSaveResp.INNER_CODE_ENUE.ORDER_WEIGHT_ERROR.getValue());
            taskSaveRep.setRestWeight(ContextConsts.MIN_CARRY_WEIGHT);
            return JsonResult.successStatus(TaskSaveResp.CODE_ENUE.ORDER_FAIL.getValue());
        }
        LorryInfo lorryInfo = lorryInfoDao.getLorryByLorryID(taskSaveVO.getLarryId());

        // 校验车辆信息 0 可接 1 车辆无效 2 提交载重大于车辆最大能承载的重量
        Integer checkLorryStatus = checkTaskAvailableLorry(lorryInfo, taskSaveVO);
        // 校验车辆-- 车辆状态不可调度
        if (checkLorryStatus.equals(1)) {
            log.debug("save order fail due to car status invalid car status is [{}] ", lorryInfo.getStatus());
            stringRedisTemplate.delete(String.format(ORDER_LOCK_FORMAT, taskSaveVO.getTaskId()));
            return JsonResult.successStatus(TaskSaveResp.CODE_ENUE.MSG_CODE_ERROR.getValue());
        }
        // 校验车辆-- 车辆载重小于提交重量
        if (checkLorryStatus.equals(2)) {
            log.debug("save order fail due to car max carry weight [{}] less than order weight [{}]",
                    lorryInfo.getOverCarryWeight().toString(), taskSaveVO.getCarryWeight().toString());
            stringRedisTemplate.delete(String.format(ORDER_LOCK_FORMAT, taskSaveVO.getTaskId()));
            taskSaveRep.setStatus(1);
            taskSaveRep.setRestWeight(lorryInfo.getOverCarryWeight());
            return JsonResult.successStatus(TaskSaveResp.CODE_ENUE.MSG_CODE_ERROR.getValue());
        }
        OrderInfo orderInfo = new OrderInfo();
        try {
            //todo 这一块的定义再想想
            orderInfo.setOrderId(IDUtils.getUUID());
            orderInfo.setTaskId(taskSaveVO.getTaskId());
            orderInfo.setUserId(UserHolder.getValue(CookieAuthUtils.KEY_USER_NAME));
            orderInfo.setLorryId(lorryInfo.getId());

            orderInfo.setDistance(taskInfo.getDistance());
            orderInfo.setUnitDistance(taskInfo.getUnitDistance());
            orderInfo.setCarryWeight(lorryInfo.getCarryWeight());
            orderInfo.setOrderWeight(taskSaveVO.getCarryWeight());
            orderInfo.setUnitWeight(taskInfo.getUnitWeight());

            orderInfo.setSendOutUserId(taskInfo.getSendOutUserId());
            orderInfo.setReceiverUserId(taskInfo.getReceiverUserId());
            orderInfo.setReceiveCode(GenerateCodeUtils.generateRandomCode(4));
            orderInfo.setSendOutCode(GenerateCodeUtils.generateRandomCode(4));

            orderInfo.setAcceptTime(DateFormatUtils.formatDateTime(new Date(System.currentTimeMillis())));
            orderInfo.setStartTime(DateFormatUtils.formatDateTime(new Date(System.currentTimeMillis())));
            orderInfo.setReceiveTime(taskSaveVO.getSelectTime());
            orderInfo.setStatus(OrderInfo.STATUS_ENUE.ORDER_INIT.getValue());

            Integer count = taskInfo.getSubTaskTime().get(taskSaveVO.getSelectTime()) - 1;
            taskInfo.getSubTaskTime().put(taskSaveVO.getSelectTime(), count);
            List<TaskInfoReq.StartTime> startTimes = buildStartTime(taskInfo.getSubTaskTime());
            // 减小task 库存
            if (taskInfoDao.updateTaskUnDistWeightAndSelectTime(
                    taskInfo.getUndistWeight().subtract(taskSaveVO.getCarryWeight()), taskSaveVO.getTaskId(),
                    JacksonUtils.obj2String(startTimes)) != 1) {
                log.error("update task un dist weight error task id is [{}]", taskSaveVO.getTaskId());
                stringRedisTemplate.delete(String.format(ORDER_LOCK_FORMAT, taskSaveVO.getTaskId()));
                return JsonResult.successStatus(TaskSaveResp.CODE_ENUE.NETWORK_ERROR.getValue());
            }
            // 保存订单
            if (orderInfoDao.save(orderInfo) != 1) {
                log.error("save the order into db fail order info is [{}]", JacksonUtils.obj2String(orderInfo));
                stringRedisTemplate.delete(String.format(ORDER_LOCK_FORMAT, taskSaveVO.getTaskId()));
                return JsonResult.successStatus(TaskSaveResp.CODE_ENUE.NETWORK_ERROR.getValue());
            }
            //锁定车辆状态
            lorryInfoDao.updateLorryStatus(taskSaveVO.getLarryId(), LorryInfo.STATUS_ENUE.USEING.getValue());
        } catch (Exception e) {
            log.error("save the order error order info is [{}]", JacksonUtils.obj2String(orderInfo), e);
            stringRedisTemplate.delete(String.format(ORDER_LOCK_FORMAT, taskSaveVO.getTaskId()));
            return JsonResult.successStatus(TaskSaveResp.CODE_ENUE.NETWORK_ERROR.getValue());
        }
        taskSaveRep.setStatus(TaskSaveResp.CODE_ENUE.SUCCESS.getValue());
        taskSaveRep.setVerCode(orderInfo.getReceiveCode());
        return JsonResult.success(taskSaveRep);
    }

    /**
     * 校验是否可以接单 0 可以接单 ;;  1 task 无效 或已经完成了;; 2 任务剩余重量小于提交接单的重量;;  3 该时间段不可选了 4提交重量小于最低接货重量
     *
     * @param taskInfo
     * @param taskSaveVO
     * @return
     */
    private Integer checkTaskAvailable(TaskInfo taskInfo, TaskSaveVO taskSaveVO) {
        if (taskInfo == null || !taskInfo.getStatus().equals(TaskInfo.STATUS_ENUE.TASK_ING.getValue())) {
            return 1;
        }
        if (taskInfo.getUndistWeight().compareTo(taskSaveVO.getCarryWeight()) == -1) {
            return 2;
        }
        if (taskInfo.getSubTaskTime().get(taskSaveVO.getSelectTime()) == null
                || taskInfo.getSubTaskTime().get(taskSaveVO.getSelectTime()).compareTo(0) != 1) {
            return 3;
        }
        //        if(taskSaveVO.getCarryWeight().compareTo(ContextConsts.MIN_CARRY_WEIGHT)<0){
        //            return 4;
        //        }
        return 0;
    }

    public HashMap<String, Integer> getAvailableSelectTime(TaskInfo taskInfo) {
        HashMap<String, Integer> availableTime = new HashMap<>();
        taskInfo.getSubTaskTime().keySet().forEach(x -> {
            if (taskInfo.getSubTaskTime().get(x) > 0) {
                availableTime.put(x, taskInfo.getSubTaskTime().get(x));
            }
        });
        return availableTime;
    }

    /**
     * 校验该车辆能否接当前这个单子
     *
     * @param lorryInfo
     * @return 0 可接 1 车辆无效 2 提交载重大于车辆最大能承载的重量
     */
    private Integer checkTaskAvailableLorry(LorryInfo lorryInfo, TaskSaveVO taskSaveVO) {
        if (lorryInfo == null || !lorryInfo.getStatus().equals(LorryInfo.STATUS_ENUE.IDLE.getValue())) {
            return 1;
        }
        BigDecimal maxCarryWeight = lorryInfo.getOverCarryWeight().multiply(lorryInfo.getCarryWeight());
        if (-1 == maxCarryWeight.compareTo(taskSaveVO.getCarryWeight())) {
            return 2;
        }
        return 0;
    }

    /**
     * 司机抵达烘干塔
     *
     * @param userId
     * @param orderId
     * @return
     */
    public ArriveDepDriverResp driverArriveDep(String userId, String orderId) {

        ArriveDepDriverResp arriveDepDriverResp = new ArriveDepDriverResp();
        arriveDepDriverResp.setOrderId(orderId);
        if (Strings.isNullOrEmpty(userId) || Strings.isNullOrEmpty(orderId)) {
            arriveDepDriverResp.setSuccess(false);
            return arriveDepDriverResp;
        }
        long arriveStatus = OrderInfo.STATUS_ENUE.ORDER_ARRIVE_DEP.getValue();
        if (!orderInfoDao.getDriverUserId(orderId).equals(userId)) {
            arriveDepDriverResp.setSuccess(false);
            return arriveDepDriverResp;
        }

        OrderInfo orderInfoUpdate = new OrderInfo();
        orderInfoUpdate.setStatus(arriveStatus);
        orderInfoUpdate.setOrderId(orderId);
        orderInfoUpdate.setUserId(userId);
        orderInfoUpdate.setOldStatus(OrderInfo.STATUS_ENUE.ORDER_INIT.getValue());
        if (orderInfoDao.updateShipmentStatusByOldStatus(orderInfoUpdate) != 1) {
            arriveDepDriverResp.setSuccess(false);
            return arriveDepDriverResp;
        }
        OrderInfo orderInfo = orderInfoDao.getOrderInfoByOrderId(orderId);
        arriveDepDriverResp.setOrderId(orderId);
        arriveDepDriverResp.setSuccess(true);
        arriveDepDriverResp.setStatus(arriveStatus);
        arriveDepDriverResp.setPickUpCode(orderInfo.getSendOutCode());
        return arriveDepDriverResp;
    }

    /**
     * 司机确认从烘干塔出库
     *
     * @param userId
     * @param orderId
     * @return
     */
    public OperationOrderResp confirmStockOut(String userId, String orderId) {
        OperationOrderResp operationOrderResp = new OperationOrderResp();
        if (Strings.isNullOrEmpty(userId) || Strings.isNullOrEmpty(orderId)) {
            operationOrderResp.setSuccess(false);
            return operationOrderResp;
        }
        long arriveStatus = OrderInfo.STATUS_ENUE.ORDER_ROUTING.getValue();
        Date curDate = new Date(System.currentTimeMillis());

        OrderInfo orderInfoUpdate = new OrderInfo();
        orderInfoUpdate.setOrderId(orderId);
        orderInfoUpdate.setUserId(userId);
        orderInfoUpdate.setStatus(arriveStatus);
        orderInfoUpdate.setOldStatus(OrderInfo.STATUS_ENUE.ORDER_SHIPMENT_OVER.getValue());

        orderInfoDao.updateShipmentStatusByOldStatus(orderInfoUpdate);

        SimpleDateFormat sft = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        operationOrderResp.setOrderId(orderId);
        operationOrderResp.setOpResult(OperationOrderResp.SUPPLIER_RESP_STATUS_ENUE.SUCCESS.getValue());

        operationOrderResp.setSendOutTime(sft.format(curDate).toString());
        operationOrderResp.setOrderStatus(arriveStatus);
        return operationOrderResp;
    }

    /**
     * 司机侧确认到达港口
     *
     * @param userId
     * @param orderId
     * @return
     */
    public ArriveArrResp arriveArr(String userId, String orderId) {
        long status = OrderInfo.STATUS_ENUE.ORDER_ARRIVE_ARR.getValue();
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setOldStatus(OrderInfo.STATUS_ENUE.ORDER_ROUTING.getValue());
        orderInfo.setStatus(status);
        orderInfo.setOrderId(orderId);
        orderInfo.setUserId(userId);
        orderInfoDao.updateShipmentStatusByOldStatus(orderInfo);
        String receiveCode = orderInfoDao.getReceiveCode(orderId);
        return ArriveArrResp.builder().
                orderId(orderId).orderStatus(status).verCode(receiveCode).build();
    }

    public OperationOrderResp driverConfrimTaskOver(String orderId, String userId) {
        long status = OrderInfo.STATUS_ENUE.ORDER_SUCCESS.getValue();
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setUserId(userId);
        orderInfo.setOrderId(orderId);
        orderInfo.setStatus(status);
        orderInfo.setOldStatus(OrderInfo.STATUS_ENUE.ORDER_DELIVERY_OVER.getValue());
        orderInfo.setFinishTime(DateFormatUtils.formatDateTime(new Date(System.currentTimeMillis())));
        int num = orderInfoDao.updateShipmentStatusByOldStatus(orderInfo);
        if (num == 0) {
            return OperationOrderResp.builder().opResult(1).build();
        }
        OrderInfo orderInfoCurr = orderInfoDao.getOrderInfoByOrderId(orderId);
        lorryInfoDao.updateLorryStatus(orderInfoCurr.getLorryId(), LorryInfo.STATUS_ENUE.IDLE.getValue());
        log.info("release car status car id is [{}] status is [{}]", orderInfoCurr.getLorryId(),
                LorryInfo.STATUS_ENUE.IDLE.getValue());
        return OperationOrderResp.builder().opResult(0).orderStatus(OrderInfo.STATUS_ENUE.ORDER_SUCCESS.getValue())
                .orderId(orderId).build();
    }

    public GetOrderListResp driverGetTaskInfo(String orderId) {
        GetOrderListResp getOrderInfoResp = orderInfoDao
                .getOrderInfoByUserAndOrderId(UserHolder.getValue(CookieAuthUtils.KEY_USER_NAME), orderId);
        //todo 校验为空
        if (CollectionUtils.isNotEmpty(getOrderInfoResp.getSupplierMobileSet())) {
            getOrderInfoResp.setSupplierMobile(getOrderInfoResp.getSupplierMobileSet().iterator().next());
        }
        if (CollectionUtils.isNotEmpty(getOrderInfoResp.getReceiverMobileSet())) {
            getOrderInfoResp.setReceiverMobile(getOrderInfoResp.getReceiverMobileSet().iterator().next());
        }
        return getOrderInfoResp;
    }

    List<TaskInfoReq.StartTime> buildStartTime(Map<String, Integer> stringIntegerHashMap) {
        List<TaskInfoReq.StartTime> startTimes = new ArrayList<>();
        if (stringIntegerHashMap == null) {
            return startTimes;
        }
        stringIntegerHashMap.keySet().forEach(x -> {
            TaskInfoReq.StartTime startTime = TaskInfoReq.StartTime.builder().time(x).count(stringIntegerHashMap.get(x))
                    .build();
            startTimes.add(startTime);
        });
        return startTimes;
    }

}
