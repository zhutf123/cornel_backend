/**
 * Copyright (c) 2015 Qunar.com. All Rights Reserved.
 */
package com.demai.cornel.service;

import com.demai.cornel.dao.TaskInfoDao;
import com.demai.cornel.vo.supplier.SupplierTaskListResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Create By tfzhu  2019/12/19  8:08 AM
 * 烘干塔服务接口
 */
@Service
@Slf4j
public class SupplierTaskService {

    @Resource
    private TaskInfoDao taskInfoDao;

    /**
     * 根据用户烘干塔用户id 订单状态查询任务订单
     * @param userId
     * @param status
     */
    public List<SupplierTaskListResp> getTaskOrderListByStatus(String userId,Long status){

        taskInfoDao.getTaskOrderListByStatus(userId,status);

        return null;
    }

}
