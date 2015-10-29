package com.b_noble.android.networktest.util;

/**
 * Created by mqy on 15/10/29.
 */
public interface HttpCallbackListener {

    public void onFinish(String response);
    public void onError(Exception e);

}
