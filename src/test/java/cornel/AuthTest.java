package cornel;

import com.demai.cornel.auth.service.impl.UrlAclServiceImpl;
import com.demai.cornel.dao.AclInfoDao;
import com.demai.cornel.dao.RoleInfoDao;
import com.demai.cornel.dao.UserAclInfoDao;
import com.demai.cornel.dao.UserRoleInfoDao;
import com.demai.cornel.model.AclInfo;
import com.demai.cornel.model.RoleInfo;
import com.demai.cornel.model.UserAclInfo;
import com.demai.cornel.model.UserRoleInfo;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.util.List;

public class AuthTest extends BaseTest {

    @Resource
    private AclInfoDao aclInfoDao;
    @Resource
    private RoleInfoDao roleInfoDao;
    @Autowired
    private UrlAclServiceImpl urlAclService;
    @Resource
    private UserRoleInfoDao userRoleInfoDao;

    @Resource
    private UserAclInfoDao userAclInfoDao;

    @Test
    public void testInserAclInfo() {
        AclInfo aclInfo = new AclInfo();
        aclInfo.setCode("1111");
        aclInfo.setName("测试1");
        aclInfo.setModule(1);
        aclInfo.setStatus(1);
        aclInfoDao.save(aclInfo);
        boolean  fd = urlAclService.checkUserUrlAcls("admin","/test/demo2");
        List<String> acls = urlAclService.getURLAcls("/test/demo2");
        System.out.println("pk");

    }
    @Test
    public void testRoleInfo(){
        RoleInfo roleInfo = new RoleInfo();
        roleInfo.setName("管理员");
        roleInfo.setAclCode("c");
        roleInfo.setRoleId("admin");
        roleInfoDao.save(roleInfo);
    }

    @Test
    public void testUseRoleInfo(){
        UserRoleInfo userRoleInfo = new UserRoleInfo();
        userRoleInfo.setUserId("zhang.san");
        userRoleInfo.setRoleId("1");
        userRoleInfoDao.save(userRoleInfo);
    }
    @Test
    public void testUserAclInfo(){
        UserAclInfo userAclInfo = new UserAclInfo();
        userAclInfo.setAclCode("1");
        userAclInfo.setAclCode("4");
        userAclInfo.setUserId("admin");
        userAclInfoDao.save(userAclInfo);
    }


}
