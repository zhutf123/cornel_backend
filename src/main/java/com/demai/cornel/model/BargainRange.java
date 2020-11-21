package com.demai.cornel.model;

import lombok.Builder;
import lombok.Data;

/**
 * @Author binz.zhang
 * @Date: 2020-01-02    14:28
 */
@Data
@Builder
public class BargainRange {
    /**
     * 线上次数
     */
    private Integer upper = 6;
    /***
     *  向下次数
     */
    private Integer down = 6;
    /***
     *   每次变更的金额 ，默认5
     */
    private Integer unit = 5;

}
