/**
 * Copyright (c) 2019 dm.com. All Rights Reserved.
 */
package com.demai.cornel.dao;

import com.demai.cornel.model.TaskInfo;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * Create By zhutf 19-10-6 下午1:18
 */
public interface TaskInfoDao {
    public void update(TaskInfo taskInfo);

    public void save(TaskInfo taskInfo);

    public TaskInfo selectTaskInfoByTaskId(@Param("taskId") String taskId);

    public int updateTaskUnDistWeightAndSelectTime(@Param("unDistWeight") BigDecimal bigDecimal, @Param("taskId") String taskId,@Param("selectTime")String selectTime);

    public List<Object> getTaskOrderListByStatus(@Param("supplierId") String userId, @Param("taskId") Long status);


    BigDecimal getTaskUnacceptWeight(@Param("taskId") String taskId);
}
