package cornel.dao;

import com.demai.cornel.model.DistTaskOrderReq;
import com.demai.cornel.service.DistOrderService;
import com.demai.cornel.service.TaskService;
import com.demai.cornel.service.impl.TaskServiceImp;
import cornel.BaseTest;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @Author binz.zhang
 * @Date: 2019-11-07    17:59
 */
public class TaskServiceTest extends BaseTest {
    @Resource
    private DistOrderService distOrderService;

    @Resource
    private TaskServiceImp taskServiceImp;



    @Test
    public void testDistOrder(){
        Set<String> phones = new HashSet<>();
        phones.add("13551151842");
        phones.add("13551151845");
        phones.add("13551151846");
        String taskId = "8011ec4d-5517-4d5a-b71b-0d011c11cb8f";
        distOrderService.distOrderByTels(phones,taskId);
    }


    @Test
    public void testDistOrderByPlatNum(){
        Set<String> phones = new HashSet<>();
        phones.add("京A123454");
        phones.add("京A123451");
        String taskId = "8011ec4d-5517-4d5a-b71b-0d011c11cb8f";
        distOrderService.distOrderByPlateNumber(phones,taskId);
    }


    @Test
    public void testGetTaskList(){
        List<DistTaskOrderReq>as =  taskServiceImp.getDistTaskList("binz.zhang",null,1);
        System.out.println("ok");
    }
}
