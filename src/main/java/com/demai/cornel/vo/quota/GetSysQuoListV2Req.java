package com.demai.cornel.vo.quota;

import lombok.Data;

import java.sql.Timestamp;

/**
 * @Author binz.zhang
 * @Date: 2020-03-09    15:53
 */
@Data
public class GetSysQuoListV2Req {
    private Timestamp time;
    private Integer pgSize;
}
