package com.villcore.web.catcher;

//TODO use factory method

import java.util.Map;

public class RequestBundle {
    private HttpTypeEnum httpTypeEnum;
    private String requestUrl;
    private Map<String, String> params;
    private Map<String, String> headers;

    //TODO

    public HttpTypeEnum getHttpTypeEnum() {
        return httpTypeEnum;
    }

    public void setHttpTypeEnum(HttpTypeEnum httpTypeEnum) {
        this.httpTypeEnum = httpTypeEnum;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }
}
