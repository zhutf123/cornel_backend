package com.demai.purcharse.dao;

import com.demai.purcharse.model.OfferSheet;

/**
* @Author binz.zhang
* @Date: 2020-02-11    21:26
*/
public interface OfferSheetMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OfferSheet record);

    int insertSelective(OfferSheet record);

    OfferSheet selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OfferSheet record);

    int updateByPrimaryKey(OfferSheet record);
}