package com.vrv.ldgenie.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.ListViewCompat;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.vrv.imsdk.SDKManager;
import com.vrv.imsdk.model.Chat;
import com.vrv.imsdk.model.ChatMsg;
import com.vrv.imsdk.model.ChatMsgList;
import com.vrv.ldgenie.R;
import com.vrv.ldgenie.adapter.MessageAdapter;
import com.vrv.ldgenie.bpo.GenieRequestHandler;
import com.vrv.ldgenie.common.sdk.action.RequestHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by kinee on 2016/3/31.
 */
public class MessageActivity extends AppCompatActivity {
    private static final String ID = "USER_IN_ID";
    private static final String NAME = "USER_IN_NAME";
    private static final String AVATAR_PATH = "AVATAR_AVATAR_PATH";

    private Toolbar toolbarMessage;
    private HashMap<String, String> userIn = new HashMap();
    private List<ChatMsg> chatMsgQueue = new ArrayList<>();
    private ChatMsgList chatMsgList;
    private ListViewCompat lvMessage;
    private MessageAdapter messageAdapter;

    public static void startMessageActivity(Activity activity, Chat chat) {
        Intent intent = new Intent();

        intent.putExtra(MessageActivity.ID, chat.getId());
        intent.putExtra(MessageActivity.NAME, chat.getName());
        intent.putExtra(MessageActivity.AVATAR_PATH, chat.getAvatar());

        intent.setClass(activity, MessageActivity.class);
        activity.startActivity(intent);
        activity.finish();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        Bundle bundle = getIntent().getExtras();
        userIn.put(MessageActivity.ID, String.valueOf(bundle.getLong(ID)));
        userIn.put(MessageActivity.NAME, bundle.getString(NAME));
        userIn.put(MessageActivity.AVATAR_PATH, bundle.getString(AVATAR_PATH));
        initToolbar();
        initMessageData();

    }

    private void initToolbar() {
        toolbarMessage = (Toolbar)findViewById(R.id.toolbarMessage);
        setSupportActionBar(toolbarMessage);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(userIn.get(NAME));

    }

    private void initMessageData() {
        messageAdapter = new MessageAdapter(MessageActivity.this, chatMsgQueue);
        lvMessage = (ListViewCompat)findViewById(R.id.lvMessage);
        lvMessage.setAdapter(messageAdapter);

        chatMsgList = SDKManager.instance().getChatMsgList();
        chatMsgList.setReceiveListener(Long.parseLong(userIn.get(ID)), new ChatMsgList.OnReceiveChatMsgListener() {
            @Override
            public void onReceive(ChatMsg msg) {
                addChatMsg(msg);
            }

            @Override
            public void onUpdate(ChatMsg msg) {
                updateMsgStatus(msg);
            }
        });

        final Button btnSendMessage = (Button)findViewById(R.id.btnSendMessage);
        final EditText edtMessage = (EditText)findViewById(R.id.edtMessage);

        btnSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt = edtMessage.getText().toString();
                if (!txt.isEmpty()) {
                    RequestHelper.sendTxt(Long.parseLong(userIn.get(ID)), txt, null, new GenieRequestHandler(GenieRequestHandler.HANDLER_SEND_MESSAGE, MessageActivity.this));
                    edtMessage.getText().clear();
                }
            }
        });
    }

    private void addChatMsg(ChatMsg chatMsg) {
        if (chatMsg == null) return;
        if((chatMsg.getTargetID() == Long.parseLong(userIn.get(ID))) && (chatMsgQueue != null)) {
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
