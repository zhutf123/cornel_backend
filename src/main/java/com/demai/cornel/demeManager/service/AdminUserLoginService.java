package com.demai.cornel.demeManager.service;

import com.demai.cornel.demeManager.dao.AdminUserMapper;
import com.demai.cornel.demeManager.model.AdminUser;
import com.demai.cornel.demeManager.vo.AdminLoginResp;
import com.demai.cornel.dmEnum.ResponseStatusEnum;
import com.demai.cornel.service.SendMsgService;
import com.demai.cornel.util.GenRandomCodeUtil;
import com.demai.cornel.util.StringUtil;
import com.demai.cornel.vo.user.UserLoginParam;
import com.demai.cornel.vo.user.UserLoginResp;
import com.google.common.base.Joiner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @Author binz.zhang
 * @Date: 2020-03-08    12:09
 */
@Service
@Slf4j
public class AdminUserLoginService {
    @Resource
    private AdminUserMapper adminUserMapper;
    @Resource
    private SendMsgService sendMsgService;
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    public AdminLoginResp doLogin(UserLoginParam param) {
        // valid msg code
        AdminLoginResp adminLoginResp = new AdminLoginResp();
        adminLoginResp.setMobile(param.getPhone());
        if (!checkLoginMsgCode(param.getPhone(), param.getMsgCode())) {
            adminLoginResp.setCode(UserLoginResp.CODE_ENUE.MSG_CODE_ERROR.getValue());
             return adminLoginResp;
        }
        AdminUser adminUser = adminUserMapper.selectUserByTel(param.getPhone());
        if(adminUser==null){
            adminLoginResp.setCode(UserLoginResp.CODE_ENUE.NO_USER.getValue());
            return adminLoginResp;
        }
        BeanUtils.copyProperties(adminUser,adminLoginResp);
        return adminLoginResp;
    }



    /***
     * 给用户发送登录验证码 -1 查不到用户 0 成功 2 发送失败，稍后重试
     *
     * @param phone
     */
    public Integer sendLoginCodeMsg(String phone) {

        AdminUser adminUser = adminUserMapper.selectUserByTel(phone);
        if(adminUser==null){
            return ResponseStatusEnum.NO_USER.getValue();
        }
        Integer validCode = GenRandomCodeUtil.genRandomCode(6);
        stringRedisTemplate.opsForValue().set(Joiner.on("_").join(phone, "loginValid"), String.valueOf(validCode), 300,
                TimeUnit.SECONDS);
        Integer sendResult = sendMsgService.sendLoginValid(phone, validCode);
        if (sendResult.compareTo(SendMsgService.SEND_MSG_CODE.SUCCESS.getValue()) == 0) {
            return ResponseStatusEnum.SUCCESS.getValue();
        }
        return ResponseStatusEnum.NETWORK_ERROR.getValue();
    }



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
