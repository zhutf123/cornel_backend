/**
 * Copyright (c) 2019 dm.com. All Rights Reserved.
 */
package com.demai.cornel.dao;

import com.demai.cornel.model.TaskInfo;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

/**
 * Create By zhutf 19-10-6 下午1:18
 */
public interface TaskInfoDao {
    public void update(TaskInfo taskInfo);

    public void save(TaskInfo taskInfo);

    public TaskInfo selectTaskInfoByTaskId(@Param("taskId") String taskId);

    public void updateTaskUnDistWeight(@Param("unDistWeight") BigDecimal bigDecimal, @Param("taskId") String taskId);


}
