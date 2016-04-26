package com.example.tantao.coolweather.util;

/**
 * Created by tantao on 2016/4/25.
 */
public interface HttpCallbackListener {

    void onFinish(String request);

    void onError(Exception err);
}
