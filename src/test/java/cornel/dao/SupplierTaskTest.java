package cornel.dao;

import com.demai.cornel.service.SupplierTaskService;
import com.demai.cornel.vo.supplier.SupplierTaskListResp;
import com.demai.cornel.vo.task.GetOrderListReq;
import com.demai.cornel.vo.task.GetOrderListResp;
import cornel.BaseTest;
import org.junit.Test;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.Collection;

/**
 * @Author binz.zhang
 * @Date: 2019-12-23    14:42
 */
public class SupplierTaskTest extends BaseTest {

    @Resource private SupplierTaskService supplierTaskService;

    @Test public void getTaskList() throws ParseException {
        String supplierId="hubin.hu";
        GetOrderListReq param  = new GetOrderListReq();
        param.setPgSize(10);
        param.setOrderType(1L);
        Collection<SupplierTaskListResp> resps =  supplierTaskService.getTaskOrderListByStatus(supplierId,param);
        System.out.println("okkk");
    }
}

