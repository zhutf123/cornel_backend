package com.demai.cornel.demeManager.service;

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

    public List<AdminGetFreightViewResp> adminGetFreightView(Integer offset, Integer pgSize) {

        List<AdminGetFreightViewResp> freightViewResps = freightInfoMapper
                .adminnGetFreightView(Optional.ofNullable(offset).orElse(0), Optional.ofNullable(pgSize).orElse(0));
        if (freightViewResps == null) {
            return Collections.emptyList();
        }
        freightViewResps.stream().forEach(x -> {
            if (x.getAverPrice() == null) {
                x.setAverPrice(new BigDecimal(0));
            }
            if (x.getMinPrice() == null) {
                x.setMinPrice(new BigDecimal(0));
            }
        });
        return freightViewResps;
    }
}
