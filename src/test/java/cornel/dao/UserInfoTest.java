package cornel.dao;

import com.demai.cornel.dao.UserInfoDao;
import com.demai.cornel.model.UserInfo;
import cornel.BaseTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.Set;
import java.util.TreeSet;

@Slf4j
public class UserInfoTest extends BaseTest {

    @Resource
    private UserInfoDao userInfoDao;


    @Test
    public void insertUserInfo() {
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId("bin.zhang");
        userInfo.setName("张滨");
        userInfo.setGender("male");
        userInfo.setNickName("张滨");
        Set<String> tel = new TreeSet<String>();
        tel.add("13551151842");
        userInfo.setMobile(tel);
        Set<String> openId = new TreeSet<String>();
        openId.add("a");
        openId.add("b");
        userInfo.setOpenId(openId);
        userInfoDao.save(userInfo);

    }
}
