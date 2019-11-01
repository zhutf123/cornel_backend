/**
 * Copyright (c) 2015 Qunar.com. All Rights Reserved.
 */
package com.demai.cornel.service;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.demai.cornel.constant.ConfigProperties;
import com.demai.cornel.dmEnum.IEmus;
import com.demai.cornel.util.json.JsonUtil;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Create By zhutf 19-11-1 上午12:36
 */
@Service
@Slf4j
public class SendMsgService {

    @Resource
    private ConfigProperties configProperties;

    private static int phone_max_size = 1000;

    public static enum SEND_MSG_CODE implements IEmus {
        PARAM_ERROR(-1, "参数错误"), SUCCESS(0, "请求成功"), MANY_PHONE(2, "同批次发送手机号超过1000");

        private int value;
        private String expr;

        private SEND_MSG_CODE(int value, String expr) {
            this.value = value;
            this.expr = expr;
        }

        @Override
        public int getValue() {
            return value;
        }

        @Override
        public String getExpr() {
            return expr;
        }

    }


    /***
     * 发送登录验证码
     * 
     * @param phone
     * @param paramValue
     */
    public Integer sendLoginValid(String phone, String paramValue) {
        Integer result = doSendMsg(Lists.newArrayList(phone), paramValue, configProperties.loginValidcodeId);
        log.info("send code to phone: {},{}", phone, result);
        return result;
    }

    /***
     * 阿里平台发送短信操作，phones size limit 1000
     * 
     * @param phones
     */
    public Integer doSendMsg(List<String> phones, String paramValue, String templateCode) {
        try {
            if (CollectionUtils.isEmpty(phones)) {
                if (log.isDebugEnabled()) {
                    log.debug("发送短信手机为空！");
                }
                return SEND_MSG_CODE.PARAM_ERROR.getValue();
            }

            if (phones.size() >= phone_max_size) {
                if (log.isDebugEnabled()) {
                    log.debug("返送短信手机号长度不得大于1000");
                }
                return SEND_MSG_CODE.MANY_PHONE.getValue();
            }

            if (StringUtils.isEmpty(templateCode)) {
                if (log.isDebugEnabled()) {
                    log.debug("短信模板不能为空！");
                }
                return SEND_MSG_CODE.PARAM_ERROR.getValue();
            }

            DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", configProperties.AliAccessKeyId,
                    configProperties.AliAccessSecret);
            IAcsClient client = new DefaultAcsClient(profile);
            CommonRequest request = new CommonRequest();
            request.setMethod(MethodType.POST);
            request.setDomain("dysmsapi.aliyuncs.com");
            request.setVersion("2017-05-25");
            request.setAction("SendSms");
            request.putQueryParameter("RegionId", "cn-hangzhou");
            request.putQueryParameter("PhoneNumbers", Joiner.on(",").join(phones));
            request.putQueryParameter("SignName", "xxxx");
            request.putQueryParameter("TemplateCode", templateCode);
            if (StringUtils.isNotBlank(paramValue)) {
                request.putQueryParameter("TemplateParam", paramValue);
            }
            CommonResponse response = client.getCommonResponse(request);
            if (log.isDebugEnabled()) {
                log.debug("短信发送返回：{}", JsonUtil.toJson(response));
            }
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return SEND_MSG_CODE.SUCCESS.getValue();
    }

}
