package com.b_noble.android.coolweather.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.b_noble.android.coolweather.service.AutoUpdateWeather;

/**
 * Created by mqy on 15/11/6.
 */
public class AutoUpdateReceiver extends BroadcastReceiver{


    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, AutoUpdateWeather.class);
        context.startActivity(i);
    }
}
