package com.villcore.web.download;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class DownBytesCache {
    private static final Map<DownTask, AtomicLong> downBytes = Collections.synchronizedMap(new LinkedHashMap(){
        @Override
        protected boolean removeEldestEntry(Map.Entry eldest) {
            if (downBytes.size() > MAX_CACHE_COUNT) {
                return true;
            }
            return false;
        }
    });

    private static final int MAX_CACHE_COUNT = 100;

    public static void incDownBytes(DownTask downTask, long bytesSize) {
        if (downBytes.containsKey(downTask)) {
            downBytes.get(downTask).set(bytesSize);
        }
    }

    public static long getDownBytes(DownTask downTask) {
        if (downBytes.containsKey(downTask)) {
            return downBytes.get(downTask).get();
        }
        return 0;
    }
}
