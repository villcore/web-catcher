package com.villcore.web.network;

public class HttpClient{
    private static volatile HttpClient instance;

    private HttpClient() {}

    public static HttpClient getInstance() {
        if (instance == null) {
            synchronized (HttpClient.class) {
                if (instance == null) {
                    instance = configHttpClient();
                }
            }
        }
        return instance;
    }

    private static HttpClient configHttpClient() {
        return null;
    }
}
