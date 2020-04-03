package com.demai.cornel.demeManager.service;

import com.demai.cornel.demeManager.vo.*;
import com.demai.cornel.purcharse.dao.*;
import com.demai.cornel.purcharse.model.*;
import com.demai.cornel.purcharse.service.OutStackService;
import com.demai.cornel.util.JacksonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @Author binz.zhang
 * @Date: 2020-04-03    10:28
 */
@Service
@Slf4j
/*管理员获取审核通过的订单的详情*/
public class AdminApprovedService {
    @Resource private StackOutInfoMapper stackOutInfoMapper;
    @Resource private StoreInfoMapper storeInfoMapper;
    @Resource private LocationInfoMapper locationInfoMapper;
    @Resource private FreightInfoMapper freightInfoMapper;
    @Resource private SaleStackOutService saleStackOutService;
    @Resource private OutStackService outStackService;
    @Resource private SaleOrderMapper saleOrderMapper;

    public List<AdminAppSaleList> adminGetSaleList(Integer status, Integer offset, Integer pgSize) {
        List<AdminUnRevSaleList> saleOrder = saleOrderMapper
                .AdminGetUnRevSaleOrderList(status, Optional.ofNullable(pgSize).orElse(10),
                        Optional.ofNullable(offset).orElse(0));
        if (saleOrder == null) {
            log.warn("SaleUnderReviewService--adminGetSaleList fail due to order invalid");
            return Collections.EMPTY_LIST;
        }
        return saleOrder;

    }
}
