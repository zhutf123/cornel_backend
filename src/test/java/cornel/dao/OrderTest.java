package cornel.dao;

import com.demai.cornel.dao.OrderInfoDao;
import com.demai.cornel.model.OrderInfo;
import com.demai.cornel.model.TaskInfo;
import com.demai.cornel.util.JacksonUtils;
import cornel.BaseTest;
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

    @Resource
    private OrderInfoDao orderInfoDao;

    @Test
    public void insertTask() throws ParseException {
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setOrderId("12345678909876543");
        orderInfo.setFinishTime(new Timestamp(System.currentTimeMillis()));
        orderInfo.setSendOutTime(new Timestamp(System.currentTimeMillis()));
        orderInfoDao.save(orderInfo);
        System.out.println("ok");
    }
}
