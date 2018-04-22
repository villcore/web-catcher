package com.villcore.web.catcher;

import com.villcore.web.utils.BytesUtils;

public class ResponseBundle {
    private RequestBundle requestBundle;
    private byte[] respBytes;
    private String respHtml;

    public String getRespHtml() {
        return respHtml;
    }

    public void setRespHtml(String respHtml) {
        this.respHtml = respHtml;
    }

    public RequestBundle getRequestBundle() {
        return requestBundle;
    }

    public void setRequestBundle(RequestBundle requestBundle) {
        this.requestBundle = requestBundle;
    }

    public byte[] getRespBytes() {
        return respBytes;
    }

    public void setRespBytes(byte[] respBytes) {
        this.respBytes = respBytes;
    }

    public String toHtml() {
        if (respHtml != null) {
            return respHtml;
        }

        if (respBytes != null && respBytes.length > 0) {
            respHtml = BytesUtils.toStringOrEmpty(respBytes);
            return respHtml;
        }
        return "";
    }
}
