package cornel.dao;

import com.demai.cornel.model.DistTaskOrderReq;
import com.demai.cornel.model.TaskInfoReq;
import com.demai.cornel.service.DistOrderService;
import com.demai.cornel.service.OrderService;
import com.demai.cornel.service.TaskService;
import com.demai.cornel.service.impl.TaskServiceImp;
import com.demai.cornel.util.JacksonUtils;
import com.demai.cornel.vo.task.GetOrderListReq;
import com.demai.cornel.vo.task.TaskSaveVO;
import cornel.BaseTest;
import org.junit.Test;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @Author binz.zhang
 * @Date: 2019-11-07    17:59
 */
public class TaskServiceTest extends BaseTest {
    @Resource private DistOrderService distOrderService;

    @Resource private TaskServiceImp taskServiceImp;

    @Resource private OrderService orderService;

    @Test public void testDistOrder() {
        Set<String> phones = new HashSet<>();
        phones.add("13551151842");
        phones.add("13551151845");
        phones.add("13551151846");
        String taskId = "8011ec4d-5517-4d5a-b71b-0d011c11cb8f";
        distOrderService.distOrderByTels(phones, taskId);
    }

    @Test public void testDistOrderByPlatNum() {
        Set<String> phones = new HashSet<>();
        phones.add("京A123454");
        phones.add("京A123451");
        String taskId = "8011ec4d-5517-4d5a-b71b-0d011c11cb8f";
        distOrderService.distOrderByPlateNumber(phones, taskId);
    }

    @Test public void testGetTaskList() {
        List<DistTaskOrderReq> as = taskServiceImp.getDistTaskList("bin.zhang", null, 3);
        System.out.println(JacksonUtils.obj2String(as));
    }

    @Test public void testSaveTaskInfo() {
        TaskSaveVO taskInfoReq = new TaskSaveVO();
        taskInfoReq.setCarryWeight(new BigDecimal(100));
        //taskInfoReq.setLarryId(1);
        taskInfoReq.setTaskId("8011ec4d-5517-4d5a-b71b-0d011c11cb8f");
        taskInfoReq.setSelectTime("2019-08-29 12:00-14:00");
        orderService.saveOrder(taskInfoReq);
    }

    @Test public void testGetOrderList() {
        GetOrderListReq getOrderListReq = new GetOrderListReq();
        getOrderListReq.setOrderId("123");
        // getOrderListReq.setOrderType(1);
        getOrderListReq.setPgSize(10);
        // taskServiceImp.getOrderList(getOrderListReq,"binz.zhang");
        System.out.println("ok");
    }

}
