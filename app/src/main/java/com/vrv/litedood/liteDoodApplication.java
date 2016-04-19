package com.vrv.litedood;

import android.app.Application;

import com.vrv.imsdk.VIMClient;
import com.vrv.litedood.common.LiteDood;

/**
 * Created by kinee on 2016/3/24.
 */
public class LiteDoodApplication extends Application {

    private static LiteDoodApplication appContext;

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = this;
        VIMClient.init(appContext, LiteDood.APP_ID);
    }

    public static LiteDoodApplication getAppContext() {
        return appContext;
    }

}
