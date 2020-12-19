/**
 * Copyright (c) 2019 demai.com. All Rights Reserved.
 */
package com.demai.cornel.model;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Map;

import com.demai.cornel.dmEnum.CarTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Create By zhutf 19-10-6 下午1:04
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleInfo implements Serializable {
    private static final long serialVersionUID = -8784176082403415679L;
    private Long id;
    private String name;
    private String roleId;
    private String aclCode;
    private Integer status;
    private Map<String, String> extInfo;
    private String createTime;
    private String operateTime;


    public enum ROLE_TYPE_ENUM {

        BUS_OP("bus_op", "4"), FIN_OP("fin_op", "5");
        private String desc;
        private String routeId;

        ROLE_TYPE_ENUM(String desc, String routeId) {
            this.desc = desc;
            this.routeId = routeId;
        }

        public String getDesc() {
            return desc;
        }

        public String getRouteId() {
            return routeId;
        }

        public static ROLE_TYPE_ENUM descValueOf(String desc) {
            return Arrays.stream(ROLE_TYPE_ENUM.values()).filter(carTypeEnum -> carTypeEnum.getDesc().equals(desc)).findAny()
                    .orElse(null);
        }
        
    }

}