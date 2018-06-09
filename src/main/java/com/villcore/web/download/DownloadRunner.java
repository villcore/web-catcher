package com.villcore.web.download;

import com.villcore.web.download.dao.DownTaskDao;
import com.villcore.web.utils.JsonUtils;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * create by WangTao on 2018/6/7
 */
public class DownloadRunner implements Runnable {
    private static final Logger LOG = LoggerFactory.getLogger(DownloadRunner.class);

    private static final String CHARSET = "utf-8";

    private DownTask downTask;

    private volatile boolean running = false;
    private CountDownLatch stopLatch;

    private DownTaskManager downTaskManager;
    private DownTaskDao downTaskDao;

    public DownloadRunner(DownTask downTask, DownTaskManager downTaskManager, DownTaskDao downTaskDao) {
        this.downTask = downTask;
        this.downTaskManager = downTaskManager;
        this.downTaskDao = downTaskDao;
    }

    @Override
    public void run() {
        String taskId = downTask.getId() + "";
        String taskUrl = downTask.getUrl();

//        DownTaskConfig downTaskConfig = null;
//        try {
//            downTaskConfig = createOrGetConfig(taskId, taskUrl);
//        } catch (IOException e) {
//            LOG.error("start down task runner error.", e);
//        }

        if (running) {
//            if (downTaskConfig == null) {
//                return;
//            }

            try {
                OkHttpClient okHttpClient = new OkHttpClient.Builder()
                        .readTimeout(1L, TimeUnit.MINUTES)
                        .writeTimeout(1L, TimeUnit.MINUTES)
                        .build();

//                List<Long> hasDownBytes = downTaskConfig.getHasDownBytes();
//                long startPos = hasDownBytes.isEmpty() ? 0 : hasDownBytes.get(0);
                long startPos = 0;
                Request request = new Request.Builder()
                        .get()
                        .url(taskUrl)
                        .header("RANGE", "bytes=" + startPos + "-")
                        .build();

                Response response = okHttpClient.newCall(request).execute();
                long contentLen = response.body().contentLength();
//                downTaskConfig.setTotalFileLen(contentLen);
                String[] paths = downTask.getUrl().split("/");
                String fileName = paths.length > 1 ? paths[paths.length - 1] : taskId;
                try {
                    String fileDetail = response.header("Content-Disposition");
                    fileName = fileDetail.replace("filename=", "").replace("attachment;", "").trim();
                } catch (Exception e) {
                    LOG.warn("can not parse file name.", e);
                }

                downTask.setName(fileName);
                downTask.setTotalFileLen(contentLen);
                downTaskDao.updateDownTask(downTask);

                byte[] buffer = new byte[1024 * 1024]; //128K
                int pos = -1;

                try (InputStream is = response.body().byteStream();
                     RandomAccessFile os = getOrCreateFileDataStream(fileName, startPos)) {
                    int flushCount = 0;
                    while ((pos = is.read(buffer)) > 0 && running) {
                        os.write(buffer, 0, pos);
                        startPos += pos;
//                        downTaskConfig.setHasDownBytes(Collections.singletonList(startPos));
                        if (flushCount++ > 100) {
                            DownBytesCache.incDownBytes(downTask, startPos);
                            flushCount = 0;
                        }
                    }
                    downTask.setHasDownBytes(startPos);
//                    downTaskDao.updateDownTask(downTask);
                    downTask.setState(2);
                    downTaskDao.updateDownTask(downTask);
                    downTaskManager.removeDownloadRunner(downTask);
                    //TODO downTask setState
                } catch (IOException e) {
                    LOG.error("download [{}] error.", downTask, e);
                    downTask.setState(3);
                    downTaskDao.updateDownTask(downTask);
                    downTaskManager.removeDownloadRunner(downTask);
                    //TODO downTask setState
                }
            } catch (Exception e) {
                LOG.error("download [{}] error.", downTask, e);
                downTask.setState(3);
                downTaskDao.updateDownTask(downTask);
                downTaskManager.removeDownloadRunner(downTask);
            } finally {
                try {
                    Path configPath = Paths.get("config" + File.separator + taskId);
//                    writeConfig(configPath, downTaskConfig);
                } catch (Exception e) {
                    LOG.error("write down config error.", e);
                }
            }
        }
        stopLatch.countDown();
    }

    private RandomAccessFile getOrCreateFileDataStream(String fileName, long startPos) throws IOException {
        Path dataPath = Paths.get( "data" + File.separator + fileName);
        File dataFile = dataPath.toFile();
        if (!dataFile.exists()) {
            dataFile.getParentFile().mkdirs();
            dataFile.createNewFile();
        }
        RandomAccessFile dataFileStream = new RandomAccessFile(dataPath.toFile(), "rw");
        dataFileStream.seek(startPos);
        return dataFileStream;
    }

    public void start() {
        running = true;
        stopLatch = new CountDownLatch(1);
        new Thread(this, "DownloadRunner").start();
    }

    public void stop() {
        LOG.info("stopping down task [{}].", downTask);
        running = false;
        try {
            stopLatch.await();
        } catch (InterruptedException e) {
            LOG.warn("wait stop error.", e);
        }
        LOG.info("stop down task [{}] finshed.", downTask);
    }

    private DownTaskConfig createOrGetConfig(String taskName, String taskUrl) throws IOException {
        Path configPath = Paths.get("config" + File.separator + taskName);
        File configFile = configPath.toFile();
        if (!configFile.exists()) {
            configFile.getParentFile().mkdirs();
            configFile.createNewFile();
            DownTaskConfig downTaskConfig = new DownTaskConfig();
            downTaskConfig.setConfigPath(configPath);
            downTaskConfig.setUrl(taskUrl);
            downTaskConfig.setTotalFileLen(0);
            downTaskConfig.setHasDownBytes(Collections.emptyList());
            downTaskConfig.setHasDownBytes(Collections.emptyList());
            writeConfig(configPath, downTaskConfig);
            return downTaskConfig;
        }
        return readConfig(configPath);
    }

    private void writeConfig(Path configPath, DownTaskConfig downTaskConfig) throws IOException {
        Files.write(configPath, JsonUtils.toJson(downTaskConfig).getBytes(CHARSET));
    }

    private DownTaskConfig readConfig(Path configPath) throws IOException {
        byte[] bytes = Files.readAllBytes(configPath);
        String configJson = new String(bytes, CHARSET);
        return JsonUtils.toObject(configJson, DownTaskConfig.class);
    }
}
