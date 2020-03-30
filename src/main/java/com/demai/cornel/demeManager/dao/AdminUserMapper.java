package com.demai.cornel.demeManager.dao;

import com.demai.cornel.demeManager.model.AdminUser;
import org.apache.ibatis.annotations.Param;

/**
 * @Author binz.zhang
 * @Date: 2020-03-08    12:12
 */
public interface AdminUserMapper {
    int insert(AdminUser record);

    int insertSelective(AdminUser record);

    AdminUser selectUserByTel(@Param("tel") String tel);
    AdminUser selectUserByUserId(@Param("userId") String userId);
}