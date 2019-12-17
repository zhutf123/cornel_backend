package com.demai.cornel.dao;

import com.demai.cornel.model.LorryGisInfo;
import com.demai.cornel.model.SubTaskInfo;

/**
 * @Author binz.zhang
 * @Date: 2019-11-10    20:08
 */
public interface SubTaskDao {
    public void update(SubTaskInfo subTaskInfo);

    public void save(SubTaskInfo lorryGisInfo);
}
