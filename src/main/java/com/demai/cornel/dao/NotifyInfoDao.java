package com.demai.cornel.dao;

import com.demai.cornel.model.AclInfo;
import com.demai.cornel.model.NotifyInfo;
import org.apache.ibatis.annotations.Param;

import java.sql.Date;
import java.util.List;

/**
 * @Author binz.zhang
 * @Date: 2019-11-07    16:53
 */
public interface NotifyInfoDao {
    public void update(NotifyInfo notifyInfo);

    public void save(NotifyInfo notifyInfo);

    Integer getTaskCurrJobNo(@Param("taskId") String taskId);


    List<NotifyInfo> getExpireNotify(@Param("curTime") Date curTime);
}
