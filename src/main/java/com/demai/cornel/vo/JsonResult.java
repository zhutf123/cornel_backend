/**
 * Copyright (c) 2019 dm.com. All Rights Reserved.
 */
package com.demai.cornel.vo;

import com.demai.cornel.dmEnum.IEmus;
import com.demai.cornel.dmEnum.ResponseStatusEnum;
import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Create By zhutf 19-9-27 上午12:32
 */
@Data
public class JsonResult<DATATYPE> implements Serializable {
    private boolean ret;
    private Integer status;
    private String msg;
    private DATATYPE data;

    public JsonResult() {
        this.ret = true;
        this.msg = "";
    }


    public JsonResult(DATATYPE data) {
        this();
        this.status = ResponseStatusEnum.SUCCESS.getValue();
        this.msg = ResponseStatusEnum.SUCCESS.getExpr();
        this.data = data;
    }

    public JsonResult<DATATYPE> data(DATATYPE data) {
        this.data = data;
        this.status = ResponseStatusEnum.SUCCESS.getValue();
        this.msg = ResponseStatusEnum.SUCCESS.getExpr();
        return this;
    }

    public JsonResult(boolean ret, String msg) {
        this.msg = msg;
        this.ret = ret;
    }

    public JsonResult(boolean ret,Integer status, String msg) {
        this.msg = msg;
        this.status = status;
        this.ret = ret;
    }

    public JsonResult(boolean ret, Integer status) {
        this.status = status;
        this.ret = ret;
    }

    public void setErrInfo(String msg) {
        this.msg = msg;
        this.ret = false;
    }

    public JsonResult errorInfo(String msg) {
        this.msg = msg;
        this.ret = false;
        return this;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> rsMap = new HashMap<String, Object>();
        rsMap.put("ret", ret);
        rsMap.put("msg", msg);
        rsMap.put("data", data);
        return rsMap;
    }

    public Map<String, Object> toMapForTouch() {
        Map<String, Object> map = new HashMap<String, Object>();
        if (ret) {
            map.put("ret", 1);
            map.put("data", data);
        } else {
            map.put("ret", -1);
            map.put("msg", msg);
        }
        return map;
    }

    public static JsonResult error(String message) {
        return new JsonResult<Object>(false, message);
    }

    public static JsonResult success(Object object) {
        return new JsonResult<>(object);
    }

    public static JsonResult successStatus(Integer status) {
        return new JsonResult(true, status);
    }

    public static JsonResult successStatus(IEmus status) {
        return new JsonResult(true, status.getValue(),status.getExpr());
    }

    public boolean isRet() {
        return ret;
    }

    public void setRet(boolean ret) {
        this.ret = ret;
    }

}
