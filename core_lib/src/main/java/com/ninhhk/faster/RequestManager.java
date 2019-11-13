package com.ninhhk.faster;

import java.util.HashMap;

public class RequestManager {
    private HashMap<Key, Request> hashMap;

    private static RequestManager mInstance = null;

    private RequestManager(){
        hashMap = new HashMap<>();
    }

    public static RequestManager getInstance(){
        if (mInstance == null){
            synchronized (RequestManager.class){
                if (mInstance == null){
                    mInstance = new RequestManager();
                }
            }
        }
        return mInstance;
    }

    public Request getRequest(Key k){
        return hashMap.get(k);
    }

    public void clearRequest(Key k){
        hashMap.remove(k);
    }

    public void clearAllRequests(){
        hashMap.clear();
    }

    public void addRequest(Key key, Request request) {
        hashMap.put(key, request);
    }
}
