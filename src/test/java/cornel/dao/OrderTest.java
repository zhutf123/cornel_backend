package cornel.dao;

import com.demai.cornel.dao.OrderInfoDao;
import com.demai.cornel.model.OrderInfo;
import com.demai.cornel.model.TaskInfo;
import com.demai.cornel.util.DateFormatUtils;
import com.demai.cornel.util.JacksonUtils;
import com.demai.cornel.vo.task.GetOrderListResp;
import cornel.BaseTest;
import org.joda.time.DateTime;
import org.junit.Test;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @Author binz.zhang
 * @Date: 2019-12-21    22:12
 */
public class OrderTest extends BaseTest {

    @Resource private OrderInfoDao orderInfoDao;

    @Test public void insertTask() throws ParseException {
        List<GetOrderListResp> orderListResp = orderInfoDao.getOrderInfoBySupplier("hubin.hu", 1L,null,10);
       System.out.println("ok");
    }
}
