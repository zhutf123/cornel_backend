package com.demai.cornel.demeManager.service;

import com.demai.cornel.dao.DryTowerDao;
import com.demai.cornel.demeManager.vo.AdminGetFreightViewResp;
import com.demai.cornel.purcharse.dao.FreightInfoMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @Author binz.zhang
 * @Date: 2020-04-03    16:27
 */
@Service @Slf4j public class AdminFreightService {
    @Resource private FreightInfoMapper freightInfoMapper;
    @Resource private DryTowerDao dryTowerDao;

    public List<AdminGetFreightViewResp> adminGetFreightView(Integer offset, Integer pgSize) {

        List<AdminGetFreightViewResp> freightViewResps = dryTowerDao.adminGetDryTower( pgSize,offset);
        if (freightViewResps == null) {
            return Collections.EMPTY_LIST;
        }

        if (freightViewResps == null) {
            return Collections.emptyList();
        }
        freightViewResps.stream().forEach(x -> {
            AdminGetFreightViewResp adminGetFreightViewResp = freightInfoMapper.adminnGetOptFreightView(x.getLocationId());
            if (adminGetFreightViewResp==null || adminGetFreightViewResp.getAverPrice() == null) {
                x.setAverPrice(new BigDecimal(0));
            }else {
                x.setAverPrice(adminGetFreightViewResp.getAverPrice());

            }
            if (adminGetFreightViewResp==null || adminGetFreightViewResp.getMinPrice() == null) {
                x.setMinPrice(new BigDecimal(0));
            }else {
                x.setMinPrice(adminGetFreightViewResp.getMinPrice());
            }
        });
        return freightViewResps;
    }
}
