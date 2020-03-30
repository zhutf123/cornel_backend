package com.demai.cornel.demeManager.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.demai.cornel.annotation.AccessControl;
import com.demai.cornel.demeManager.service.AdminCornService;
import com.demai.cornel.demeManager.service.AdminUserLoginService;
import com.demai.cornel.demeManager.vo.*;
import com.demai.cornel.dmEnum.ResponseStatusEnum;
import com.demai.cornel.util.Base64Utils;
import com.demai.cornel.util.CookieAuthUtils;
import com.demai.cornel.util.JacksonUtils;
import com.demai.cornel.vo.JsonResult;
import com.demai.cornel.vo.user.UserLoginParam;
import com.demai.cornel.vo.user.UserLoginResp;
import com.demai.cornel.vo.user.UserLoginSendMsgParam;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.lang.ref.PhantomReference;
import java.util.UUID;

/**
 * @Author binz.zhang
 * @Date: 2020-03-08    12:06
 */
@Controller @RequestMapping("/admin") @Slf4j public class UserController {

    @Resource private AdminUserLoginService adminUserLoginService;
    @Resource private AdminCornService adminCornService;

    /***
     * 给用户手机号 发送短信验证码 需要补充逻辑 在n分钟内，发送x条的限制
     * 60s 内最大发3次
     * @param phone 手机号
     * @return
     */
    @RequestMapping(value = "/sendCode.json", method = RequestMethod.POST) @ResponseBody @AccessControl(value = "60_3") @CrossOrigin public JsonResult userLoginSendCode(
            @RequestBody UserLoginSendMsgParam phone) {
        try {
            log.debug("send code access [{}]", JacksonUtils.obj2String(phone));
            return JsonResult.successStatus(adminUserLoginService.sendLoginCodeMsg(phone.getPhone()));
        } catch (Exception e) {
            log.error("用户发送短信异常！", e);
            return JsonResult.successStatus(ResponseStatusEnum.NETWORK_ERROR);
        }
    }

    /**
     * @return
     */
    @RequestMapping(value = "/login.json", method = RequestMethod.POST, produces = "application/json; charset=utf-8") @CrossOrigin @ResponseBody public JsonResult doUserLogin(
            @RequestBody UserLoginParam param, HttpServletResponse response) {
        try {
            Preconditions.checkNotNull(param);
            Preconditions.checkNotNull(param.getPhone());
            Preconditions.checkNotNull(param.getMsgCode());
            AdminLoginResp login = adminUserLoginService.doLogin(param,response);
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

    @CrossOrigin @RequestMapping(value = "/get-quote-list.json", method = RequestMethod.POST, produces = "application/json; charset=utf-8") @ResponseBody public JsonResult getQuoteList(
            @RequestBody GetQuoteListReq param, HttpServletResponse response) {
        Preconditions.checkNotNull(param);
        return JsonResult.success(adminCornService.getQuoteList(param,response));
    }

    @CrossOrigin @RequestMapping(value = "/get-quote-detail.json", method = RequestMethod.POST, produces = "application/json; charset=utf-8") @ResponseBody public JsonResult getQuoteDetail(
            @RequestBody String param) {
        Preconditions.checkNotNull(param);
        JSONObject receivedParam = JSON.parseObject(param);
        String quoteId = (String) receivedParam.get("quoteId");
        return JsonResult.success(adminCornService.getQuteDetail(quoteId));
    }

    @CrossOrigin @RequestMapping(value = "/review-quote.json", method = RequestMethod.POST, produces = "application/json; charset=utf-8") @ResponseBody public JsonResult reviewQuote(
            @RequestBody ReviewQuoteReq reviewQuoteReq) {
        Preconditions.checkNotNull(reviewQuoteReq);
        return JsonResult.success(adminCornService.adminReviewQuote(reviewQuoteReq));
    }

    @CrossOrigin @RequestMapping(value = "/get-tower-list.json", method = RequestMethod.POST, produces = "application/json; charset=utf-8") @ResponseBody public JsonResult getTowerList(
            @RequestBody AdminGetTowerReq reviewQuoteReq) {
        Preconditions.checkNotNull(reviewQuoteReq);
        return JsonResult.success(adminCornService.getTowerList(reviewQuoteReq));
    }

    @CrossOrigin @RequestMapping(value = "/edit-quote.json", method = RequestMethod.POST, produces = "application/json; charset=utf-8") @ResponseBody public JsonResult editQuote(
            @RequestBody AdminEditQuoteReq adminEditQuoteReq) {
        Preconditions.checkNotNull(adminEditQuoteReq);
        return JsonResult.success(adminCornService.editQuote(adminEditQuoteReq));
    }

    @CrossOrigin @RequestMapping(value = "/get-system-quote.json", method = RequestMethod.POST, produces = "application/json; charset=utf-8") @ResponseBody public JsonResult getSystemQuote() {
        return JsonResult.success(adminCornService.getSyQuLis());
    }

    @CrossOrigin @RequestMapping(value = "/get-tower-quote.json", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody public JsonResult getTowerQuote(@RequestBody AdGetTowQuLiReq adGetTowQuLiReq) {
        Preconditions.checkNotNull(adGetTowQuLiReq);
        return JsonResult.success(adminCornService.getTowerQuoteList(adGetTowQuLiReq.getUserId()));
    }
    @CrossOrigin @RequestMapping(value = "/get-err-opt.json", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody public JsonResult getErrOption() {
        return JsonResult.success(adminCornService.getReviewErrOpt());
    }

}
