package com.vrv.ldgenie.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.vrv.ldgenie.R;
import com.vrv.ldgenie.bpo.LoginRequestHandler;
import com.vrv.ldgenie.common.sdk.action.RequestHandler;
import com.vrv.ldgenie.common.sdk.action.RequestHelper;

/**
 * Created by kinee on 2016/3/25.
 */
public class LoginActivity extends AppCompatActivity {
    private final static String AREA_CODE = "vrv";
    private final static String NATIONAL_CODE = "0086";

    private String sUserCode="", sPwd = "";

    private LoginRequestHandler handler = new LoginRequestHandler(this);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

    }

    public static void startLoginActivity(Activity activity) {
        Intent intent = new Intent();
        intent.setClass(activity, LoginActivity.class);
        activity.startActivity(intent);
        activity.finish();

    }

}
