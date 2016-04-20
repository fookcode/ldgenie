package com.vrv.litedood;

import android.app.Application;

import com.vrv.imsdk.SDKManager;
import com.vrv.imsdk.VIMClient;
import com.vrv.imsdk.model.Contact;
import com.vrv.litedood.common.LiteDood;

/**
 * Created by kinee on 2016/3/24.
 */
public class LiteDoodApplication extends Application {

    private static LiteDoodApplication appContext;

    private Contact myself = null;

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = this;
        VIMClient.init(appContext, LiteDood.APP_ID);

    }

    public void setMyself(Contact contact) {
        myself = contact;
    };

    public Contact getMyself() {
        return myself;
    }

    public static LiteDoodApplication getAppContext() {
        return appContext;
    }

}
