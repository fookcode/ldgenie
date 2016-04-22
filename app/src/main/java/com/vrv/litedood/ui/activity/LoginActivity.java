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

import com.vrv.imsdk.SDKManager;
import com.vrv.imsdk.model.Contact;
import com.vrv.litedood.LiteDoodApplication;
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

    public enum HandlerType  {TYPE_LOGIN, TYPE_AUTOLOGIN, TYPE_GET_MYSELF};
    private final static String RELOGIN = "relogin";

    private String sUserCode="", sPwd = "";

    private LoginRequestHandler handler = new LoginRequestHandler(this, HandlerType.TYPE_LOGIN);

    public static void startLoginActivity(Activity activity) {
        startLoginActivity(activity, true);

    }

    public static void startLoginActivity(Activity activity, boolean autoLogin) {
        Intent intent = new Intent();
        intent.putExtra(RELOGIN, autoLogin);
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
                setLoginContent();
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
                    Toast.makeText(LoginActivity.this, R.string.login_usercode_empty_message, Toast.LENGTH_SHORT).show();
                    return;
                }
                sPwd = ((EditText)findViewById(R.id.edtPassword)).getText().toString();
                if (sPwd.isEmpty()) {
                    Toast.makeText(LoginActivity.this, R.string.login_password_empty_message, Toast.LENGTH_SHORT).show();
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
                ForgotPasswordActivity.startForgotPasswordActivity(LoginActivity.this);
            }
        });

        findViewById(R.id.tvRegisterUser).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterActivity.startRegisterActivity(LoginActivity.this);
            }
        });
    }

    private class LoginRequestHandler extends RequestHandler {

        private Activity activity;
        private HandlerType type;

        public LoginRequestHandler(Activity activity, HandlerType type) {
            super(activity);
            this.activity = activity;
            this.type = type;
        }

        @Override
        public void handleSuccess(Message msg) {
            switch (type) {
                case TYPE_AUTOLOGIN:
                case TYPE_LOGIN:
                    long userid = SDKManager.instance().getAuth().getUserID();
                    RequestHelper.getUserInfo(userid, new LoginRequestHandler(LoginActivity.this, HandlerType.TYPE_GET_MYSELF));
                    break;
                case TYPE_GET_MYSELF: {
                    LiteDoodApplication.getAppContext().setMyself((Contact) msg.getData().getParcelable("data"));
                    MainActivity.startMainActivity(activity);
                    break;
                }
            }
        }

        @Override
        public void handleFailure(int code, String message) {

            Log.v(TAG, "登录失败 " + code + ": " + message);
            clearLoginInfo();
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

        private void clearLoginInfo() {
            LiteDoodApplication.getAppContext().setMyself(null);
        }
    }
}
