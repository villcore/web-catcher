package com.villcore.web.rest;

import com.villcore.web.download.DownBytesCache;
import com.villcore.web.download.DownTask;
import com.villcore.web.download.DownTaskManager;
import com.villcore.web.download.dao.DownTaskDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

/**
 * create by villcore on 2018/6/9
 */
@RestController
@EnableAutoConfiguration
public class DownloadController {
    @Autowired
    private DownTaskDao downTaskDao;

    @Autowired
    private DownTaskManager downTaskManager;

//    @RequestMapping("/download/addDownTask")
//    public int addDownTask(DownTask downTask) {
//        downTask.setSubmitTimestamp(System.currentTimeMillis());
//        return downTaskDao.saveDownTask(downTask);
//    }

    @RequestMapping("/download/addDownTask")
    public int addDownTask(String url) {
        DownTask downTask = new DownTask();
        downTask.setUrl(url);
        downTask.setSubmitTimestamp(System.currentTimeMillis());
        return downTaskDao.saveDownTask(downTask);
    }

    @RequestMapping("/download/deleteDownTask")
    public int deleteDownTask(int id) {
        DownTask downTask = downTaskDao.getDownTask(id);
        if (downTask != null) {
            downTaskManager.deleteDataFile(downTask.getName());
            downTaskDao.deleteDownTask(id);
            return 1;
        }
        return 0;
    }

    @RequestMapping("/download/getDownTask")
    public DownTask getDownTask(int id) {
        return downTaskDao.getDownTask(id);
    }

    @RequestMapping("/download/listDownTask")
    public List<DownTask> listDownTask(@RequestParam(value = "start", defaultValue = "0") int start,
                                       @RequestParam(value = "size", defaultValue = "10") int size) {
        List<DownTask> downTasks = downTaskDao.listDownTask(start, size);
        for (DownTask downTask : downTasks) {
            if (downTask.getHasDownBytes() == 0) {
                downTask.setHasDownBytes(DownBytesCache.getDownBytes(downTask));
            }
            downTask.setAttr(Collections.singletonMap("down_url", "http://207.246.108.224:22/home/data/"+downTask.getName()));
        }
        return downTasks;
    }
}
