package com.demai.cornel.model;

import lombok.Data;

import java.sql.Date;

/**
 * @Author binz.zhang
 * @Date: 2019-11-10    20:05
 */
@Data
public class SubTaskInfo {
    private static final long serialVersionUID = -6914169130042746224L;
    private Long id ;
    private String taskId ;
    private Date startTime ;
    private Date endTime ;
    private Integer lorryNum ;
    private Integer status ;
    private Integer undistNum ;
    private String subTaskId ;

}
