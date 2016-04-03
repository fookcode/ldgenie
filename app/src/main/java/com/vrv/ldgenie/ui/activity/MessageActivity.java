package com.vrv.ldgenie.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ListViewCompat;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;

import com.vrv.imsdk.SDKManager;
import com.vrv.imsdk.model.ChatMsg;
import com.vrv.imsdk.model.ChatMsgList;
import com.vrv.ldgenie.R;
import com.vrv.ldgenie.adapter.MessageAdapter;
import com.vrv.ldgenie.bpo.GenieRequestHandler;
import com.vrv.ldgenie.common.sdk.action.RequestHelper;
import com.vrv.ldgenie.common.sdk.utils.BaseInfoBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kinee on 2016/3/31.
 */
public class MessageActivity extends AppCompatActivity {
    private static final String ID_USER_INFO="USER_INFO";

    private Toolbar toolbarMessage;
    private BaseInfoBean userInfo;
    private List<ChatMsg> chatMsgQueue = new ArrayList<>();
    private ChatMsgList chatMsgList;
    private ListViewCompat lvMessage;
    private MessageAdapter messageAdapter;

    public static void startMessageActivity(Activity activity, BaseInfoBean bean) {
        Intent intent = new Intent();
        intent.putExtra(ID_USER_INFO, bean);
        intent.setClass(activity, MessageActivity.class);
        activity.startActivity(intent);
        activity.finish();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        userInfo = (BaseInfoBean) getIntent().getParcelableExtra(MessageActivity.ID_USER_INFO);

        initToolbar();
        initMessageData();

    }

    private void initToolbar() {
        toolbarMessage = (Toolbar)findViewById(R.id.toolbarMessage);
        setSupportActionBar(toolbarMessage);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(userInfo.getName());

    }

    private void initMessageData() {
        messageAdapter = new MessageAdapter(MessageActivity.this, chatMsgQueue);
        lvMessage = (ListViewCompat)findViewById(R.id.listMessage);
        lvMessage.setAdapter(messageAdapter);

        chatMsgList = SDKManager.instance().getChatMsgList();
        chatMsgList.setReceiveListener(userInfo.getID(), new ChatMsgList.OnReceiveChatMsgListener() {
            @Override
            public void onReceive(ChatMsg msg) {
                addChatMsg(msg);
            }

            @Override
            public void onUpdate(ChatMsg msg) {
                updateMsgStatus(msg);
            }
        });

        final AppCompatButton btnSendMessage = (AppCompatButton)findViewById(R.id.btnSendMessage);
        final AppCompatEditText edtMessage = (AppCompatEditText)findViewById(R.id.edtMessage);

        btnSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt = edtMessage.getText().toString();
                if (!txt.isEmpty()) {
                    RequestHelper.sendTxt(userInfo.getID(), txt, null, new GenieRequestHandler(GenieRequestHandler.HANDLER_SEND_MESSAGE, MessageActivity.this));
                    edtMessage.getText().clear();
                }
            }
        });
    }

    private void addChatMsg(ChatMsg chatMsg) {
        if (chatMsg == null) return;
        if((chatMsg.getTargetID() == userInfo.getID()) && (chatMsgQueue != null)) {
            chatMsgQueue.add(chatMsg);
            messageAdapter.notifyDataSetChanged();
        }
    }

    private void updateMsgStatus(ChatMsg chatMsg) {
        if (chatMsg == null) {
            return;
        }
        int size = chatMsgQueue.size();
        for (int i = size - 1; i >= 0; i--) {
            if (chatMsg.getLocalID() == chatMsgQueue.get(i).getLocalID()) {
                chatMsgQueue.set(i, chatMsg);
                messageAdapter.notifyDataSetChanged();
                return;
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                //NavUtils.navigateUpFromSameTask(this);
                MainActivity.startMainActivity(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
