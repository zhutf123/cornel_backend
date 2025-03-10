package com.demai.cornel.model;

import lombok.Data;

import java.sql.Date;
import java.sql.Timestamp;

/**
 * @Author binz.zhang
 * @Date: 2019-11-10    20:05
 */
@Data
public class SubTaskInfo {
    private static final long serialVersionUID = -6914169130042746224L;
    private Long id ;
    private String taskId ;
    private String startTime ;
    private String endTime ;
    private Integer lorryNum ;
    private Integer status ;
    private Integer undistNum ;
    private String subTaskId ;

}
