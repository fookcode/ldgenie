package com.vrv.litedood.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import com.vrv.litedood.R;
import com.vrv.litedood.common.sdk.action.RequestHandler;
import com.vrv.litedood.common.sdk.action.RequestHelper;

/**
 * Created by kinee on 2016/4/18.
 */
public class RegisterActivity extends AppCompatActivity {
    private final static String TAG = RegisterActivity.class.getSimpleName();
    private Toolbar toolbar;
    private final static int TYPE_REGISTER_STEP1 = 1;

    public static void startRegisterActivity(Activity activity) {
        Intent intent = new Intent();
        intent.setClass(activity, RegisterActivity.class);
        activity.startActivity(intent);
        //activity.finish();
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        toolbar = (Toolbar)findViewById(R.id.tbRegister);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.register_title);

        final AppCompatEditText edtUser = (AppCompatEditText)findViewById(R.id.edtRegisterID);
        final AppCompatEditText edtPassword = (AppCompatEditText)findViewById(R.id.edtRegisterPassword);

        findViewById(R.id.btnRegisterNoMask).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatEditText edtPassword = (AppCompatEditText)findViewById(R.id.edtRegisterPassword);
                if (edtPassword.getInputType() == (EditorInfo.TYPE_TEXT_VARIATION_PASSWORD | EditorInfo.TYPE_CLASS_TEXT)) {
                    edtPassword.setInputType(EditorInfo.TYPE_CLASS_TEXT | EditorInfo.TYPE_TEXT_VARIATION_NORMAL);
                }
                else {
                    edtPassword.setInputType(EditorInfo.TYPE_CLASS_TEXT | EditorInfo.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });

        findViewById(R.id.btnRegister).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userID = edtUser.getText().toString().trim();
                String password = edtPassword.getText().toString().trim();
                if (userID.equals("") || password.equals("")) {
                    Toast.makeText(RegisterActivity.this, "请输入正确的注册号码和密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                RequestHelper.register(true,userID, LoginActivity.AREA_CODE, LoginActivity.NATIONAL_CODE, new RegisterRequestHandler(RegisterActivity.this, TYPE_REGISTER_STEP1));
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()){
//            case android.R.id.home: {
//                LoginActivity.startLoginActivity(this);
//                return true;
//            }
//        }
        return super.onOptionsItemSelected(item);
    }

    class RegisterRequestHandler extends RequestHandler {
        private int mType = 0;

        public RegisterRequestHandler(Context context, int type) {
            super(context);
            mType = type;
        }

        @Override
        public void handleSuccess(Message msg) {
            switch (mType) {
                case TYPE_REGISTER_STEP1:
                    Log.v(TAG, msg.toString());
                    break;

            }
        }
    }
}
