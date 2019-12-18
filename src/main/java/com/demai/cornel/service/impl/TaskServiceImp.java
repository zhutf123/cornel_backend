package com.demai.cornel.service.impl;

import com.demai.cornel.constant.ContextConsts;
import com.demai.cornel.dao.DistOrderInfoDao;
import com.demai.cornel.dao.LorryInfoDao;
import com.demai.cornel.dao.TaskInfoDao;
import com.demai.cornel.model.*;
import com.demai.cornel.service.ITaskService;
import com.demai.cornel.vo.JsonResult;
import com.demai.cornel.vo.task.TaskSaveVO;
import com.google.common.base.Preconditions;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

/**
 * @Author binz.zhang
 * @Date: 2019-12-17    12:02
 */
@Service
public class TaskServiceImp implements ITaskService {
    @Resource
    private DistOrderInfoDao distOrderInfoDao;
    @Resource
    private LorryInfoDao lorryInfoDao;

    @Resource
    private TaskInfoDao taskInfoDao;


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
        TaskInfo taskInfo = taskInfoDao.selectTaskInfoByTaskId(taskSaveVO.getTaskId());
        if (checkTaskAvailable(taskInfo)) {
            return JsonResult.successStatus(TaskSaveVO.CODE_ENUE.TASK_CODE_ERROR);
        }
        LorryInfo lorryInfo = lorryInfoDao.getLorryByLorryID(taskSaveVO.getLarryId());
        if (lorryInfo == null || lorryInfo.getStatus().equals(1)) {
            return JsonResult.successStatus(TaskSaveVO.CODE_ENUE.MSG_CODE_ERROR);
        }
        if (taskSaveVO.getCarryWeight().compareTo(lorryInfo.getCarryWeight().multiply(new BigDecimal(String.valueOf(ContextConsts.LORRY_OVER_WEIGHT_FACTOR)))) == 1) {
            return JsonResult.successStatus(TaskSaveVO.CODE_ENUE.WEIGHT_CODE_ERROR);
        }
        return null;


    }

    private static boolean checkTaskAvailable(TaskInfo taskInfo) {
        if (taskInfo == null || taskInfo.getStatus().equals(0)) {
            return false;
        }
        return true;
    }
}

//    private static boolean checkTaskAvailable(TaskInfo taskInfo) {
//
//    }
