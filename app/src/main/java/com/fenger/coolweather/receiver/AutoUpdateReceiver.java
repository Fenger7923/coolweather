package com.fenger.coolweather.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.fenger.coolweather.service.AutoUpdateService;

/**
 * com.fenger.coolweather.receiver
 * Created by fenger
 * in 2020/6/9
 */
public class AutoUpdateReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, AutoUpdateService.class);
        context.startService(i);
    }
}
