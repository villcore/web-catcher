package com.villcore.web.download;

/**
 * create by WangTao on 2018/6/6
 */
public enum  DownTaskStateEnum {
    NEW(0),
    START(1),
    RESUME(2),
    FINISH(3),
    FAILED(4);

    private final int state;

    DownTaskStateEnum(int state) {
        this.state = state;
    }
}
