/**
 * Copyright (c) 2019 dm.com. All Rights Reserved.
 */
package com.demai.cornel.dao;

import com.demai.cornel.model.TaskInfo;

/**
 * Create By zhutf 19-10-6 下午1:18
 */
public interface TaskInfoDao {
    public void update(TaskInfo taskInfo);

    public void save(TaskInfo taskInfo);
}
