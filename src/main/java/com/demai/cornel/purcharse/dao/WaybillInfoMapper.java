package com.demai.cornel.purcharse.dao;

import com.demai.cornel.purcharse.model.WaybillInfo;

import java.util.List;

/**
 * @Author binz.zhang
 * @Date: 2020-02-22    20:17
 */
public interface WaybillInfoMapper {
    int insert(WaybillInfo record);

    int insertSelective(WaybillInfo record);


    List<String> getSaleOrderDeliverId(String saleId);
}