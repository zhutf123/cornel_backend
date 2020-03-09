package com.demai.purcharse.dao;

import com.demai.purcharse.model.WaybillInfo;

/**
* @Author binz.zhang
* @Date: 2020-02-11    21:28
*/
public interface WaybillInfoMapper {
    int insert(WaybillInfo record);

    int insertSelective(WaybillInfo record);
}