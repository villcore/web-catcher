package com.villcore.web.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BytesUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(BytesUtils.class);

    private static final String DEFAULT_CHARSET = "utf-8";

    public static String toStringOrEmpty(byte[] bytes) {
        return toStringOrEmpty(bytes, DEFAULT_CHARSET);
    }

    public static String toStringOrEmpty(byte[] bytes, String charset) {
        try {
            return toString(bytes, charset);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return "";
    }

    public static String toString(byte[] bytes) throws Exception {
        return toString(bytes, DEFAULT_CHARSET);
    }

    public static String toString(byte[] bytes, String charset) throws Exception {
        return new String(bytes, charset);
    }
}