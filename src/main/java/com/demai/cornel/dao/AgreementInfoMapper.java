package com.demai.cornel.dao;

import com.demai.cornel.model.AgreementInfo;
import org.apache.ibatis.annotations.Param;

/**
* @Author binz.zhang
* @Date: 2020-01-08    17:14
*/
public interface AgreementInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(AgreementInfo record);

    int insertSelective(AgreementInfo record);

    AgreementInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AgreementInfo record);

    int updateByPrimaryKey(AgreementInfo record);


    AgreementInfo selectByAdapt(@Param("adapt") String  adapt);

}