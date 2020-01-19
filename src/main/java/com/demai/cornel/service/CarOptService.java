package com.demai.cornel.service;

import com.demai.cornel.dao.LorryInfoDao;
import com.demai.cornel.model.LorryInfo;
import com.google.common.base.Strings;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author binz.zhang
 * @Date: 2020-01-19    16:13
 */
@Service public class CarOptService {

    @Resource LorryInfoDao lorryInfoDao;

    /**
     * 添加车辆时校验参数 车牌号 车架号 发动机号 行驶证类型 车辆类型
     * @param lorryInfo
     * @return
     */
    public boolean checkAddParam(LorryInfo lorryInfo) {
        if (lorryInfo == null) {
            return false;
        }
        if (Strings.isNullOrEmpty(lorryInfo.getPlateNumber()) || Strings.isNullOrEmpty(lorryInfo.getEngineNo())
                || Strings.isNullOrEmpty(lorryInfo.getFrameNumber()) || Strings.isNullOrEmpty(lorryInfo.getLorryType())
                || Strings.isNullOrEmpty(lorryInfo.getCarLiceOwner())) {
            return false;
        }
        return true;
    }

    /**
     * 根据车架号 车牌号 以及发动看
     * @param plateNumber
     * @param frameNumber
     * @param engineNo
     * @return
     */
    public boolean getCarExistByParam(String plateNumber, String frameNumber, String engineNo) {
        return lorryInfoDao.getLorryInfoByFlaNoEngNoFraNo(plateNumber, frameNumber, engineNo) == null ? false : true;
    }

}
