package com.vrv.litedood.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.vrv.litedood.R;
import com.vrv.litedood.common.sdk.action.RequestHandler;
import com.vrv.litedood.common.sdk.action.RequestHelper;

/**
 * Created by kinee on 2016/3/25.
 */
public class LoginActivity extends AppCompatActivity {
    private final static String TAG = LoginActivity.class.getSimpleName();


    private final static String AREA_CODE = "vrv";
    private final static String NATIONAL_CODE = "0086";

    public enum HandlerType  {TYPE_LOGIN, TYPE_AUTOLOGIN};
    private final static String RELOGIN = "relogin";

    private String sUserCode="", sPwd = "";

    private LoginRequestHandler handler = new LoginRequestHandler(this, HandlerType.TYPE_LOGIN);

    public static void startLoginActivity(Activity activity) {
        startLoginActivity(activity, true);

    }

    public static void startLoginActivity(Activity activity, boolean isLogout) {
        Intent intent = new Intent();
        intent.putExtra(RELOGIN, isLogout);
        intent.setClass(activity, LoginActivity.class);
        activity.startActivity(intent);
        activity.finish();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        if (!intent.getBooleanExtra(RELOGIN, false)) {
            boolean loginResult = RequestHelper.autoLogin(new LoginRequestHandler(this, HandlerType.TYPE_AUTOLOGIN));
            if (!loginResult) {
                Log.v(TAG, "SDK autoLogin()调用失败");
            }
        }
        else {
            setLoginContent();
        }
    }


    public void setLoginContent() {
        setContentView(R.layout.activity_login);

        findViewById(R.id.btnLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sUserCode = ((EditText)findViewById(R.id.edtUserCode)).getText().toString();
                if(sUserCode.isEmpty()) {
                    Toast.makeText(LoginActivity.this, R.string.login_usercode_empty_hint, Toast.LENGTH_SHORT).show();
                    return;
                }
                sPwd = ((EditText)findViewById(R.id.edtPassword)).getText().toString();
                if (sPwd.isEmpty()) {
                    Toast.makeText(LoginActivity.this, R.string.login_password_empty_hint, Toast.LENGTH_SHORT).show();
                    return;
                }

                handler.sendEmptyMessage(RequestHandler.SHOW_PRO);
                try {

                    boolean login = RequestHelper.login(sUserCode, sPwd, AREA_CODE,NATIONAL_CODE, handler);
                    if(!login) {
                        handler.sendEmptyMessage(RequestHandler.REQUEST_FALSE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(RequestHandler.DIS_PRO);
                } finally {
                    handler.sendEmptyMessage(RequestHandler.DIS_PRO);
                }
            }
        });

        findViewById(R.id.tvForgotPassword).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        findViewById(R.id.tvRegisterUser).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        findViewById(R.id.tvRegisterUser).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterActivity.startActivity(LoginActivity.this);
            }
        });
    }

    private class LoginRequestHandler extends RequestHandler {

        private Activity activity;
        private HandlerType type;

        public LoginRequestHandler(Activity activity, HandlerType type) {
            super();
            this.activity = activity;
            this.type = type;
        }

        @Override
        public void handleSuccess(Message msg) {
            MainActivity.startMainActivity(activity);
        }

        @Override
        public void handleFailure(int code, String message) {

            Log.v(TAG, "登录失败 " + code + ": " + message);

            switch(type) {
                case TYPE_AUTOLOGIN: {
                    setLoginContent();
                    break;
                }
                case TYPE_LOGIN: {
                    String hint = "登录失败";
                    if ((message != null) && (!message.isEmpty())) {
                        hint = hint + ": " + message;
                    } else {
                        hint = hint + ", 请与管理员联系";
                    }

                    Toast.makeText(activity, hint, Toast.LENGTH_SHORT).show();
                    break;
                }
            }

        }
    }
}
