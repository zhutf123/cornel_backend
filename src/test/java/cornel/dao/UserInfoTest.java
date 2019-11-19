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
        userInfo.setUserId("hubin.hu");
        userInfo.setName("胡滨");
        userInfo.setGender("male");
        userInfo.setNickName("胡滨");
        Set<String> tel = new TreeSet<String>();
        tel.add("13551151848");
        tel.add("13551151849");
        userInfo.setMobile(tel);
        Set<String> openId = new TreeSet<String>();
        openId.add("a-a-e");
        openId.add("a-a-f");
        openId.add("a-a-g");
        userInfo.setOpenId(openId);
        userInfo.setIdType(1);
        userInfo.setIdCard("370830199208251236");
        userInfoDao.save(userInfo);

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
