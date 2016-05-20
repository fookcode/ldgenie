package com.vrv.litedood.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.ListViewCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.vrv.imsdk.model.Contact;
import com.vrv.litedood.R;
import com.vrv.litedood.adapter.ContactCardAdapter;
import com.vrv.litedood.common.sdk.action.RequestHandler;
import com.vrv.litedood.common.sdk.action.RequestHelper;
import com.vrv.litedood.common.sdk.utils.BaseInfoBean;

/**
 * Created by kinee on 2016/4/30.
 */
public class ContactsCardActivity extends AppCompatActivity {
    public static final String TAG = ContactsCardActivity.class.getSimpleName();
    public static final String ID_CONTACT = "CONTACT";

    private BaseInfoBean mContactBean;
    private Contact mContact;
    private ContactCardAdapter mContactCardAdapter;

    public static void startContactCardActivity(Activity activity, BaseInfoBean contact, String action) {
        Intent intent = new Intent();
        intent.putExtra(ID_CONTACT, contact);
        intent.setAction(action);
        intent.setClass(activity, ContactsCardActivity.class);
        activity.startActivity(intent);
        if (!(activity instanceof MainActivity)) {
            activity.finish();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContactBean = (BaseInfoBean)getIntent().getParcelableExtra(ID_CONTACT);
        ContactCardActivityHandler handler = new ContactCardActivityHandler(this);

        handler.sendEmptyMessage(RequestHandler.SHOW_PRO);

        try {
            if (!RequestHelper.getUserInfo(mContactBean.getID(),  handler)) {
                Toast.makeText(this, "好友信息获取失败，请稍后重试", Toast.LENGTH_SHORT);
                handler.sendEmptyMessage(RequestHandler.DIS_PRO);
                this.finish();
                return;
            }
        } catch (Exception e) {
            handler.sendEmptyMessage(RequestHandler.DIS_PRO);
            e.printStackTrace();
        } finally {
            handler.sendEmptyMessage(RequestHandler.DIS_PRO);
        }


    }

    class ContactCardActivityHandler extends RequestHandler {
        Activity mActivity;
        public ContactCardActivityHandler(Activity activity) {
            super(activity);
            this.mActivity = activity;

        }

        @Override
        public void handleSuccess(Message msg) {
            mContact = (Contact)msg.getData().get("data");
            //Log.v(TAG, mContact.toString());
            setContentView(R.layout.activity_contacts_card);

            Toolbar tbContactCard = (Toolbar) findViewById(R.id.tbContactCard);
            tbContactCard.setNavigationIcon(R.drawable.ic_back);
            setSupportActionBar(tbContactCard);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            ListViewCompat lvContactCardItemList = (ListViewCompat)findViewById(R.id.lvContactCardItemList);
            lvContactCardItemList.setAdapter(new ContactCardAdapter(ContactsCardActivity.this, mContact));

            String action = mActivity.getIntent().getAction();
            if (Intent.ACTION_EDIT.equals(action)) {
                findViewById(R.id.btnContactCardSendMessage).setVisibility(View.GONE);
            }
            else if (Intent.ACTION_VIEW.equals(action)) {
                AppCompatButton sendMessage = (AppCompatButton) findViewById(R.id.btnContactCardSendMessage);
                sendMessage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MessageActivity.startMessageActivity(ContactsCardActivity.this, mContact);
                    }
                });
            }

        }
    }
}
