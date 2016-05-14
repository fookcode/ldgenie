package com.vrv.litedood.adapter;

import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.vrv.imsdk.SDKManager;
import com.vrv.imsdk.api.ChatMsgApi;
import com.vrv.imsdk.model.ChatMsg;
import com.vrv.litedood.R;
import com.vrv.litedood.common.LiteDood;
import com.vrv.litedood.ui.activity.MainFragment.ContactsFragment;
import com.vrv.litedood.ui.activity.MessageActivity;

import java.util.List;

/**
 * Created by kinee on 2016/3/31.
 */
public class MessageAdapter extends BaseAdapter {
    private static String TAG = MessageAdapter.class.getSimpleName();

    private static final int MESSAGE_IN = 1;
    private static final int MESSAGE_OUT = 2;

    private MessageActivity mMessageActivity;
    private List<ChatMsg> chatMsgList;

    public MessageAdapter(MessageActivity mMessageActivity, List<ChatMsg> chatMsgList) {
        this.mMessageActivity = mMessageActivity;
        this.chatMsgList = chatMsgList;
    }

    @Override
    public int getCount() {
        int result = 0;
        if (chatMsgList != null)
            result = chatMsgList.size();
        return result;
    }

    @Override
    public Object getItem(int position) {
        if(chatMsgList != null) return chatMsgList.get(position);
        else return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        ChatMsg chatMsg = chatMsgList.get(position);
        long id = chatMsg.getSendID() == 0?chatMsg.getId():chatMsg.getSendID();

        return  SDKManager.instance().getAuth().isMyself(id) ? MESSAGE_OUT : MESSAGE_IN;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ChatMsg chatMsg = chatMsgList.get(position);
        Log.v(TAG, chatMsg == null?"":chatMsg.toString() + chatMsg.getMessage());
        int type = getItemViewType(position);
        ViewHolder viewHolder = null;
        String msg;

        convertView = null;
        switch (type) {
            case MESSAGE_IN: {
                convertView = LayoutInflater.from(mMessageActivity).inflate(R.layout.item_message_in, null);
                viewHolder = new ViewHolder();
                viewHolder.tvMessage = (AppCompatTextView)convertView.findViewById(R.id.tvMessageIn);
                viewHolder.imgAvatar = (AppCompatImageView)convertView.findViewById(R.id.imgMessageInAvatar);
                viewHolder.tvName = (AppCompatTextView)convertView.findViewById(R.id.tvMessageInName);
                break;
            }
            case MESSAGE_OUT: {
                convertView = LayoutInflater.from(mMessageActivity).inflate(R.layout.item_message_out, null);
                viewHolder = new ViewHolder();
                viewHolder.tvMessage = (AppCompatTextView)convertView.findViewById(R.id.tvMessageOut);
                viewHolder.imgAvatar = (AppCompatImageView)convertView.findViewById(R.id.imgMessageOutAvatar);
                viewHolder.tvName = (AppCompatTextView)convertView.findViewById(R.id.tvMessageOutName);
                break;
            }
        }

        viewHolder.imgAvatar.setImageBitmap(LiteDood.getAvatarBitmap(chatMsg.getAvatar()));

        switch (mMessageActivity.getIntent().getIntExtra(MessageActivity.ID_MESSAGE_TYPE, 0)) {
            case MessageActivity.TYPE_MESSAGE_GROUP:
                String name = chatMsg.getName().trim();
                if ((name.equals("") || name.equals("0"))) {
                    name = MessageActivity.getMemberName(chatMsg.getSendID());
                }
                if (!name.equals("")) {
                    viewHolder.tvName.setText(name);
                }
                else {
                    viewHolder.tvName.setVisibility(View.GONE);
                }
                break;
            case MessageActivity.TYPE_MESSAGE_CHAT:
                viewHolder.tvName.setVisibility(View.GONE);
                break;
            default:
                viewHolder.tvName.setVisibility(View.GONE);
                break;
        }

        switch (chatMsg.getMessageType()) {
            case ChatMsgApi.TYPE_TEXT:
                msg = ChatMsgApi.parseTxtJson(chatMsg.getMessage());
                break;
            case ChatMsgApi.TYPE_IMAGE:
                msg = "[图片]";
                break;
            case ChatMsgApi.TYPE_FILE:
                msg = "[文件]";
                break;
            case ChatMsgApi.TYPE_AUDIO:
                msg = "[音频]";
                break;
            case ChatMsgApi.TYPE_CARD:
                msg = "[名片]";
                break;
            case ChatMsgApi.TYPE_HTML:
                msg = "[网页]";
                break;
            case ChatMsgApi.TYPE_POSITION:
                msg = "[位置]";
                break;
            case ChatMsgApi.TYPE_MULTI:
                msg = "[组合消息]";
                break;
            default:
                msg = "[未知消息]";
                break;
        }
        viewHolder.tvMessage.setText(msg);

        return convertView;
    }

    class ViewHolder {
        AppCompatImageView imgAvatar;
        AppCompatTextView tvMessage;
        AppCompatTextView tvName;
        public ViewHolder() {};
    }


}

