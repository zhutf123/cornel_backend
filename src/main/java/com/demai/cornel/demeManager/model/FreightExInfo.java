package com.demai.cornel.demeManager.model;

import com.demai.cornel.dmEnum.IEmus;
import lombok.Data;

/**
 * @Author binz.zhang
 * @Date: 2020-04-03    19:42
 */
@Data public class FreightExInfo {

    private String key;
    private String value;
    private Integer type;

    public static enum TYPE_ENUE {
        MOTO_COST("moto_cost", "汽运费", 1), STATION_NAME("station_name", "场站名称", 2), STATION_COST("station_cost", "场站费",
                1), HIRE_COST("hire_cost", "租车费", 1), UNLOAD_COST("unload_cost", "卸车费", 1), OTHER_COST("other_cost",
                "其他费用", 1);
        private String key;
        private String value;
        private Integer type;

        private TYPE_ENUE(String key, String value, Integer type) {
            this.key = key;
            this.value = value;
            this.type = type;
        }

        public String getKey() {
            return key;
        }

        public String getValue() {
            return value;
        }

        public Integer getType() {
            return type;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public void setType(Integer type) {
            this.type = type;
        }
    }

}
