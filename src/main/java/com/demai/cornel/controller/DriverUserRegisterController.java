package com.demai.cornel.controller;

import com.demai.cornel.annotation.AccessControl;
import com.demai.cornel.dmEnum.ResponseStatusEnum;
import com.demai.cornel.service.DriverRegisterService;
import com.demai.cornel.service.UserLoginService;
import com.demai.cornel.service.UserService;
import com.demai.cornel.util.JacksonUtils;
import com.demai.cornel.vo.JsonResult;
import com.demai.cornel.vo.delivery.DriverCpllUserInfoReq;
import com.demai.cornel.vo.user.UserLoginParam;
import com.demai.cornel.vo.user.UserLoginResp;
import com.demai.cornel.vo.user.UserLoginSendMsgParam;
import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author binz.zhang
 * @Date: 2020-01-16    15:57
 */
@Controller @RequestMapping("/driver-reg") @Slf4j public class DriverUserRegisterController {
    @Resource private UserLoginService userLoginService;
    @Resource private DriverRegisterService driverRegisterService;

    /***
     * 给用户手机号 发送短信验证码 需要补充逻辑 在n分钟内，发送x条的限制
     * 60s 内最大发3次
     * @param phone 手机号
     * @return
     */
    @RequestMapping(value = "/sendCode.json", method = RequestMethod.POST) @ResponseBody @AccessControl(value = "60_3") public JsonResult userLoginSendCode(
            @RequestBody UserLoginSendMsgParam phone) {
        try {
            log.debug("send code access [{}]", JacksonUtils.obj2String(phone));
            return JsonResult.successStatus(userLoginService.sendLoginCodeMsgV2(phone.getPhone()));
        } catch (Exception e) {
            log.error("用户发送短信异常！", e);
            return JsonResult.successStatus(ResponseStatusEnum.NETWORK_ERROR);
        }
    }

    /**
     * @return
     */
    @RequestMapping(value = "/login.json", method = RequestMethod.POST, produces = "application/json; charset=utf-8") @ResponseBody public JsonResult doUserLogin(
            @RequestBody UserLoginParam param, HttpServletResponse response) {
        try {
            Preconditions.checkNotNull(param);
            Preconditions.checkNotNull(param.getJscode());
            Preconditions.checkNotNull(param.getPhone());
            Preconditions.checkNotNull(param.getMsgCode());
            UserLoginResp login = userLoginService.doLoginV3(param);
            if (login.getCode().compareTo(UserLoginResp.CODE_ENUE.SUCCESS.getValue()) == 0) {
                return JsonResult.success(login);
            } else {
                return JsonResult.successStatus(UserLoginResp.CODE_ENUE.getByValue(login.getCode()));
            }
        } catch (Exception e) {
            log.error("用户登录异常！", e);
        }
        return JsonResult.successStatus(ResponseStatusEnum.NETWORK_ERROR);
    }
    @RequestMapping(value = "/car-type.json", method = RequestMethod.POST, produces = "application/json; charset=utf-8") @ResponseBody public JsonResult getCarType(
            @RequestBody String param, HttpServletResponse response) {
        try {
            return JsonResult.success(driverRegisterService.getCarType());
        } catch (Exception e) {
            log.error("获取车辆类型失败！", e);
        }
        return JsonResult.successStatus(ResponseStatusEnum.NETWORK_ERROR);
    }
    @RequestMapping(value = "/car-lice.json", method = RequestMethod.POST, produces = "application/json; charset=utf-8") @ResponseBody public JsonResult getCarLiceType(
            @RequestBody String param, HttpServletResponse response) {
        try {
            return JsonResult.success(driverRegisterService.getCarLiceType());
        } catch (Exception e) {
            log.error("获取车辆行驶证类型失败！", e);
        }
        return JsonResult.successStatus(ResponseStatusEnum.NETWORK_ERROR);
    }
    @RequestMapping(value = "/comp-userinfo.json", method = RequestMethod.POST, produces = "application/json; charset=utf-8") @ResponseBody public JsonResult completeUserInfo(
            @RequestBody DriverCpllUserInfoReq param, HttpServletResponse response) {
        try {
            return JsonResult.success(driverRegisterService.getCarType());
        } catch (Exception e) {
            log.error("司机完善个人信息失败！", e);
        }
        return JsonResult.successStatus(ResponseStatusEnum.NETWORK_ERROR);
    }

}
