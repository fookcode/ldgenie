package com.vrv.litedood.ui.activity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.ListViewCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.vrv.imsdk.SDKManager;
import com.vrv.imsdk.model.Chat;
import com.vrv.imsdk.model.ChatMsg;
import com.vrv.imsdk.model.ChatMsgList;
import com.vrv.imsdk.model.ItemModel;
import com.vrv.litedood.R;
import com.vrv.litedood.adapter.MessageAdapter;
import com.vrv.litedood.common.sdk.action.RequestHandler;
import com.vrv.litedood.common.sdk.action.RequestHelper;
import com.vrv.litedood.common.sdk.utils.BaseInfoBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kinee on 2016/3/31.
 */
public class MessageActivity extends AppCompatActivity {
    private static final String TAG = MessageActivity.class.getSimpleName();

    private static final String ID_USER_ID = "USER_ID";
    private static final String ID_USER_NAME = "USER_NAME";
    private static final String ID_LAST_MESSAGE_ID = "LAST_MESSAGE_ID";
    private static final String ID_UNREAD_MESSAGE_NUMBER = "UNREAD_NUMBER";

    private static final int TYPE_GET_HISTORY_MESSAGE = 1;
    private static final int TYPE_SEND_MESSAGE = 2;

    private static final int DEFAULT_MESSAGE_COUNT = 15;

    private Toolbar toolbarMessage;
//    private BaseInfoBean userInfo;
    private List<ChatMsg> chatMsgQueue = new ArrayList<>();
    private ChatMsgList chatMsgList;
    private ListViewCompat lvMessage;
    private MessageAdapter messageAdapter;

    private ContentResolver resolver;

    public static <T extends ItemModel> void startMessageActivity(Activity activity, T item) {
        Intent intent = new Intent();
        //intent.putExtra(ID_USER_INFO, BaseInfoBean.chat2BaseInfo(chat));
        intent.putExtra(ID_USER_ID, item.getId());
        intent.putExtra(ID_USER_NAME, item.getName());
        if (item instanceof Chat) {
            intent.putExtra(ID_LAST_MESSAGE_ID, ((Chat)item).getLastMsgID());
            intent.putExtra(ID_UNREAD_MESSAGE_NUMBER, ((Chat)item).getUnReadNum());
        }
        intent.setClass(activity, MessageActivity.class);
        activity.startActivity(intent);
        if (!(activity instanceof MainActivity)) {
            activity.finish();
        }
    }

//    public static void startMessageActivity(Context context, BaseInfoBean bean) {
//        Intent intent = new Intent();
//        intent.putExtra(ID_USER_INFO, bean);
//        intent.setClass(context, MessageActivity.class);
//        context.startActivity(intent);
//        if (!(context instanceof MainActivity)) {
//            if (context instanceof AppCompatActivity)
//                ((AppCompatActivity)context).finish();
//        }
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

//        userInfo = getIntent().getParcelableExtra(MessageActivity.ID_USER_INFO);
        resolver = getContentResolver();

