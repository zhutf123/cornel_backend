package com.demai.cornel.service;

import com.demai.cornel.model.DistOrderInfo;
import com.demai.cornel.model.DistTaskOrderReq;

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
}
