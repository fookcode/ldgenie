package com.vrv.litedood;

import android.app.Application;

import com.vrv.imsdk.VIMClient;

/**
 * Created by kinee on 2016/3/24.
 */
public class LiteDoodApplication extends Application {
    public final static String APP_ID = "litedood";
    private static LiteDoodApplication appContext;

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = this;
        VIMClient.init(appContext, "litedood");
    }

    public static LiteDoodApplication getAppContext() {
        return appContext;
    }

}
