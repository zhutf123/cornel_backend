package com.demai.cornel.purcharse.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.demai.cornel.annotation.AccessControl;
import com.demai.cornel.dmEnum.ResponseStatusEnum;
import com.demai.cornel.purcharse.model.LocationInfo;
import com.demai.cornel.purcharse.service.BuyerLoginService;
import com.demai.cornel.purcharse.service.PurcharseOtherService;
import com.demai.cornel.purcharse.vo.req.AddLocationReq;
import com.demai.cornel.purcharse.vo.req.BuyerCplCompanyReq;
import com.demai.cornel.purcharse.vo.req.BuyerCplUserInfoReq;
import com.demai.cornel.purcharse.vo.req.GetSystemOfferReq;
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
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author binz.zhang
 * @Date: 2020-02-14    20:33
 */
@Controller @RequestMapping("/purchase") @Slf4j public class PurchaseOtherController {

    @Resource private PurcharseOtherService purcharseOtherService;


    @Resource
    private BuyerLoginService buyerLoginService;
    @Resource
    private UserService userService;

    /***
     * 给用户手机号 发送短信验证码 需要补充逻辑 在n分钟内，发送x条的限制
     * 60s 内最大发3次
     * @param phone 手机号
     * @return
     */
    @RequestMapping(value = "/sendCode.json", method = RequestMethod.POST)
    @ResponseBody
    @AccessControl(value = "60_3")
    public JsonResult userLoginSendCode(@RequestBody UserLoginSendMsgParam phone) {
        try {
            log.debug("send code access [{}]", JacksonUtils.obj2String(phone));
            return JsonResult.successStatus(buyerLoginService.sendLoginCodeMsg(phone.getPhone()));
        } catch (Exception e) {
            log.error("用户发送短信异常！", e);
            return JsonResult.successStatus(ResponseStatusEnum.NETWORK_ERROR);
        }
    }

    /**
     *
     * @return
     */
    @RequestMapping(value = "/login.json", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public JsonResult doUserLogin(@RequestBody UserLoginParam param, HttpServletResponse response) {
        try {
            Preconditions.checkNotNull(param);
            Preconditions.checkNotNull(param.getJscode());
            Preconditions.checkNotNull(param.getPhone());
            Preconditions.checkNotNull(param.getMsgCode());
            UserLoginResp login = buyerLoginService.doLogin(param);
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

    @RequestMapping(value = "/comp-userinfo.json", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public JsonResult completeUserInfo(
            @RequestBody BuyerCplUserInfoReq param, HttpServletResponse response) {
        try {
            return JsonResult.success(buyerLoginService.driverCompleteUserInfo(param));
        } catch (Exception e) {
            log.error("m买家完善个人信息！", e);
        }
        return JsonResult.successStatus(ResponseStatusEnum.NETWORK_ERROR);
    }
    @RequestMapping(value = "/comp-company.json", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody public JsonResult completeCompanyInfo(
            @RequestBody BuyerCplCompanyReq param, HttpServletResponse response) {
        try {
            return JsonResult.success(buyerLoginService.buyerCplCompany(param));
        } catch (Exception e) {
            log.error("m买家完善公司信息信息！", e);
        }
        return JsonResult.successStatus(ResponseStatusEnum.NETWORK_ERROR);
    }
    @RequestMapping(value = "/get-userinfo.json", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody public JsonResult getUserInfo() {
        try {
            return JsonResult.success(buyerLoginService.getUserCompleteInfo());
        } catch (Exception e) {
            log.error("m买家完善公司信息信息！", e);
        }
        return JsonResult.successStatus(ResponseStatusEnum.NETWORK_ERROR);
    }
    /**
     * 增加收货地址
     *
     * @return
     */
    @RequestMapping(value = "/add-location.json", method = RequestMethod.POST) @ResponseBody public JsonResult addLocation(
            @RequestBody AddLocationReq locationInfo) {
        return purcharseOtherService.addLocation(locationInfo);
    }

    /**
     * 获取收货地址list
     * @return
     */
    @RequestMapping(value = "/get-location.json", method = RequestMethod.POST) @ResponseBody public JsonResult getLocation() {
        return purcharseOtherService.getLocationList();
    }
    /**
     * 获取收货地址list
     * @return
     */
    @RequestMapping(value = "/get-location-detail.json", method = RequestMethod.POST) @ResponseBody
    public JsonResult getLocationDetail(@RequestBody String param) {
        Preconditions.checkNotNull(param);
        JSONObject receivedParam = JSON.parseObject(param);
        String locationId = (String) receivedParam.get("locationId");
        return JsonResult.success(purcharseOtherService.getLocationDetail(locationId));
    }

    /**
     * 修改地址
     * @param locationInfo
     * @return
     */
    @RequestMapping(value = "/edit-location.json", method = RequestMethod.POST) @ResponseBody public JsonResult editLocation(
            @RequestBody AddLocationReq locationInfo) {
        return purcharseOtherService.editLocation(locationInfo);
    }

    @RequestMapping(value = "/commodity-list.json", method = RequestMethod.POST)
    @ResponseBody public JsonResult getCommotidyList() {
        return JsonResult.success(purcharseOtherService.getCommodityList());
    }

}
