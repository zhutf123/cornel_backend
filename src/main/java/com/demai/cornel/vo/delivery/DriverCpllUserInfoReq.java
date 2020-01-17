package com.demai.cornel.vo.delivery;

import lombok.Data;

/**
 * @Author binz.zhang
 * @Date: 2020-01-17    11:29
 * 司机完善个人信息的
 */
@Data
public class DriverCpllUserInfoReq {
    /** 用户名 **/
    private String name;
    private String userId;
    private String mobile;
    private String idCard;
    private Integer idType=1;
}
