package com.demai.cornel.dao;

import com.demai.cornel.model.UserInfo;
import org.apache.ibatis.annotations.Param;

public interface UserInfoDao {
    public void update(UserInfo userInfo);

    public void save(UserInfo userInfo);

    public UserInfo getUserInfoByPhone(@Param("phone") String phone);

}
