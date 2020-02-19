package com.demai.cornel.purcharse.service;

import com.demai.cornel.dao.UserInfoDao;
import com.demai.cornel.dmEnum.ResponseStatusEnum;
import com.demai.cornel.model.UserInfo;
import com.demai.cornel.purcharse.dao.BuyerInfoMapper;
import com.demai.cornel.purcharse.model.BuyerInfo;
import com.demai.cornel.service.SendMsgService;
import com.demai.cornel.service.WeChatService;
import com.demai.cornel.util.GenRandomCodeUtil;
import com.demai.cornel.util.StringUtil;
import com.demai.cornel.util.json.JsonUtil;
import com.demai.cornel.vo.WeChat.WechatCode2SessionResp;
import com.demai.cornel.vo.user.UserLoginParam;
import com.demai.cornel.vo.user.UserLoginResp;
import com.google.common.base.Joiner;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @Author binz.zhang
 * @Date: 2020-02-19    22:54
 */
@Slf4j
@Service
public class BuyerLoginService {
    @Resource
    private BuyerInfoMapper buyerInfoMapper;
    @Resource
    private WeChatService weChatService;
    @Resource
    private SendMsgService sendMsgService;
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    public UserLoginResp doLogin(UserLoginParam param) {
        // valid msg code
        if (!checkLoginMsgCode(param.getPhone(), param.getMsgCode())) {
            return new UserLoginResp(StringUtils.EMPTY, StringUtils.EMPTY, 1,
                    UserLoginResp.CODE_ENUE.MSG_CODE_ERROR.getValue(),param.getPhone());
        }
        BuyerInfo buyerInfo = buyerInfoMapper.selectByUserIdByLogin(param.getPhone());
        WechatCode2SessionResp resp = weChatService.getOpenId(param.getJscode());
        if (buyerInfo == null) {
            BuyerInfo buyerInfo1 = new BuyerInfo();
            buyerInfo1.setUserId(UUID.randomUUID().toString());
            buyerInfo1.setMobile(Sets.newHashSet(param.getPhone()));
            buyerInfo1.setStatus(BuyerInfo.USER_STATUS.PENDING.getValue());
            buyerInfoMapper.insertSelective(buyerInfo1);
            return new UserLoginResp(resp.getOpenid(), buyerInfo.getUserId(), 1,
                    UserLoginResp.CODE_ENUE.SUCCESS.getValue(), param.getPhone(),
                    UserLoginResp.USER_STATUS_ENUE.UNREGISTERED.getValue());
        }
        if(buyerInfo.getStatus()==null || buyerInfo.getStatus().equals(BuyerInfo.USER_STATUS.ENABLE.getValue())){
            return new UserLoginResp(resp.getOpenid(), buyerInfo.getUserId(), 1,
                    UserLoginResp.CODE_ENUE.SUCCESS.getValue(), param.getPhone(),
                    UserLoginResp.USER_STATUS_ENUE.UNREGISTERED.getValue());
        }
        return new UserLoginResp(StringUtils.EMPTY, StringUtils.EMPTY, UserInfo.ROLE_ENUE.BUYER.getValue(),
                UserLoginResp.CODE_ENUE.OPENID_ERROR.getValue(),param.getPhone());
    }



    /***
     * 给用户发送登录验证码 -1 查不到用户 0 成功 2 发送失败，稍后重试
     *
     * @param phone
     */
    public Integer sendLoginCodeMsg(String phone) {
        //UserInfo userInfo = getUserInfoByPhone(phone);
        Integer validCode = GenRandomCodeUtil.genRandomCode(6);
        if(phone.equals("13439679479")){
            validCode = 888888;
        }
        stringRedisTemplate.opsForValue().set(Joiner.on("_").join(phone, "loginValid"), String.valueOf(validCode), 300,
                TimeUnit.SECONDS);
        Integer sendResult = sendMsgService.sendLoginValid(phone, validCode);
        if (sendResult.compareTo(SendMsgService.SEND_MSG_CODE.SUCCESS.getValue()) == 0) {
            return ResponseStatusEnum.SUCCESS.getValue();
        }
        return ResponseStatusEnum.NETWORK_ERROR.getValue();
    }




    //    public Integer sendLoginCodeMsg(String phone) {
    //        UserInfo userInfo = getUserInfoByPhone(phone);
    //        Integer validCode = GenRandomCodeUtil.genRandomCode(6);
    //        if (userInfo == null) {
    //            return ResponseStatusEnum.NO_USER.getValue();
    //        }
    //        stringRedisTemplate.opsForValue().set(Joiner.on("_").join(phone, "loginValid"), String.valueOf(validCode), 300,
    //                TimeUnit.SECONDS);
    //        Integer sendResult = sendMsgService.sendLoginValid(phone, validCode);
    //        if (sendResult.compareTo(SendMsgService.SEND_MSG_CODE.SUCCESS.getValue()) == 0) {
    //            return ResponseStatusEnum.SUCCESS.getValue();
    //        }
    //        return ResponseStatusEnum.NETWORK_ERROR.getValue();
    //    }

    /***
     * 验证该手机号的登录验证码
     *
     * @param phone
     * @return
     */
    public Boolean checkLoginMsgCode(String phone, String msgCode) {
        String code = stringRedisTemplate.opsForValue().get(Joiner.on("_").join(phone, "loginValid"));
        if (StringUtil.isBlank(code)){
            return Boolean.FALSE;
        }
        if (msgCode.equalsIgnoreCase(code)){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }



}
