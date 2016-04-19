package com.vrv.litedood.common;

import com.vrv.litedood.LiteDoodApplication;

/**
 * Created by kinee on 2016/4/18.
 */
public final class LiteDood {

    public static final String SCHEME = "content://";

    public static final String AUTHORITY = "com.vrv.litedood.provider";

    public static final String URI = SCHEME + AUTHORITY;

    public static final String DB_NAME = LiteDoodApplication.APP_ID + ".db";
}
