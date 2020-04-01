package com.demai.cornel.demeManager.service;

import com.demai.cornel.purcharse.dao.CargoInfoMapper;
import com.demai.cornel.purcharse.model.CargoInfo;
import com.demai.cornel.purcharse.model.SaleOrder;
import com.demai.cornel.purcharse.model.StackOutInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.UUID;

/**
 * @Author binz.zhang
 * @Date: 2020-04-01    14:56
 */
@Service @Slf4j public class SaleConvertTaskService {
    @Resource private CargoInfoMapper cargoInfoMapper;

    public void buildTask(StackOutInfo stackOutInfo, SaleOrder saleOrder) {
        CargoInfo cargoInfo = new CargoInfo();
        cargoInfo.setCargoId(UUID.randomUUID().toString());
        cargoInfo.setUnitWeight(stackOutInfo.getUnitWeight());
        cargoInfo.setPrice(saleOrder.getCommodityPrice());
        cargoInfo.setCommodityId(stackOutInfo.getCommodityId());
        cargoInfo.setDealTime(saleOrder.getOrderTime());
        cargoInfo.setStoreId(stackOutInfo.getStoreId());
        cargoInfoMapper.insertSelective(cargoInfo);
        stackOutInfo.setCargoId(cargoInfo.getCargoId());

    }
}
