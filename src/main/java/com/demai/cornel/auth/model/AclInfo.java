package com.demai.cornel.auth.model;

import lombok.Data;

import java.util.Date;
import java.util.Map;
@Data
public class AclInfo {
    private static final long serialVersionUID = -3278874872049402020L;
    private Integer id ;
    private String name ;
    private String code ;
    private String url ;
    private Integer module ;
    private Integer status ;
    private Map<String,String> extInfo ;
    private Date createTime ;
    private Date operateTime ;
}
