package com.villcore.web.download;

import java.util.List;

/**
 * create by villcore on 2018/6/6
 */
public interface DownTaskService {

    boolean addTask(DownTask task);

    List<DownTask> listTask(int offset, int pageNum);

    DownTask getTask(int taskId);
}
