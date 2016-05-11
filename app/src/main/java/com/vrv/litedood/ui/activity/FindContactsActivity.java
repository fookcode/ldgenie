package com.vrv.litedood.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.ListViewCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.vrv.imsdk.SDKManager;
import com.vrv.imsdk.model.ItemModel;
import com.vrv.litedood.R;
import com.vrv.litedood.adapter.ItemModelSelectorAdapter;
import com.vrv.litedood.common.sdk.action.RequestHandler;
import com.vrv.litedood.common.sdk.action.RequestHelper;

import java.util.ArrayList;

/**
 * Created by kinee on 2016/5/10.
 */
public class FindContactsActivity extends AppCompatActivity {
    private static final String TAG = FindContactsActivity.class.getSimpleName();

    public static void startFindContactsActivity(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, FindContactsActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_contacts);

        Toolbar toolbar = (Toolbar)findViewById(R.id.tbFindContacts);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        AppCompatButton btnFindAction = (AppCompatButton)findViewById(R.id.btnFindContactsAction);
        btnFindAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatEditText etFindContactsCode = (AppCompatEditText)findViewById(R.id.etFindContactsCode);
                String code = etFindContactsCode.getText().toString().trim();
                if (code.equals("")) {
                    ListViewCompat lvFindContactsResult = (ListViewCompat)findViewById(R.id.lvFindContactsResult);
                    //lvFindContactsResult.removeAllViews();
                    lvFindContactsResult.setAdapter(null);
                    Toast.makeText(FindContactsActivity.this, "请输入正确的手机号/豆豆号", Toast.LENGTH_SHORT).show();
                }
                else {
                    RequestHelper.searchNet(code, new FindContactsRequestHandler());
                }
            }
        });
    }

    class FindContactsRequestHandler extends RequestHandler {
        @Override
        public void handleSuccess(Message msg) {
            ArrayList<ItemModel> contacts = (ArrayList)msg.getData().getParcelableArrayList("data");
            ListViewCompat lvFindContactsResult = (ListViewCompat)findViewById(R.id.lvFindContactsResult);
            if((contacts != null) && (contacts.size()>0)) {
                ItemModelSelectorAdapter<ItemModel> adapter = new ItemModelSelectorAdapter<>(FindContactsActivity.this, contacts);
                lvFindContactsResult.setAdapter(adapter);
            }
            else {
                lvFindContactsResult.setAdapter(null);
                Toast.makeText(FindContactsActivity.this, "没有找到相关的信源豆豆账号信息",Toast.LENGTH_SHORT).show();
                //lvFindContactsResult.removeAllViews();
            }
            Log.v(TAG, String.valueOf(contacts.size()));

        }
    }
}
