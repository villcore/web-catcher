package com.villcore.web.download;

import com.villcore.web.download.dao.DownTaskDao;
import com.villcore.web.download.service.DownTaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import static java.util.Calendar.SECOND;

/**
 * create by WangTao on 2018/6/6
 */
@Component
public class DownTaskManager implements Runnable {
    private static final Logger LOG = LoggerFactory.getLogger(DownTaskManager.class);

    private static final int MAX_RUNNING_TASK = 10;

    @Autowired
    DownTaskDao downTaskDao;

    private Map<DownTask, DownloadRunner> downloadRunnerMap = new ConcurrentHashMap<>();

    public void removeDownloadRunner(DownTask downTask) {
        downloadRunnerMap.remove(downTask);
    }

    public List<DownTask> listPending() {
        return downTaskDao.listUnfinishDownTask(0, 10);
    }

    public boolean addTask(){
        //start task
        //
        return false;
    }

    void startTask(DownTask task) {
        String url = "";
    }

    @PostConstruct
    public void start() {

    }

    @Scheduled(fixedRate = 10000)
    @Override
    public void run() {
        List<DownTask> downTasks = listPending();
        LOG.info("auto scan unfinish [{}] task.", downTasks.size());
        for (DownTask downTask : downTasks) {
            if (downloadRunnerMap.size() < MAX_RUNNING_TASK) {
                downTask.setState(1);
                downTaskDao.updateDownTask(downTask);
                DownloadRunner downloadRunner = new DownloadRunner(downTask, this, downTaskDao);
                downloadRunner.start();
                downloadRunnerMap.put(downTask, downloadRunner);
                LOG.info("add a new task.");
            } else {
                LOG.info("current task exceed max.");
                return;
            }
        }
    }

    @PreDestroy
    public void stop() {
        downloadRunnerMap.forEach((k, v) -> {
            v.stop();
        });
        downloadRunnerMap.clear();
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

    public void deleteDataFile(String name) {
        try {
            Path dataPath = Paths.get("data", name);
            File dataFile = dataPath.toFile();
            if (dataFile.exists()) {
                dataFile.delete();
            }
        } catch (Exception e) {
            LOG.error("delete data file [{}] failed.", name);
        }
    }
}
