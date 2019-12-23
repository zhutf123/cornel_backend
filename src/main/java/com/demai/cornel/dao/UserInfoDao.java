package com.demai.cornel.dao;

import com.demai.cornel.model.UserDistOrderModel;
import com.demai.cornel.model.UserInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

public interface UserInfoDao {
    public void update(UserInfo userInfo);

    public void save(UserInfo userInfo);

    public UserInfo getUserInfoByPhone(@Param("phone") String phone);

    List<UserInfo> getUsersInfoByPhones(@Param("phones") Set<String> phones);

    List<UserDistOrderModel> getUserDefaultLorryByTels(@Param("phones") Set<String> phones);

    int updateUserOpenIdByUid(@Param("openIds") Set<String> openIds, @Param("userId") Long userId);

    List<UserDistOrderModel> getUsersLorryInfoByPlateNumber(@Param("platNumbers") Set<String> platNumber);

    UserInfo getUserInfoByUserId(@Param("userId") String userId);

    String getUserNameByUserId(@Param("userID") String userId);

}
