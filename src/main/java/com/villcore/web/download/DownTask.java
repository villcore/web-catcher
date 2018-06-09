package com.villcore.web.download;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * create by villcore on 2018/6/6
 */
public class DownTask implements Serializable {

    private int id;
    private String name;
    private String url;
    private int state;
    private long totalFileLen;
    private volatile long hasDownBytes;
    private long submitTimestamp;
    private long finishTimestamp;
    private Map<String, Object> attr = new HashMap<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public long getTotalFileLen() {
        return totalFileLen;
    }

    public void setTotalFileLen(long totalFileLen) {
        this.totalFileLen = totalFileLen;
    }

    public long getHasDownBytes() {
        return hasDownBytes;
    }

    public void setHasDownBytes(long hasDownBytes) {
        this.hasDownBytes = hasDownBytes;
    }

    public long getSubmitTimestamp() {
        return submitTimestamp;
    }

    public void setSubmitTimestamp(long submitTimestamp) {
        this.submitTimestamp = submitTimestamp;
    }

    public long getFinishTimestamp() {
        return finishTimestamp;
    }

    public void setFinishTimestamp(long finishTimestamp) {
        this.finishTimestamp = finishTimestamp;
    }

    public Map<String, Object> getAttr() {
        return attr;
    }

    public void setAttr(Map<String, Object> attr) {
        this.attr = attr;
    }

    @Override
    public String toString() {
        return "DownTask{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", state=" + state +
                ", totalFileLen=" + totalFileLen +
                ", hasDownBytes=" + hasDownBytes +
                ", submitTimestamp=" + submitTimestamp +
                ", finishTimestamp=" + finishTimestamp +
                ", attr=" + attr +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DownTask downTask = (DownTask) o;
        return id == downTask.id &&
                state == downTask.state &&
                totalFileLen == downTask.totalFileLen &&
                hasDownBytes == downTask.hasDownBytes &&
                submitTimestamp == downTask.submitTimestamp &&
                finishTimestamp == downTask.finishTimestamp &&
                Objects.equals(name, downTask.name) &&
                Objects.equals(url, downTask.url) &&
                Objects.equals(attr, downTask.attr);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, name, url, state, totalFileLen, hasDownBytes, submitTimestamp, finishTimestamp, attr);
    }
}
