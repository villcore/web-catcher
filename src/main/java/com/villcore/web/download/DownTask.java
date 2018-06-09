package com.villcore.web.download;

import java.io.Serializable;
import java.util.Objects;

/**
 * create by villcore on 2018/6/6
 */
public class DownTask implements Serializable {

    private int id;
    private String name;
    private String url;
    private DownTaskStateEnum state;
    private long totalFileLen;
    private volatile long hasDownBytes;

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

    public DownTaskStateEnum getState() {
        return state;
    }

    public void setState(DownTaskStateEnum state) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DownTask downTask = (DownTask) o;
        return id == downTask.id &&
                totalFileLen == downTask.totalFileLen &&
                hasDownBytes == downTask.hasDownBytes &&
                Objects.equals(name, downTask.name) &&
                Objects.equals(url, downTask.url) &&
                state == downTask.state;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, url, state, totalFileLen, hasDownBytes);
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
                '}';
    }
}
