package com.vrv.litedood;

import android.app.Activity;
import android.app.Application;

import com.vrv.imsdk.VIMClient;
import com.vrv.imsdk.model.Contact;
import com.vrv.litedood.common.LiteDood;

/**
 * Created by kinee on 2016/3/24.
 */
public class LiteDoodApplication extends Application {

    private static LiteDoodApplication mAppContext;

    private static Activity mMainActivity;

    private Contact myself = null;

    @Override
    public void onCreate() {
        super.onCreate();
        mAppContext = this;
        VIMClient.init(mAppContext, LiteDood.APP_ID);

    }

    public void setMyself(Contact contact) {
        myself = contact;
    };

    public Contact getMyself() {
        return myself;
    }

    public static LiteDoodApplication getAppContext() {
        return mAppContext;
    }

    public static void setMainActivity(Activity activity) {
        mMainActivity = activity;
    }

    public static Activity getMainActivity() {
        return mMainActivity;
    }

}
