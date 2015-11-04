package com.b_noble.android.coolweather.util;

import android.renderscript.ScriptGroup;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by mqy on 15/11/3.
 */
public class HttpUtil {

    /**
     * 发送http请求工具类
     * @param address
     * @param listener
     */
    public static void sendHttpRequest(final String address,final HttpCallbackListener listener){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try{
                    URL url = new URL(address);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    InputStream in = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while((line = reader.readLine()) != null){
                        sb.append(line);
                    }
                    if(listener != null){
                        listener.onFinish(sb.toString());
                    }

                }catch(Exception e){
                    if(listener != null){
                        listener.onError(e);
                    }
                }finally{
                    if(connection != null){
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }


}
