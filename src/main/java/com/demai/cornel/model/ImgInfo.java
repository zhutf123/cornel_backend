package com.demai.cornel.model;

import com.demai.cornel.dmEnum.IEmus;
import lombok.Data;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.UUID;

/**
 * @Author binz.zhang
 * @Date: 2020-01-19    12:07
 */
@Data public class ImgInfo {
    private Long id;
    private String imgId;
    private String imgDesc;
    private Integer status;
    private String bindId;
    private Integer bindType;
    private Timestamp createTime;
    private String url;

    public static enum STATUS implements IEmus {
        DELETE(0, "已删除"), EXIST(1, "可用");

        private int value;
        private String expr;

        private STATUS(int value, String expr) {
            this.value = value;
            this.expr = expr;
        }

        @Override public int getValue() {
            return value;
        }

        @Override public String getExpr() {
            return expr;
        }
    }

    public static enum BINDTYPESTATUS implements IEmus {
        BIND_USER(0, "绑定的人"), BIND_CAR(1, "绑定的车");

        private int value;
        private String expr;

        private BINDTYPESTATUS(int value, String expr) {
            this.value = value;
            this.expr = expr;
        }

        @Override public int getValue() {
            return value;
        }

        @Override public String getExpr() {
            return expr;
        }
    }

    public  enum IMGDESC {
        ID_CARD("idcard", "身份证号"), USER_CARD("userCard", "手持身份证号"), CAR_LICE("carLice", "行驶证照片"), DRIVER_LICE(
                "driverLice", "驾驶证照片"), CAR_IMG("carImg", "车辆45度照片");

        private String key;
        private String expr;

        private IMGDESC(String key, String expr) {
            this.key = key;
            this.expr = expr;
        }

        public String getKey() {
            return key;
        }

        public String getExpr() {
            return expr;
        }

        public static IMGDESC keyOf(String key) {
            return Arrays.stream(IMGDESC.values()).filter(X -> X.getKey() == key).findAny()
                    .orElse(null);
        }
    }

//    public static void main(String[] args) {
//        String u= "idcard";
//        System.out.println(ImgInfo.IMGDESC.keyOf(u).getExpr());
//        ImgInfo imgInfo = new ImgInfo();
//        imgInfo.setImgId(UUID.randomUUID().toString());
//        imgInfo.setImgDesc(ImgInfo.IMGDESC.keyOf(u).getExpr());
//        System.out.println(ImgInfo.IMGDESC.keyOf(u).getExpr());
//
//    }
}
