package cornel.dao;

import com.demai.cornel.dao.LorryInfoDao;
import com.demai.cornel.model.LorryInfo;
import com.demai.cornel.util.DateFormatUtils;
import cornel.BaseTest;
import org.junit.Test;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * @Author binz.zhang
 * @Date: 2019-11-05    15:37
 */
public class LorryInfoTest extends BaseTest {
    @Resource
    private LorryInfoDao lorryInfoDao;

    @Test
    public void insertLorry() throws ParseException {
        LorryInfo lorryInfo = new LorryInfo();
        lorryInfo.setLorryType("东风大卡车6");
        lorryInfo.setCompany("得麦科技");
        lorryInfo.setWeight(new BigDecimal(5));
        lorryInfo.setCarryWeight(new BigDecimal(25));
        lorryInfo.setLength(new BigDecimal(10));
        lorryInfo.setWeight(new BigDecimal(3));
        String s = "2019-11-08 11:59:59";
        java.util.Date d1 = DateFormatUtils.parseDateTime(s);
        lorryInfo.setBuyTime(DateFormatUtils.formatDateTime(new Date(System.currentTimeMillis())));
        lorryInfo.setMileage(new BigDecimal(30));
        lorryInfo.setPlateNumber("京A123459");
        //lorryInfo.setIdType(1);
        //lorryInfo.setIdCard("370830199208251234");
        lorryInfoDao.save(lorryInfo);

    }
}
