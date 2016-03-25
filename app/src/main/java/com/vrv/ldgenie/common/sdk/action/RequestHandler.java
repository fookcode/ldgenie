package com.vrv.ldgenie.common.sdk.action;


import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.vrv.ldgenie.ldGenieApplication;


/**
 * {@link CallBackHelper} SDK请求使用
 * Created by Yang on 2015/8/30 030.
 */
public abstract class RequestHandler extends Handler {

    private static final String TAG = RequestHandler.class.getSimpleName();
    public static final String KEY_DATA = "data";
    public static final int SUCCESS = 0;
    public static final int FAILURE = -1;
    public static final int REQUEST_FALSE = -2;
    public static final int SHOW_PRO = 2;
    public static final int DIS_PRO = 3;

    private Dialog dialog;

    public RequestHandler() {

    }

    public RequestHandler(Context context) {
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        switch (msg.what) {
            case SUCCESS:
                handleSuccess(msg);
                break;
            case REQUEST_FALSE:
                break;
            case FAILURE:
                handleFailure(msg.arg1, String.valueOf(msg.obj));
                break;
            case SHOW_PRO:
                if (dialog != null) {
                    dialog.show();
                }
                break;
            case DIS_PRO:
                try {
                    if (dialog != null) {
                        dialog.dismiss();
                        dialog = null;
                    }
                } catch (Exception e) {
                }
                break;
        }
    }

    public abstract void handleSuccess(Message msg);

    public void handleFailure(int code, String message) {
        Toast.makeText(ldGenieApplication.getAppContext(), "Code=" + code, Toast.LENGTH_SHORT).show();
    }
}
