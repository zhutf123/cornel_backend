package com.demai.cornel.purcharse.dao;

import com.demai.cornel.purcharse.model.StackOutInfo;
import org.apache.ibatis.annotations.Param;

/**
 * @Author binz.zhang
 * @Date: 2020-03-31    14:56
 */
public interface StackOutInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(StackOutInfo record);

    int insertSelective(StackOutInfo record);

    StackOutInfo selectByPrimaryKey(Integer id);

    StackOutInfo selectByOutId(String outId);

    int updateByPrimaryKeySelective(StackOutInfo record);

    int updateByPrimaryKey(StackOutInfo record);

    int updateTaskIdAndCargoId(@Param("taskId") String tsakId, @Param("cargoId") String cargoId,
            @Param("outId") String outId);
}