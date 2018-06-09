package com.villcore.web.download;

import com.villcore.web.download.service.DownTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * create by WangTao on 2018/6/6
 */
@Component
public class DownTaskManager {
    //network
    //file
    //thread

    //sechudler add downtask not start to queue, add start task.

    //add a task.
    // check resouce, not ready add to queue.

    //task ready.
    //  okhttpclient open, read add transfer to file.

    //task resume, close connection, save offset.

    //@Autowired
    DownTaskService downTaskService;

    private Map<DownTask, DownloadRunner> downloadRunnerMap = new ConcurrentHashMap<>();

    public List<DownTask> listPending() {
        return downTaskService.listPendingTask(0, 10);
    }

    public boolean addTask(){
        //start task
        //
        return false;
    }

    void startTask(DownTask task) {
        String url = "";

    }

    private void ensureEnoughCapacity() {}

    private File createDataFile(String name, long dataSize) {
        return null;
    }

    private File createConfigFile(String name) {
        return null;
    }

    private DownloadRunner createDownloadRunner(DownTask task, File dataFile, File configFile) {
        return null;
    }
}
