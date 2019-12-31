package com.demai.cornel.dao;

import com.demai.cornel.model.Commodity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author binz.zhang
 * @Date: 2019-12-27    16:42
 */
public interface CommodityDao {
    List<Commodity> getCommdityList(@Param("userId")String userId);

    int save(Commodity commodity);
}
