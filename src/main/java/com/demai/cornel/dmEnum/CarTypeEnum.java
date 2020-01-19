package com.demai.cornel.dmEnum;

import java.util.Arrays;

/**
 * @Author binz.zhang
 * @Date: 2020-01-19    14:54
 */
public enum CarTypeEnum {
    SMLL_TRUCK("小卡车", "小卡车"), HIGH_BURD_TRUCK("高栏货车", "高栏货车"), FLAT_TRUCK("平板货车", "平板货车"), VAN_TRUCK("厢式货车",
            "厢式货车厢式货车"), LIGHT_TRUCK("轻型货车", "轻型货车");
    private String type;
    private String name;

    CarTypeEnum(String type, String name) {
        this.name = name;
        this.type = type;
    }

    public String getType() {
        return this.name;
    }

    public String getName() {
        return null;
    }

    public static CarTypeEnum typeOf(String type) {
        return Arrays.stream(CarTypeEnum.values()).filter(carTypeEnum -> carTypeEnum.getType() == type).findAny()
                .orElse(null);
    }
}
