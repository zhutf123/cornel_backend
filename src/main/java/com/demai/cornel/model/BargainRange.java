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
    private Integer upper; // +5元
    private Integer down;// -5

}
