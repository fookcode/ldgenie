package com.vrv.ldgenie;

import android.app.Application;
import android.content.Context;

import com.vrv.imsdk.VIMClient;

/**
 * Created by kinee on 2016/3/24.
 */
public class ldGenieApplication extends Application {
    private final static String APP_ID = "ldgenie";
    private static Context appContext;

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = this;
        VIMClient.init(appContext, "ldgenie");
    }

    public static Context getAppContext() {
        return appContext;
    }
}
