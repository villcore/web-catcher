package com.villcore.web.download;

import java.io.Serializable;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

/**
 * create by WangTao on 2018/6/9
 */
public class DownTaskConfig implements Serializable {

    public Path configPath;
    public String url;
    public long totalFileLen;
    public List<Long> needDownBytes;
    public List<Long> hasDownBytes;

    public Path getConfigPath() {
        return configPath;
    }

    public void setConfigPath(Path configPath) {
        this.configPath = configPath;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getTotalFileLen() {
        return totalFileLen;
    }

    public void setTotalFileLen(long totalFileLen) {
        this.totalFileLen = totalFileLen;
    }

    public List<Long> getNeedDownBytes() {
        return needDownBytes;
    }

    public void setNeedDownBytes(List<Long> needDownBytes) {
        this.needDownBytes = needDownBytes;
    }

    public List<Long> getHasDownBytes() {
        return hasDownBytes;
    }

    public void setHasDownBytes(List<Long> hasDownBytes) {
        this.hasDownBytes = hasDownBytes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DownTaskConfig that = (DownTaskConfig) o;
        return totalFileLen == that.totalFileLen &&
                Objects.equals(configPath, that.configPath) &&
                Objects.equals(url, that.url) &&
                Objects.equals(needDownBytes, that.needDownBytes) &&
                Objects.equals(hasDownBytes, that.hasDownBytes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(configPath, url, totalFileLen, needDownBytes, hasDownBytes);
    }

    @Override
    public String toString() {
        return "DownTaskConfig{" +
                "configPath=" + configPath +
                ", url='" + url + '\'' +
                ", totalFileLen=" + totalFileLen +
                ", needDownBytes=" + needDownBytes +
                ", hasDownBytes=" + hasDownBytes +
                '}';
    }
}
