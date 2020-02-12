package com.demai.cornel.purcharse.dao;

import com.demai.cornel.purcharse.model.BuyerInfo;import org.apache.ibatis.annotations.Param;

/**
 * @Author binz.zhang
 * @Date: 2020-02-12    17:18
 */
public interface BuyerInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(BuyerInfo record);

    int insertSelective(BuyerInfo record);

    BuyerInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BuyerInfo record);

    int updateByPrimaryKey(BuyerInfo record);

    BuyerInfo selectByUserId(@Param("userId") String userId);
}