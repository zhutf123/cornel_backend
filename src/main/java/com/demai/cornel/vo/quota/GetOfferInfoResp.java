package com.demai.cornel.vo.quota;

import lombok.Data;

/**
 * @Author binz.zhang
 * @Date: 2020-01-06    15:45
 */
@Data public class GetOfferInfoResp extends GetOfferListResp {
    private String userName;
    private String mobile;
    private String location;
}
