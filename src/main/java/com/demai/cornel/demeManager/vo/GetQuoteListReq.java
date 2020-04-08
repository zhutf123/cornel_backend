package com.demai.cornel.demeManager.vo;

import lombok.Data;

/**
 * @Author binz.zhang
 * @Date: 2020-03-08    13:28
 */
@Data
public class GetQuoteListReq {

    private Integer offset;
    private Integer pgSize;
    private String towerId;

}
