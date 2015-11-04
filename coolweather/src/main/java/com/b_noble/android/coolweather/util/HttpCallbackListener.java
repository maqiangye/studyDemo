package com.b_noble.android.coolweather.util;

/**
 * Created by mqy on 15/11/3.
 */
public interface HttpCallbackListener {

    public void onFinish(String response);

    public void onError(Exception e);
}
