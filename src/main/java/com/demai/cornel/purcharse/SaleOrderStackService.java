package com.demai.cornel.purcharse;

import com.demai.cornel.dao.OrderInfoDao;
import com.demai.cornel.dao.TaskInfoDao;
import com.demai.cornel.demeManager.service.SaleConvertTaskService;
import com.demai.cornel.model.OrderInfo;
import com.demai.cornel.model.TaskInfo;
import com.demai.cornel.purcharse.dao.*;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author binz.zhang
 * @Date: 2020-04-02    16:04
 */
@Slf4j
@Service
public class SaleOrderStackService {
    @Resource private SaleOrderMapper saleOrderMapper;
    @Resource private StackOutInfoMapper stackOutInfoMapper;
    @Resource private StoreInfoMapper storeInfoMapper;
    @Resource private LocationInfoMapper locationInfoMapper;
    @Resource private FreightInfoMapper freightInfoMapper;
    @Resource private SaleConvertTaskService saleConvertTaskService;
    @Resource private OrderInfoDao orderInfoDao;
    @Resource private TaskInfoDao taskInfoDao;

    private static final String TIME_TORMAT = "yyyy-MM-dd";


//    public boolean checkSalerOrderStatus(String driverOrderId){
//        if(Strings.isNullOrEmpty(driverOrderId)){
//            return true;
//        }
//        OrderInfo orderInfo = orderInfoDao.getOrderInfoByOrderId(driverOrderId);
//        if(orderInfo==null || !orderInfo.getTaskId().startsWith(TaskInfo.TASK_PREX_ENUE.SALE.getValue())){
//            return false;
//        }
//
//        TaskInfo taskInfo = taskInfoDao.selectTaskInfoByTaskId(orderInfo.getTaskId());
//        if(taskInfo==null || taskInfo.getStatus(TaskInfo.STATUS_ENUE.TASK_OVER.getValue()))
//    }
}
