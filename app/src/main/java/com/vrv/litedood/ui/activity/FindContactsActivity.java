package com.vrv.litedood.ui.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.ListViewCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

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
        toolbar.setNavigationIcon(R.drawable.ic_back);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        AppCompatButton btnFindAction = (AppCompatButton)findViewById(R.id.btnFindContactsAction);
        final ListViewCompat lvFindContactsResult = (ListViewCompat)findViewById(R.id.lvFindContactsResult);
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
                    setFindBusy(true);
                    lvFindContactsResult.setAdapter(null);
                    try {
                        RequestHelper.searchNet(code, new FindContactsRequestHandler());
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                        setFindBusy(false);
                    }
                }
            }
        });
    }

    private void setFindBusy(boolean isBusy) {
        ContentLoadingProgressBar clpbBusyIndicator = (ContentLoadingProgressBar)findViewById(R.id.clpbFindContactsBusyIndicator);
        if (isBusy) {
            clpbBusyIndicator.setVisibility(View.VISIBLE);
        }
        else {
            clpbBusyIndicator.setVisibility(View.GONE);
        }
    }

    class FindContactsRequestHandler extends RequestHandler {
        @Override
        public void handleSuccess(Message msg) {
            ArrayList<ItemModel> contacts = (ArrayList)msg.getData().getParcelableArrayList("data");
            final ListViewCompat lvFindContactsResult = (ListViewCompat)findViewById(R.id.lvFindContactsResult);
            if((contacts != null) && (contacts.size()>0)) {
                ItemModelSelectorAdapter<ItemModel> adapter = new ItemModelSelectorAdapter<>(FindContactsActivity.this, contacts);
                lvFindContactsResult.setAdapter(adapter);
                lvFindContactsResult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        final ItemModel item = (ItemModel) lvFindContactsResult.getItemAtPosition(position);

                        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(FindContactsActivity.this);
                        dialogBuilder.setTitle("添加好友");
                        dialogBuilder.setMessage("向 " + item.getName() + " 发送加友请求：");
                        dialogBuilder.setView(R.layout.dialog_confirm_add_contacts);
                        dialogBuilder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialogBuilder.getContext().toString();
                                RequestHelper.addContact(item.getId(), "VerifyText", "Remark", new AddContactsHandler(FindContactsActivity.this));
                            }
                        });
                        dialogBuilder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                              dialog.dismiss();
                            }
                        });
                        AlertDialog dialog =  dialogBuilder.create();
                        dialog.show();
//                        if (dialog.)
//
                    }
                });
            }
            else {
                lvFindContactsResult.setAdapter(null);
                lvFindContactsResult.setOnItemClickListener(null);
                Toast.makeText(FindContactsActivity.this, "没有找到相关的信源豆豆账号信息",Toast.LENGTH_SHORT).show();
                //lvFindContactsResult.removeAllViews();
            }

            setFindBusy(false);
        }

        @Override
        public void handleFailure(int code, String message) {
            super.handleFailure(code, message);
            setFindBusy(false);
        }
    }

    class AddContactsHandler extends RequestHandler {

        public AddContactsHandler(Context context) {
            //super(context);
        }

        @Override
        public void handleSuccess(Message msg) {
           Toast.makeText(FindContactsActivity.this, "加友请求发送成功，请等待对方响应", Toast.LENGTH_SHORT).show();
        }
    }
}
