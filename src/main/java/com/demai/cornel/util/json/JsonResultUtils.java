package com.demai.cornel.util.json;

/**
 * Author : mingxing.shao
 * Date : 16-4-12
 * email : mingxing.shao@qunar.com
 */
public class JsonResultUtils {

    public static JsonResult<?> success() {
        return success("");
    }

    public static <T> JsonResult<T> success(T data) {
        return new JsonResult<>(JsonResult.SUCCESS, JsonResult.SUCCESS_CODE, "", data);
    }

    public static <T> JsonResult<?> success(int code, String msg) {
        return new JsonResult<>(JsonResult.SUCCESS, code, msg, "");
    }

    public static JsonResult<?> fail(int errcode, String errmsg) {
        return new JsonResult<>(JsonResult.FAIL, errcode, errmsg, "");
    }

    public static JsonResult<?> fail(int errcode) {
        return new JsonResult<>(JsonResult.FAIL, errcode,"","");
    }

    public static <T> JsonResult<T> fail(int errcode, T errMsg) {
        return new JsonResult<>(JsonResult.FAIL, errcode, "", errMsg);
    }

    public static <T> JsonResult<T> fail(int errcode, String errmsg, T errMsg) {
        return new JsonResult<>(JsonResult.FAIL, errcode, errmsg, errMsg);
    }

    public static JsonResult<?> response(boolean flag, String msg){
        if(flag) {
            return JsonResultUtils.success();
        }else {
            return JsonResultUtils.fail(1, msg);
        }
    }

//    public static JsonResult<?> response(String response) {
//        try {
//            if (response == null) {
//                return new JsonResult<>(JsonResult.FAIL, 1, "parse response fail", "");
//            }
//            JsonResult result = JsonUtil.string2Obj(response, JsonResult.class);
//            return result;
//        } catch  (Exception e) {
//            return new JsonResult<>(JsonResult.FAIL, 1, "parse response fail", "");
//        }
//    }
}
