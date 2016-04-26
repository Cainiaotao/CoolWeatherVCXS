package com.example.tantao.coolweather.server;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;

import com.example.tantao.coolweather.broadcast.AutoUpdateReceiver;
import com.example.tantao.coolweather.util.HttpCallbackListener;
import com.example.tantao.coolweather.util.HttpUtil;
import com.example.tantao.coolweather.util.Utility;

/**
 * Created by tantao on 2016/4/26.
 */
public class AutoUpdateService extends Service {
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                updateweather();
            }
        }).start();

        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int anHour = 30*60* 1000; // 这是30min的毫秒数
        long triggerAtTime = SystemClock.elapsedRealtime() + anHour;
        Intent i = new Intent(this, AutoUpdateReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);

        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void updateweather(){
        SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(this);
        String weathercode=preferences.getString("weather_code", "");
        String address = "http://www.weather.com.cn/data/cityinfo/" + weathercode + ".html";
        HttpUtil.sendResponseHttpURLConnection(address, new HttpCallbackListener() {
            @Override
            public void onFinish(String request) {
                Utility.handleWeatherResponse(AutoUpdateService.this,request);
            }

            @Override
            public void onError(Exception err) {
                err.printStackTrace();
            }
        });


    }
}
