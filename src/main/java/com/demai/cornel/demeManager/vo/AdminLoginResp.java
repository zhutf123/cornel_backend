package com.demai.cornel.demeManager.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * @Author binz.zhang
 * @Date: 2020-03-08    13:07
 */
@Data

public class AdminLoginResp {
    private Integer id;

    /**
     * userID
     */
    private String userId;

    /**
     * 名字
     */
    private String userName;

    /**
     * 手机号
     */
    private String mobile;

    private Integer code;

}
