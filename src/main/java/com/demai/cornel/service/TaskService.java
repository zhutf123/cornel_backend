package com.demai.cornel.service;

import com.demai.cornel.dao.TaskInfoDao;
import com.demai.cornel.model.TaskInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * @Author binz.zhang
 * @Date: 2019-11-05    15:57
 */
@Slf4j
@Service
public class TaskService {
    @Resource
    private TaskInfoDao taskInfoDao;

    public TaskInfo getTaskInfoByTaskId(String taskId) {
        return taskInfoDao.selectTaskInfoByTaskId(taskId);
    }

}