        initToolbar();
        initMessageData();

    }

    private void initToolbar() {
        toolbarMessage = (Toolbar)findViewById(R.id.toolbarMessage);
        setSupportActionBar(toolbarMessage);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(getIntent().getStringExtra(ID_USER_NAME));

    }

    private void initMessageData() {

        messageAdapter = new MessageAdapter(MessageActivity.this, chatMsgQueue);
        lvMessage = (ListViewCompat)findViewById(R.id.listMessage);
        lvMessage.setAdapter(messageAdapter);

        chatMsgList = SDKManager.instance().getChatMsgList();
        chatMsgList.setReceiveListener(getIntent().getLongExtra(ID_USER_ID, 0), new ChatMsgList.OnReceiveChatMsgListener() {
            @Override
            public void onReceive(ChatMsg msg) {
                if (msg == null) return;
                if((msg.getTargetID() == getIntent().getLongExtra(ID_USER_ID, 0)) && (chatMsgQueue != null)) {
                    //Log.v(TAG, "in add, MsgQueueSize: " + String.valueOf(chatMsgQueue.size()));
                    chatMsgQueue.add(msg);
                    RequestHelper.setMsgRead(msg.getTargetID(), msg.getMessageID());
                    messageAdapter.notifyDataSetChanged();
                    //saveMessageToDB(chatMsg);
                }
            }

            @Override
            public void onUpdate(ChatMsg msg) {

                if (msg == null) {
                    return;
                }
                int size = chatMsgQueue.size();
                Log.v(TAG, "in Update, MsgQueueSize: " + String.valueOf(size));
                for (int i = size - 1; i >= 0; i--) {
                    if (msg.getLocalID() == chatMsgQueue.get(i).getLocalID()) {
                        chatMsgQueue.set(i, msg);
                        messageAdapter.notifyDataSetChanged();
                        return;
                    }
                }
            }
        });

        //setMessageHistory(userInfo.getID(), getIntent().getLongExtra(ID_LAST_MESSAGE_ID, -1));
        //获取未读记录
        int count = getIntent().getIntExtra(ID_UNREAD_MESSAGE_NUMBER, 0);
        RequestHelper.getChatHistory(getIntent().getLongExtra(ID_USER_ID, 0),
                0,
                getShowedMessageCount(count),
                new MessageRequestHandler(TYPE_GET_HISTORY_MESSAGE));

        final AppCompatButton btnSendMessage = (AppCompatButton)findViewById(R.id.btnSendMessage);
        final AppCompatEditText edtMessage = (AppCompatEditText)findViewById(R.id.edtMessage);

        btnSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt = edtMessage.getText().toString();
                if (!txt.isEmpty()) {
                    RequestHelper.sendTxt(getIntent().getLongExtra(ID_USER_ID, 0), txt, null, new MessageRequestHandler(TYPE_SEND_MESSAGE));
                    edtMessage.getText().clear();
                }
            }
        });
    }

    private int getShowedMessageCount(int unReadCount) {
        return unReadCount == 0 ? DEFAULT_MESSAGE_COUNT : unReadCount;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //清除消息点对点接收监听函数，否则此Activity就算消毁，过程依然继续后台执行
        chatMsgList.setReceiveListener(-99, new ChatMsgList.OnReceiveChatMsgListener(){
            @Override
            public void onReceive(ChatMsg chatMsg) {

            }

            @Override
            public void onUpdate(ChatMsg chatMsg) {

            }
        });
    }

    /*private void setMessageHistory(long targetID) {
        String uriString = LiteDoodMessageProvider.SCHEME + LiteDoodMessageProvider.AUTHORITY + "/" + MessageDTO.TABLE_NAME;
        Uri uri = Uri.parse(uriString);
        Log.v(TAG, "receive ID: " + String.valueOf(receiveID));
        Cursor temp = resolver.query(uri, MessageDTO.getAllColumns(), null, null, null);
        ArrayList<ChatMsg> tempList = MessageDTO.toChatMsg(temp);


        Cursor chatMsgHistory = resolver.query(uri,
                MessageDTO.getAllColumns(),
                MessageDTO.TABLE_MESSAGE_COLUMN_RECEIVEID
                    + "=? or "
                    + MessageDTO.TABLE_MESSAGE_COLUMN_SENDID
                    + "=? ", new String[] {String.valueOf(targetID), String.valueOf(targetID)},
                null);
        ArrayList<ChatMsg> oldMsg = MessageDTO.toChatMsg(chatMsgHistory);

        for(ChatMsg msg : oldMsg) {
            Log.v(TAG, msg.toString() + " id:" + msg.getId());
        }
        if ((oldMsg != null) && (oldMsg.size() > 0)) {
            chatMsgQueue.addAll(oldMsg);
            messageAdapter.notifyDataSetChanged();
        };
    }*/

//    private void saveMessageToDB(ChatMsg chatMsg) {
//
//        String uriString = LiteDood.URI + "/" + MessageDTO.TABLE_NAME;
//        Uri insertUri = Uri.parse(uriString);
//        resolver.insert(insertUri, MessageDTO.convertChatMessage(chatMsg));
//    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                MainActivity.startMainActivity(this);
//                return true;
//        }
        return super.onOptionsItemSelected(item);
    }

    class MessageRequestHandler extends RequestHandler {
        private int nType;

        public MessageRequestHandler(int type) {
            this.nType = type;
        }
        @Override
        public void handleSuccess(Message msg) {
            switch (nType){
                case TYPE_GET_HISTORY_MESSAGE:
                    ArrayList<ChatMsg> chatMsgArray = msg.getData().getParcelableArrayList("data");
                    if (chatMsgArray.size() > 0) {
                        chatMsgQueue.clear();
                        chatMsgQueue.addAll(chatMsgArray);
                        messageAdapter.notifyDataSetChanged();
                        ChatMsg lastMsg = chatMsgArray.get(chatMsgArray.size()-1);
                        RequestHelper.setMsgRead(lastMsg.getTargetID(), lastMsg.getMessageID());
                        lvMessage.setSelection(chatMsgArray.size() -1);
                    }
                    break;
                case TYPE_SEND_MESSAGE:

                    break;


            }
        }
    }
}
