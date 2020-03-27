package com.demai.cornel.demeManager.service;

import com.demai.cornel.demeManager.dao.AdminUserMapper;
import com.demai.cornel.demeManager.model.AdminUser;
import com.demai.cornel.demeManager.vo.AdminLoginResp;
import com.demai.cornel.dmEnum.ResponseStatusEnum;
import com.demai.cornel.service.SendMsgService;
import com.demai.cornel.util.Base64Utils;
import com.demai.cornel.util.GenRandomCodeUtil;
import com.demai.cornel.util.StringUtil;
import com.demai.cornel.vo.user.UserLoginParam;
import com.demai.cornel.vo.user.UserLoginResp;
import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @Author binz.zhang
 * @Date: 2020-03-08    12:09
 */
@Service @Slf4j public class AdminUserLoginService {
    @Resource private AdminUserMapper adminUserMapper;
    @Resource private SendMsgService sendMsgService;
    @Resource private StringRedisTemplate stringRedisTemplate;

    private static final String TOKEN_KEY_FORMAT = "admin_token_%s";
    private static final String ADMIN_CK_FORMAT = "u=%s&tk=%s";

    public AdminLoginResp doLogin(UserLoginParam param,HttpServletResponse httpServletResponse) {
        // valid msg code
        AdminLoginResp adminLoginResp = new AdminLoginResp();
        adminLoginResp.setMobile(param.getPhone());
        if (!checkLoginMsgCode(param.getPhone(), param.getMsgCode())) {
            adminLoginResp.setCode(UserLoginResp.CODE_ENUE.MSG_CODE_ERROR.getValue());
            return adminLoginResp;
        }
        AdminUser adminUser = adminUserMapper.selectUserByTel(param.getPhone());
        if (adminUser == null) {
            adminLoginResp.setCode(UserLoginResp.CODE_ENUE.NO_USER.getValue());
            return adminLoginResp;
        }
        String token = UUID.randomUUID().toString().replaceAll("-", "");
        stringRedisTemplate.opsForValue().set(String.format(TOKEN_KEY_FORMAT, adminUser.getUserId()), token,
                Duration.ofSeconds(24 * 60 * 60, 0));
        httpServletResponse.addCookie(clearPathCookie());
        httpServletResponse.addCookie(buildCkey(adminUser.getUserId(),token));
        BeanUtils.copyProperties(adminUser, adminLoginResp);
        adminLoginResp.setCode(0);
        return adminLoginResp;
    }

    public Cookie buildCkey(String userID, String token) {

        String ckey = String.format(ADMIN_CK_FORMAT, userID, token);
        String encodeCkey = new String(Base64Utils.encode(ckey.getBytes()));
        Cookie cookie = new Cookie("ckey", encodeCkey);
        cookie.setPath("/");
        cookie.setMaxAge(24 * 60 * 60);//24 小时过期
        return cookie;
    }
    public Cookie clearPathCookie() {
        Cookie cookie = new Cookie("ckey", null);
        cookie.setMaxAge(0);
        return cookie;
    }

    public boolean checkAdminToken(String token, String userId) {
//        if (Strings.isNullOrEmpty(token) || Strings.isNullOrEmpty(userId)) {
//            return false;
//        }
//        String tokenRedis = stringRedisTemplate.opsForValue().get(String.format(TOKEN_KEY_FORMAT, userId));
//        if (Strings.isNullOrEmpty(tokenRedis) || !tokenRedis.equals(token)) {
//            log.debug("cur user {}  token check fail carry token is {} ", userId, token);
//            return false;
//        }
        return true;
    }

    public boolean resetTokenExprieTime(String userId, String token) {
        if (Strings.isNullOrEmpty(token) || Strings.isNullOrEmpty(userId)) {
            return false;
        }
        stringRedisTemplate.opsForValue()
                .set(String.format(TOKEN_KEY_FORMAT, userId), token, Duration.ofSeconds(24 * 60 * 60, 0));
        return true;
    }

    /***
     * 给用户发送登录验证码 -1 查不到用户 0 成功 2 发送失败，稍后重试
     *
     * @param phone
     */
    public Integer sendLoginCodeMsg(String phone) {

        AdminUser adminUser = adminUserMapper.selectUserByTel(phone);
        if (adminUser == null) {
            return ResponseStatusEnum.NO_USER.getValue();
        }
        Integer validCode = GenRandomCodeUtil.genRandomCode(6);
        stringRedisTemplate.opsForValue()
                .set(Joiner.on("_").join(phone, "loginValid"), String.valueOf(validCode), 300, TimeUnit.SECONDS);
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
        if (StringUtil.isBlank(code)) {
            return Boolean.FALSE;
        }
        if (msgCode.equalsIgnoreCase(code)) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

}
