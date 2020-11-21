package com.demai.cornel.purcharse.dao;

import com.demai.cornel.purcharse.model.StoreInfo;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
* @Author binz.zhang
* @Date: 2020-02-12    13:08
*/
public interface StoreInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(StoreInfo record);

    int insertSelective(StoreInfo record);

    StoreInfo selectByPrimaryKey(Integer id);
    StoreInfo selectByStoreId(String storeId);


    int updateUndistWeight(@Param("storeId")String storeId,@Param("beforeDist")BigDecimal orDist,@Param("afterDist")BigDecimal afterDist);

    List<StoreInfo> selectStoreIdByCommodityId(String commodityId);

    int updateByPrimaryKeySelective(StoreInfo record);

    int updateByPrimaryKey(StoreInfo record);

    List<StoreInfo> selectStoreIdByCommodityIdAndWeight(@Param("commodityId") String commodityId,@Param("weight")
            BigDecimal weight);

}