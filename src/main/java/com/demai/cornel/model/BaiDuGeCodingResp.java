package com.demai.cornel.model;

import lombok.Data;

/**
 * @Author binz.zhang
 * @Date: 2019-12-30    14:25
 * 百度地图 aip 根据地理位置获取经纬度的返回model
 */
@Data
public class BaiDuGeCodingResp {

    /**
     * status : 0
     * result : {"location":{"lng":116.30789936413898,"lat":40.05703792688976},"precise":0,"confidence":75,"comprehension":0,"level":"商务大厦"}
     */

    private int status;
    private ResultBean result;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * location : {"lng":116.30789936413898,"lat":40.05703792688976}
         * precise : 0
         * confidence : 75
         * comprehension : 0
         * level : 商务大厦
         */

        private LocationBean location;
        private int precise;
        private int confidence;
        private int comprehension;
        private String level;

        public LocationBean getLocation() {
            return location;
        }

        public void setLocation(LocationBean location) {
            this.location = location;
        }

        public int getPrecise() {
            return precise;
        }

        public void setPrecise(int precise) {
            this.precise = precise;
        }

        public int getConfidence() {
            return confidence;
        }

        public void setConfidence(int confidence) {
            this.confidence = confidence;
        }

        public int getComprehension() {
            return comprehension;
        }

        public void setComprehension(int comprehension) {
            this.comprehension = comprehension;
        }

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }

        public static class LocationBean {
            /**
             * lng : 116.30789936413898
             * lat : 40.05703792688976
             */

            private double lng; //经度
            private double lat; //纬度

            public double getLng() {
                return lng;
            }

            public void setLng(double lng) {
                this.lng = lng;
            }

            public double getLat() {
                return lat;
            }

            public void setLat(double lat) {
                this.lat = lat;
            }
        }
    }
}
