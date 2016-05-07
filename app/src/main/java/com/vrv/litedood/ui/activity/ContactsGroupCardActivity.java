package com.vrv.litedood.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.vrv.litedood.R;
import com.vrv.litedood.common.sdk.action.RequestHandler;
import com.vrv.litedood.common.sdk.action.RequestHelper;

/**
 * Created by kinee on 2016/5/7.
 */
public class ContactsGroupCardActivity extends AppCompatActivity {
    private final static String TAG = ContactsGroupCardActivity.class.getSimpleName();
    private final static String ID_GROUP_ID = "CONTACT_GROUP_CARD_GROUPID";

    public static void startContactGroupCardActivity(Context context, long groupid) {
        Intent intent = new Intent();
        intent.putExtra(ID_GROUP_ID, groupid);
        intent.setClass(context, ContactsGroupCardActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_group_card);

        Toolbar toolbar = (Toolbar)findViewById(R.id.tbContactsGroupCard);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        RequestHelper.getGroupInfo(getIntent().getLongExtra(ID_GROUP_ID, 0), new ContactsGroupCardHandler());
    }

    class ContactsGroupCardHandler extends RequestHandler {
        @Override
        public void handleSuccess(Message msg) {
            Log.v(TAG, msg.toString());
        }
    }
}
