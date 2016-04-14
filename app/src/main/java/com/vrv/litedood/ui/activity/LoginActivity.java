package com.vrv.litedood.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.vrv.litedood.R;
import com.vrv.litedood.bpo.LiteDoodRequestHandler;
import com.vrv.litedood.common.sdk.action.RequestHandler;
import com.vrv.litedood.common.sdk.action.RequestHelper;

/**
 * Created by kinee on 2016/3/25.
 */
public class LoginActivity extends AppCompatActivity {
    private final static String TAG = LoginActivity.class.getSimpleName();

    private final static String AREA_CODE = "vrv";
    private final static String NATIONAL_CODE = "0086";

    private String sUserCode="", sPwd = "";

    private LiteDoodRequestHandler handler = new LiteDoodRequestHandler(LiteDoodRequestHandler.HANDLER_LOGIN, this);

    public static void startLoginActivity(Activity activity) {
        Intent intent = new Intent();
        intent.setClass(activity, LoginActivity.class);
        activity.startActivity(intent);
        activity.finish();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(TAG, getIntent().getData() == null?"null":getIntent().getData().toString());
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

        findViewById(R.id.forgot_password).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        findViewById(R.id.register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private class LoginRequestHandler extends RequestHandler {
        @Override
        public void handleSuccess(Message msg) {

        }

        @Override
        public void handleFailure(int code, String message) {
            super.handleFailure(code, message);
        }

    }


}
