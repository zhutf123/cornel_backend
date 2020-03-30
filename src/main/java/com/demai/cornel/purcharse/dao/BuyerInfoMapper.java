package com.demai.cornel.purcharse.dao;

import com.demai.cornel.demeManager.vo.AdminGetBuyerResp;
import com.demai.cornel.purcharse.model.BuyerInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * @Author binz.zhang
 * @Date: 2020-02-12    17:18
 */
public interface BuyerInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(BuyerInfo record);

    int insertSelective(BuyerInfo record);
    BuyerInfo selectByUserId(@Param("userId")String userId);
    BuyerInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BuyerInfo record);

    int updateByPrimaryKey(BuyerInfo record);

    BuyerInfo selectByUserIdByLogin(@Param("phone") String phone);

    int updateFrequentlyLocation(@Param("userId") String userId,
            @Param("frequentlyLocation") Set<String> frequentlyLocation,
            @Param("defaultLocation") String defaultLocation);

    int updateDefaultLocation(@Param("userId") String userId,
            @Param("defaultLocation") String defaultLocation);


    BuyerInfo selectBuyInfoByPhone(@Param("phone")String phone);


    List<AdminGetBuyerResp> adminGetBuyerList(@Param("offset")Integer offset,@Param("pgSzie")Integer pgSize);



}
