package com.demai.cornel.auth.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AclDao {


    List<String> selectAclsByUserId(@Param("userId") String userId);

    List<String> selectAclsByUrl(@Param("url") String url);
}
