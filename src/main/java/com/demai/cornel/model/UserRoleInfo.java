package com.demai.cornel.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRoleInfo implements Serializable {
    private static final long serialVersionUID = -6717916328374105120L;
    private Long id;
    private String userId;
    private String roleId;
    private Integer status;
    private Map<String, String> extInfo;
    private String createTime;
    private String operateTime;
}