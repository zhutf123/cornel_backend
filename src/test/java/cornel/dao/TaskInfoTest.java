package cornel.dao;

import com.demai.cornel.dao.TaskInfoDao;
import com.demai.cornel.model.TaskInfo;
import cornel.BaseTest;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.UUID;

/**
 * @Author binz.zhang
 * @Date: 2019-11-05    15:05
 */
@Slf4j
public class TaskInfoTest extends BaseTest {
    @Resource
    private TaskInfoDao taskInfoDao;


    @Test
    public void insertTask() throws ParseException {
        TaskInfo taskInfo = new TaskInfo();
        taskInfo.setTitle("2000吨优质大米大连到连云港");
        taskInfo.setTaskId(UUID.randomUUID().toString());
        taskInfo.setProduct("大米");
        taskInfo.setWeight(new BigDecimal(2000));
        taskInfo.setUnit("KG");
        taskInfo.setUnacceptWeight(new BigDecimal(2000));
        String s = "2019-11-08 23:59:59";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        java.util.Date d1 = sdf.parse(s);
        taskInfo.setEndTime(new Date(d1.getTime()));
        taskInfo.setDep("大连");
        taskInfo.setArr("连云港");
        taskInfo.setDistance(new BigDecimal(600));
        taskInfo.setUnitPrice(new BigDecimal(5.6));
        taskInfo.setLevel(1);
        taskInfoDao.save(taskInfo);
    }
}
