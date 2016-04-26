package com.example.tantao.coolweather.content;

import android.app.Application;
import android.content.Context;

/**
 * Created by tantao on 2016/4/25.
 */
public class baseContent extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context.getApplicationContext();
    }

    public static Context getContext(){
        return context;
    }

}
