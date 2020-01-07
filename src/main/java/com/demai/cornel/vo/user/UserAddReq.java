package com.demai.cornel.vo.user;

import lombok.Data;

/**
 * @Author binz.zhang
 * @Date: 2020-01-07    13:09
 */
@Data
public class UserAddReq {
    private String userId;
    private String userName;
    private String idCard;
    private String idType;
    private String mobile;
}
