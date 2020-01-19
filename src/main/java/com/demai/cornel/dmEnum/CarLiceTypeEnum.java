package com.demai.cornel.dmEnum;

import java.util.Arrays;

/**
 * @Author binz.zhang
 * @Date: 2020-01-19    15:03
 */
public enum  CarLiceTypeEnum {
    SELF_OWNER("本人", "本人所有"), OTHER_OWNER("他人", "他人所有"), COMPANY_OWNER("公司", "公司所有");
    private String type;
    private String name;

    CarLiceTypeEnum(String type, String name) {
        this.name = name;
        this.type = type;
    }

    public String getType() {
        return this.name;
    }

    public String getName() {
        return null;
    }

    public static CarLiceTypeEnum typeOf(String type) {
        return Arrays.stream(CarLiceTypeEnum.values()).filter(carLiceTypeEnum -> carLiceTypeEnum.getType() == type).findAny()
                .orElse(null);
    }
}
