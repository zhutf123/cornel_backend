package cornel;

import com.demai.cornel.dao.AclInfoDao;
import com.demai.cornel.model.AclInfo;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class AuthTest extends BaseTest{

    @Autowired
    private AclInfoDao aclInfoDao;
    @Test
    public void testInserAclInfo(){
        AclInfo aclInfo =  new AclInfo();
        aclInfo.setCode("1111");
        aclInfo.setName("测试1");
        aclInfo.setModule(1);
        aclInfo.setStatus(1);
        aclInfoDao.save(aclInfo);

    }




}
