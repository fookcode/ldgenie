package com.vrv.ldgenie.bpo;

import android.app.Activity;
import android.content.Context;
import android.os.Message;
import android.widget.Toast;

import com.vrv.ldgenie.common.sdk.action.RequestHandler;
import com.vrv.ldgenie.ui.activity.LoginActivity;
import com.vrv.ldgenie.ui.activity.MainActivity;

/**
 * Created by kinee on 2016/3/29.
 */
public class GenieRequestHandler extends RequestHandler {
    public static final int HANDLER_LOGIN = 1;
    public static final int HANDLER_LOGOUT = 2;
    public static final int HANDLER_SEND_MESSAGE = 3;
    private int handlerType;


    public GenieRequestHandler(int handlerType, Context context) {
        super(context);
        this.handlerType = handlerType;
    }

    @Override
    public void handleSuccess(Message msg) {
        switch (handlerType) {
            case HANDLER_LOGIN: {
                MainActivity.startMainActivity((Activity)getContext());
                break;
            }
            case HANDLER_LOGOUT: {
                LoginActivity.startLoginActivity((Activity)getContext());
                break;
            }
        }
    }

//    @Override
//    public void handleFailure(int code, String message) {
//        switch(code) {       //处理已知消息，否则显示返回码
//            case LOGIN_USERCODE_PASSWORD_ERROR_HINT : {
//                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
//                break;
//            }
//            default: super.handleFailure(code, message);
//        }
//    }
}