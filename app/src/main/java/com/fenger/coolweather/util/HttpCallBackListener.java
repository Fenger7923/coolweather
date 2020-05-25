package com.fenger.coolweather.util;

/**
 * com.fenger.coolweather.util
 * Created by fenger
 * in 2020/5/10
 * 回调的方法放到监听器
 */
public interface HttpCallBackListener {
    void onFinish(String response);

    void onError(Exception e);
}
