package com.demai.cornel.service;

import com.demai.cornel.dao.DistOrderInfoDao;
import com.demai.cornel.dao.TaskInfoDao;
import com.demai.cornel.dao.UserInfoDao;
import com.demai.cornel.model.*;
import com.demai.cornel.util.DateFormatUtils;
import com.demai.cornel.vo.JsonResult;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @Author binz.zhang
 * @Date: 2019-11-05    14:54
 * 派单 下发接单短信
 */
@Slf4j
@Service
public class DistOrderService {

    @Autowired
    private TaskService taskService;
    @Resource
    private UserInfoDao userInfoDao;

    @Resource
    private DistOrderInfoDao distOrderInfoDao;

    @Resource
    private TaskInfoDao taskInfoDao;

    private static BigDecimal MIN_DIST_WEIGHT = new BigDecimal(10);//最小排单的重量 10吨
    private static Integer NOTIFY_EXPIRE_TIME = 2;//订单保留时间 单位hour


    /**
     * 根据上游给的手机号进行派单
     *
     * @param tels
     */
    public JsonResult distOrderByTels(Set<String> tels, String taskID) {
        if (Strings.isNullOrEmpty(taskID) || CollectionUtils.isEmpty(tels)) {
            return null;
        }
        TaskInfo taskInfo = taskService.getTaskInfoByTaskId(taskID);
        if (!checkTaskDistAvailable(taskInfo)) {
            log.warn("task [{}] dist order fail due to task invalid", taskID);
            return JsonResult.error("task invalid");
        }
        List<UserDistOrderModel> orderModels = userInfoDao.getUserDefaultLorryByTels(tels);
        return distOrderEntity(orderModels, taskInfo);
    }


    /**
     * 根据上游给的车辆信息进行派单
     *
     * @param plateNumber 车牌号
     */
    public JsonResult distOrderByPlateNumber(Set<String> plateNumber, String taskID) {
        if(CollectionUtils.isEmpty(plateNumber)){
            return JsonResult.error("plateNumber not available");
        }
        TaskInfo taskInfo = taskService.getTaskInfoByTaskId(taskID);
        if (!checkTaskDistAvailable(taskInfo)) {
            log.warn("task [{}] dist order fail due to task invalid", taskID);
            return JsonResult.error("task invalid");
        }
        List<UserDistOrderModel> orderModels = userInfoDao.getUsersLorryInfoByPlateNumber(plateNumber);
        return distOrderEntity(orderModels, taskInfo);
    }

    /**
     * 根据查询出来的车辆以及用户信息进行派单
     * @param orderModels
     * @param taskInfo
     * @return
     */
    public JsonResult distOrderEntity(List<UserDistOrderModel> orderModels, TaskInfo taskInfo) {
        if (orderModels == null || CollectionUtils.isEmpty(orderModels)) {
            log.warn("task [{}] dist order fail due to lorry not available ");
            return JsonResult.error("lorry not available");
        }
        DistOrderReturnModel distOrderReturnModel = new DistOrderReturnModel();
        distOrderReturnModel.setTaskTotalUnDistWeight(taskInfo.getUndistWeight());
        distOrderReturnModel.setDistNum(0);
        List<UserDistOrderModel> sortOrderByCarryWeight = orderModels.stream()
                .sorted((o1, o2) -> o2.getCarryWeight().compareTo(o1.getCarryWeight())).collect(Collectors.toList());
        sortOrderByCarryWeight.stream().forEach(x -> {
            try {
                BigDecimal distWeight = distOrder(x, taskInfo);
                if (distWeight != null) {
                    distOrderReturnModel.setDistNum(distOrderReturnModel.getDistNum() + 1);
                    distOrderReturnModel.setDistWeight(distOrderReturnModel.getDistWeight().add(distWeight));
                }
            } catch (RuntimeException e) {
                log.error("dist order error ", e);
            }
        });
        distOrderReturnModel.setUnDistWeight(taskInfo.getUndistWeight());
        return JsonResult.success(distOrderReturnModel);
    }


    /**
     * 校验任务是否还可以派单
     *
     * @return
     */
    private boolean checkTaskDistAvailable(TaskInfo taskInfo) {
        if (taskInfo == null || !taskInfo.getStatus().equals(1)) {
            return false;
        }
        if (taskInfo.getUndistWeight() == null) {
            taskInfo.setUndistWeight(taskInfo.getWeight());
        }
        if (taskInfo.getUndistWeight().compareTo(MIN_DIST_WEIGHT) != 1) {
            return false;
        }
        return true;
    }

    /**
     * -1 查不到用户 0 成功 2 发送失败，稍后重试
     * 发送派单短信
     *
     * @return
     */
    public Integer sendDistOrder(TaskInfo taskInfo, UserDistOrderModel userDistOrderModel) {
        //todo 发短信待补全
        return 0;
    }

    /**
     * 派单的主要逻辑 1、下发短信 2 更新数据库的状态，2.1 更新task表为派单重量 2.2 notify表插入数据
     *
     * @param userDistOrderModel
     * @param taskInfo
     * @return 返回本次派发的重量
     */
    @Transactional
    public BigDecimal distOrder(UserDistOrderModel userDistOrderModel, TaskInfo taskInfo) throws RuntimeException {
        BigDecimal taskUnDistWeight = taskInfo.getUndistWeight();
        if (taskUnDistWeight == null || taskUnDistWeight.compareTo(MIN_DIST_WEIGHT) != 1) {
            return null;
        }
        BigDecimal curDistWeight;
        //派发货物的重量与车载重
        if (taskUnDistWeight.compareTo(userDistOrderModel.getCarryWeight()) != 1) {
            curDistWeight = taskUnDistWeight;
        } else {
            curDistWeight = userDistOrderModel.getCarryWeight();
        }
        //todo operationTime 待补充
        DistOrderInfo distOrderInfo = new DistOrderInfo();
        Date curTime = new Date(System.currentTimeMillis());
        distOrderInfo.setUserId(userDistOrderModel.getUserId());
        distOrderInfo.setCreateTime(DateFormatUtils.formatDateTime(new Date(System.currentTimeMillis())));
        //distOrderInfo.setExpireTime(new Timestamp(System.currentTimeMillis()) + NOTIFY_EXPIRE_TIME * 60 * 60 * 10000));
        distOrderInfo.setTaskId(taskInfo.getTaskId());
        distOrderInfo.setMobile(userDistOrderModel.getMobile());
        distOrderInfo.setDistId(UUID.randomUUID().toString());
        Optional<Integer> taskCurrJobNo = Optional.ofNullable(distOrderInfoDao.getTaskCurrJobNo(taskInfo.getTaskId()));
        distOrderInfo.setJobNo(taskCurrJobNo.orElse(1));
        userDistOrderModel.setDistWeigth(curDistWeight);
        distOrderInfoDao.save(distOrderInfo);
        taskInfo.setUndistWeight(taskUnDistWeight.subtract(curDistWeight));
       // taskInfoDao.updateTaskUnDistWeight(taskInfo.getUndistWeight(), taskInfo.getTaskId());
        if (!sendDistOrder(taskInfo, userDistOrderModel).equals(0)) {
            throw new RuntimeException("send msg error");
        }
        return curDistWeight;
    }

    /**
     * 定时任务
     * 主要的逻辑 定时去notify 查询过期未接受的订单
     */
    public void timerTasks(){
        //todo
        List<DistOrderInfo> expireNotify = distOrderInfoDao.getExpireNotify(new Date(System.currentTimeMillis()));
        if(CollectionUtils.isEmpty(expireNotify)){
            return;
        }
        return;
    }

}
