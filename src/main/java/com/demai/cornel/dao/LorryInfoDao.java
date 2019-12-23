/**
 * Copyright (c) 2019 dm.com. All Rights Reserved.
 */
package com.demai.cornel.dao;

import com.demai.cornel.model.LorryInfo;
import com.demai.cornel.model.TaskInfoReq;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Create By zhutf 19-10-6 下午1:08
 */
public interface LorryInfoDao {
    public void update(LorryInfo lorryInfo);

    public void save(LorryInfo lorryInfo);

    /**
     * 查询当前用户的默认车辆并且还处于绑定状态的车辆信息，
     * @param userId
     * @return
     */
    public LorryInfo getDefaultLorryByUserId(@Param("userId") String userId);

    /**
     * 查询当前用户的默认车辆并且还处于绑定状态的车辆信息，
     * @param userId
     * @return
     */
    public LorryInfo getAvailableLorryByUserId(@Param("userId") String userId);

    public List<TaskInfoReq.LorryInfoBean> getAllLorrySimpleInfoByUserId(@Param("userId") String userId);

    LorryInfo getLorryByLorryID(@Param("lorryId")int lorryID);
    public void updateLorryStatus(@Param("lorryId")Integer lorryID,@Param("status")Integer status);

}
