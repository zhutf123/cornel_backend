package com.demai.cornel.service;

import com.demai.cornel.dao.AgreementInfoMapper;
import com.demai.cornel.model.AgreementInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author binz.zhang
 * @Date: 2020-01-08    17:17
 */

@Slf4j
@Service
public class AgreementService {

    @Resource private AgreementInfoMapper agreementInfoMapper;

    public AgreementInfo getAgreementByAdapt(String adapt){
        return agreementInfoMapper.selectByAdapt(adapt);


    }


}
