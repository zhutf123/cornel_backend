package com.demai.cornel.dao;

import com.demai.cornel.model.AclInfo;
import com.demai.cornel.model.DistOrderInfo;
import com.demai.cornel.model.DistTaskOrderReq;
import org.apache.ibatis.annotations.Param;

import java.sql.Date;
import java.util.List;

/**
 * @Author binz.zhang
 * @Date: 2019-11-07    16:53
 */
public interface DistOrderInfoDao {
    public void update(DistOrderInfo notifyInfo);

    public void save(DistOrderInfo notifyInfo);

    Integer getTaskCurrJobNo(@Param("taskId") String taskId);


    List<DistOrderInfo> getExpireNotify(@Param("curTime") Date curTime);

    List<DistTaskOrderReq> getDistOrderListByUserID(@Param("userId") String userId, @Param("curId") Integer ID, @Param("pgSize") Integer pgsize);
}
