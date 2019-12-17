package com.demai.cornel.service.impl;

import com.demai.cornel.dao.DistOrderInfoDao;
import com.demai.cornel.model.DistOrderInfo;
import com.demai.cornel.model.DistTaskOrderReq;
import com.demai.cornel.service.ITaskService;
import com.google.common.base.Preconditions;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

/**
 * @Author binz.zhang
 * @Date: 2019-12-17    12:02
 */
@Service
public class TaskServiceImp implements ITaskService {
    @Resource
    private DistOrderInfoDao distOrderInfoDao;

    @Override
    public List<DistTaskOrderReq> getDistTaskList(String userId, Integer curId, Integer pgsize) {
        Preconditions.checkNotNull(userId);
        List<DistTaskOrderReq> distTaskOrderReqs = distOrderInfoDao.getDistOrderListByUserID(userId, curId, pgsize);
        if (distTaskOrderReqs == null) {
            return Collections.emptyList();
        }
        distTaskOrderReqs.stream().forEach(x -> {
            if (x.getUnitDistance() == null) x.setUnitDistance("km");
            if (x.getUnitPrice() == null) x.setUnitPrice("元");
            if (x.getUnitWeight() == null) x.setUnitWeight("吨");
            x.setIncome(x.getPrice().multiply(x.getWeight()));
        });
        return distTaskOrderReqs;
    }
}
