package com.example.tantao.coolweather.activity;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tantao on 2016/4/25.
 */
public class ActivityCollector {

    /**
     * create Activity list
     **/
    public static List<Activity> activities=new ArrayList<Activity>();
    /**
     * Add activity
     * */
    public static void AddActivity(Activity activity){
        activities.add(activity);
    }

    /**
     * Remove activity
     * */
    public static void RemoveActivity(Activity activity)
    {
        activities.remove(activity);
    }
    /***
     *finish All activity
     */
    public  static void finishAll(){
        for (Activity activity:activities)
        {
            if (!activity.isFinishing()){
                activity.finish();
            }
        }
    }


}
