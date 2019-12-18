package com.demai.cornel.service;

import com.demai.cornel.model.DistOrderInfo;
import com.demai.cornel.model.DistTaskOrderReq;
import com.demai.cornel.model.TaskInfoReq;
import com.demai.cornel.vo.JsonResult;
import com.demai.cornel.vo.task.TaskSaveVO;

import java.util.List;

/**
 * @Author binz.zhang
 * @Date: 2019-12-17    12:02
 */
public interface ITaskService {
    /**
     * 根据userID 获取派单的列表
     *
     * @param userId
     * @return
     */
    List<DistTaskOrderReq> getDistTaskList(String userId, Integer curId, Integer pgsize);


    TaskInfoReq getTaskInfo(String userId, String taskId);

    /**
     * 司机侧接单
     *
     * @param taskSaveVO
     */
    JsonResult saveTask(TaskSaveVO taskSaveVO);
}
