/**
 * Copyright (c) 2019 dm.com. All Rights Reserved.
 */
package com.demai.cornel.dao;

import com.demai.cornel.model.CarCornInfo;
import com.demai.cornel.model.LorryInfo;
import com.demai.cornel.model.TaskInfoReq;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Create By zhutf 19-10-6 下午1:08
 */
public interface LorryInfoDao {
    public int updatupdateByLorryId(LorryInfo lorryInfo);

    public int save(LorryInfo lorryInfo);

    /**
     * 查询当前用户的默认车辆并且还处于绑定状态的车辆信息，
     *
     * @param userId
     * @return
     */
    public LorryInfo getDefaultLorryByUserId(@Param("userId") String userId);

    /**
     * 查询当前用户的默认车辆并且还处于绑定状态的车辆信息，
     *
     * @param userId
     * @return
     */
    public LorryInfo getAvailableLorryByUserId(@Param("userId") String userId);

    public List<TaskInfoReq.LorryInfoBean> getAllLorrySimpleInfoByUserId(@Param("userId") String userId);

    LorryInfo getLorryByLorryID(@Param("lorryId") String lorryID);

    /**
     * 更新车辆状态
     *
     * @param lorryID
     * @param status
     */
    public int updateLorryStatus(@Param("lorryId") String lorryID, @Param("status") Integer status);

    /**
     * 根据车牌号 车架号码 以及发动机号码查询车辆
     *
     * @param plateNo
     * @param frameNo
     * @param engineNo
     * @return
     */
    public LorryInfo getLorryInfoByFlaNoEngNoFraNo(@Param("plateNo") String plateNo, @Param("frameNo") String frameNo,
            @Param("engineNo") String engineNo);

    List<LorryInfo> getAllLorryInfoByUserId(@Param("userId") String userId);

    LorryInfo getLorryByStringLorryID(@Param("lorryId") String lorryID);


    int updateCarCornInfo(CarCornInfo carCornInfo);


    List<LorryInfo> getAllIdleLorry();

}
