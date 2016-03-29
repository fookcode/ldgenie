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
public class LoginRequestHandler extends RequestHandler {

//    private static final int LOGIN_USERCODE_PASSWORD_ERROR_HINT = 112;

    public LoginRequestHandler(Context context) {
        super(context);
    }

    @Override
    public void handleSuccess(Message msg) {
        MainActivity.startMainActivity((Activity)getContext());
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