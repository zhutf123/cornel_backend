package com.demai.cornel.dao;

import com.demai.cornel.model.Commodity;
import com.demai.cornel.vo.commodity.GetCommodityListResp;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author binz.zhang
 * @Date: 2019-12-27    16:42
 */
public interface CommodityDao {
    List<GetCommodityListResp> getCommdityList(@Param("userId")String userId);

    int save(Commodity commodity);

    Commodity getCommodityByCommodityId(@Param("commodityId")String commodityId);
}
