package com.villcore.web.download;

import java.util.List;

/**
 * create by villcore on 2018/6/6
 */
public interface DownTaskService {

    boolean addTask(DownTask task);

    List<DownTask> listTask(int offset, int pageNum);

    List<DownTask> listPendingTask(int offset, int pageNum);

    DownTask startTask(int taskId);

    DownTask getTask(int taskId);

    DownTask cancelTask(int taskId);

    DownTask pauseTask(int taskId);

    DownTask resumeTask(int taskId);

    DownTask deleteTask(int taskId);
}
