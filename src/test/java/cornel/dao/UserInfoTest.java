package cornel.dao;

import com.demai.cornel.dao.UserInfoDao;
import com.demai.cornel.model.UserDistOrderModel;
import com.demai.cornel.model.UserInfo;
import cornel.BaseTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.*;

@Slf4j
public class UserInfoTest extends BaseTest {

    @Resource
    private UserInfoDao userInfoDao;


    @Test
    public void insertUserInfo() {
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId("hubin.hu2");
        userInfo.setName("胡滨");
        userInfo.setGender("male");
        userInfo.setNickName("胡滨");
        Set<String> tel = new TreeSet<String>();
        tel.add("13551151848");
        tel.add("13551151849");
        userInfo.setMobile(tel);
        Set<String> openId = new TreeSet<String>();
        openId.add("a-a-SSSSe");
        openId.add("a-a-dddf");
        openId.add("a-a-SSSSg");
        userInfo.setOpenId(openId);
        userInfo.setIdType(1);
        userInfo.setIdCard("370830199208251236");
       // userInfoDao.save(userInfo);
        userInfoDao.updateUserOpenIdByUid(openId,8L);
        UserInfo userInfo2 = userInfoDao.getUserInfoByPhone("13551151848");



    }


    @Test
    public void selectUsers(){
        Set<String>tels = new HashSet<>();
        tels.add("13551151842");
        tels.add("13551151843");
        String [] so = tels.toArray(new String[0]);
        List<UserDistOrderModel>userInfos = userInfoDao.getUsersLorryInfoByPlateNumber(tels);
        System.out.println("ok");
    }
}
