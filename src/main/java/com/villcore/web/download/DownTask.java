package com.villcore.web.download;

import java.io.Serializable;

/**
 * create by villcore on 2018/6/6
 */
public class DownTask implements Serializable {
    private int id;
    private String name;
    private String url;
    private DownTaskStateEnum state;
    private long size;
    private long position;
}
