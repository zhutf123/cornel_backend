package com.demai.cornel.vo.commodity;

import com.demai.cornel.model.Commodity;
import lombok.Builder;
import lombok.Data;

/**
 * @Author binz.zhang
 * @Date: 2019-12-31    12:10
 */
@Data
@Builder
public class CommodityResp {

    private Integer status;
    private Commodity commodityInfo;
}
