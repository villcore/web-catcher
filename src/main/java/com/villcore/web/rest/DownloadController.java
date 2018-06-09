package com.villcore.web.rest;

import com.villcore.web.download.DownTask;
import com.villcore.web.download.dao.DownTaskDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * create by villcore on 2018/6/9
 */
@RestController
@EnableAutoConfiguration
public class DownloadController {
    @Autowired
    private DownTaskDao downTaskDao;

    @RequestMapping("/download/addDownTask")
    public int addDownTask(DownTask downTask) {
        DownTask downTask1 = new DownTask();
        downTask1.setName("test");
        downTask1.setUrl("http://www.baidu.com");
        downTask1.setState(0);
        downTask1.setTotalFileLen(100);
        downTask1.setHasDownBytes(100);
        return downTaskDao.saveDownTask(downTask1);
    }
}
