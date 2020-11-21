package com.demai.cornel.demeManager.dao;

import com.demai.cornel.demeManager.model.ReviewLog;
import org.apache.ibatis.annotations.Param;

/**
* @Author binz.zhang
* @Date: 2020-04-08    18:30
*/
public interface ReviewLogMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ReviewLog record);

    int insertSelective(ReviewLog record);

    ReviewLog selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ReviewLog record);

    int updateByPrimaryKey(ReviewLog record);

    ReviewLog selectByOrderId(@Param("orderId") String orderId,@Param("operatorType")Integer operatorType);

}