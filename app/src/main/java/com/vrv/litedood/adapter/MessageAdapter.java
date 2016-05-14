package com.vrv.litedood.adapter;

import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.text.format.Time;
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
import com.vrv.litedood.ui.activity.MessageActivity;

import java.util.List;

/**
 * Created by kinee on 2016/3/31.
 */
public class MessageAdapter extends BaseAdapter {
    private static String TAG = MessageAdapter.class.getSimpleName();

    private static final int MESSAGE_IN = 1;
    private static final int MESSAGE_OUT = 2;

    private static final int TIME_INTERVAL = 15;       //间隔时间大于15分钟的消息显示一个时间弱提示

    private MessageActivity mMessageActivity;
    private List<ChatMsg> chatMsgList;

    private long mMessageTime = 0;

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
        int direction = getItemViewType(position);
        String msg;

        switch (chatMsg.getMessageType()) {
            case ChatMsgApi.TYPE_HTML:
                msg = "[网页]";
                break;
            case ChatMsgApi.TYPE_TEXT:
                convertView = inflateTextView(ChatMsgApi.TYPE_TEXT, direction, convertView);
                msg = ChatMsgApi.parseTxtJson(chatMsg.getMessage());
                ((ViewHolder)convertView.getTag()).mMsgType = ChatMsgApi.TYPE_TEXT;
                ((ViewHolder)convertView.getTag()).tvMessage.setText(msg);
                break;
            case ChatMsgApi.TYPE_AUDIO:
                msg = "[音频]";
                convertView = inflateTextView(ChatMsgApi.TYPE_TEXT, direction, convertView);
                ((ViewHolder)convertView.getTag()).tvMessage.setText(msg);
                break;
            case ChatMsgApi.TYPE_POSITION:
                msg = "[位置]";
                convertView = inflateTextView(ChatMsgApi.TYPE_TEXT, direction, convertView);
                ((ViewHolder)convertView.getTag()).tvMessage.setText(msg);
                break;
            case ChatMsgApi.TYPE_IMAGE:
                msg = "[图片]";
                convertView = inflateTextView(ChatMsgApi.TYPE_TEXT, direction, convertView);
                ((ViewHolder)convertView.getTag()).tvMessage.setText(msg);
                break;
            case ChatMsgApi.TYPE_FILE:
                msg = "[文件]";
                convertView = inflateTextView(ChatMsgApi.TYPE_TEXT, direction, convertView);
                ((ViewHolder)convertView.getTag()).tvMessage.setText(msg);
                break;
            case ChatMsgApi.TYPE_CARD:
                msg = "[名片]";
                convertView = inflateTextView(ChatMsgApi.TYPE_TEXT, direction, convertView);
                ((ViewHolder)convertView.getTag()).tvMessage.setText(msg);
                break;
            case ChatMsgApi.TYPE_WEAK_HINT:
                //msg = "[弱提示]";
                msg = ChatMsgApi.parseTxtJson(chatMsg.getMessage());
                if (msg.equals("")) msg = "[弱提示]";
                convertView = inflateWeakHintView(ChatMsgApi.TYPE_WEAK_HINT, convertView);
                ((ViewHolder)convertView.getTag()).tvMessage.setText(msg);
                break;
            case ChatMsgApi.TYPE_TO_APP:
                msg = "[TYPE_TO_APP]";
                convertView = inflateTextView(ChatMsgApi.TYPE_TEXT, direction, convertView);
                ((ViewHolder)convertView.getTag()).tvMessage.setText(msg);
                break;
            case ChatMsgApi.TYPE_APP_PUSH:
                msg = "[TYPE_APP_PUSH]";
                convertView = inflateTextView(ChatMsgApi.TYPE_TEXT, direction, convertView);
                ((ViewHolder)convertView.getTag()).tvMessage.setText(msg);
                break;
            case ChatMsgApi.TYPE_INSTRUCTION:
                msg = "[TYPE_INSTRUCTION]";
                convertView = inflateTextView(ChatMsgApi.TYPE_TEXT, direction, convertView);
                ((ViewHolder)convertView.getTag()).tvMessage.setText(msg);
                break;
            case ChatMsgApi.TYPE_NEWS:
                msg = "[TYPE_NEWS]";
                convertView = inflateTextView(ChatMsgApi.TYPE_TEXT, direction, convertView);
                ((ViewHolder)convertView.getTag()).tvMessage.setText(msg);
                break;
            case ChatMsgApi.TYPE_MULTI:
                msg = "[组合消息]";
                convertView = inflateTextView(ChatMsgApi.TYPE_TEXT, direction, convertView);
                ((ViewHolder)convertView.getTag()).tvMessage.setText(msg);
                break;
            case ChatMsgApi.MESSAGE_TYPE_VIDEO:
                msg = "[视频]";
                convertView = inflateTextView(ChatMsgApi.TYPE_TEXT, direction, convertView);
                ((ViewHolder)convertView.getTag()).tvMessage.setText(msg);
                break;
            case ChatMsgApi.MESSAGE_TYPE_VOICE:
                msg = "[音频]";
                convertView = inflateTextView(ChatMsgApi.TYPE_TEXT, direction, convertView);
                ((ViewHolder)convertView.getTag()).tvMessage.setText(msg);
                break;
            case ChatMsgApi.TYPE_TASK:
                msg = "[任务]";
                convertView = inflateTextView(ChatMsgApi.TYPE_TEXT, direction, convertView);
                ((ViewHolder)convertView.getTag()).tvMessage.setText(msg);
                break;
            case ChatMsgApi.TYPE_REVOKE:
                msg = "[TYPE_REVOKE]";
                convertView = inflateTextView(ChatMsgApi.TYPE_TEXT, direction, convertView);
                ((ViewHolder)convertView.getTag()).tvMessage.setText(msg);
                break;
            case ChatMsgApi.TYPE_DYNAMIC:
                msg = "[TYPE_DYNAMIC]";
                convertView = inflateTextView(ChatMsgApi.TYPE_TEXT, direction, convertView);
                ((ViewHolder)convertView.getTag()).tvMessage.setText(msg);
                break;
            case ChatMsgApi.TYPE_RED_ENVELOPE:
                msg = "[红包]";
                convertView = inflateTextView(ChatMsgApi.TYPE_TEXT, direction, convertView);
                ((ViewHolder)convertView.getTag()).tvMessage.setText(msg);
                break;
            default:
                msg = "[未知消息]";
                convertView = inflateTextView(ChatMsgApi.TYPE_TEXT, direction, convertView);
                ((ViewHolder)convertView.getTag()).tvMessage.setText(msg);
                break;
        }

        ViewHolder viewHolder = ((ViewHolder)convertView.getTag());
        //不是弱提示，那么肯定是对话，一定会有头像和名称组件
        if (chatMsg.getMessageType() != ChatMsgApi.TYPE_WEAK_HINT) {
            viewHolder.imgAvatar.setImageBitmap(LiteDood.getAvatarBitmap(chatMsg.getAvatar()));

            switch (mMessageActivity.getIntent().getIntExtra(MessageActivity.ID_MESSAGE_TYPE, 0)) {
                case MessageActivity.TYPE_MESSAGE_GROUP:              //群显示名称
                    String name = chatMsg.getName().trim();
                    if ((name.equals("") || name.equals("0"))) {
                        name = MessageActivity.getMemberName(chatMsg.getSendID());
                    }
                    if (!name.equals("")) {
                        viewHolder.tvName.setText(name);
                    } else {

                        viewHolder.tvName.setVisibility(View.GONE);
                    }
                    break;
                case MessageActivity.TYPE_MESSAGE_CHAT:              //点对点聊天不显示
                    viewHolder.tvName.setVisibility(View.GONE);
                    break;
                default:
                    viewHolder.tvName.setVisibility(View.GONE);
                    break;
            }
        }

        return convertView;
    }

    //注入普通文字聊天视图
    private View inflateTextView(int msgType, int msgDirection,View convertView) {
        if ((convertView != null) && (((ViewHolder)convertView.getTag()).mMsgDirection == msgDirection) &&(((ViewHolder)convertView.getTag()).mMsgType == msgType)) {
            return convertView;
        }
        else {
            ViewHolder viewHolder = new ViewHolder();
            switch (msgDirection) {
                case MESSAGE_IN: {
                    convertView = LayoutInflater.from(mMessageActivity).inflate(R.layout.item_message_in, null);
                    viewHolder = new ViewHolder();
                    viewHolder.tvMessage = (AppCompatTextView) convertView.findViewById(R.id.tvMessageIn);
                    viewHolder.imgAvatar = (AppCompatImageView) convertView.findViewById(R.id.imgMessageInAvatar);
                    viewHolder.tvName = (AppCompatTextView) convertView.findViewById(R.id.tvMessageInName);

                    break;
                }
                case MESSAGE_OUT: {
                    convertView = LayoutInflater.from(mMessageActivity).inflate(R.layout.item_message_out, null);
                    viewHolder = new ViewHolder();
                    viewHolder.tvMessage = (AppCompatTextView) convertView.findViewById(R.id.tvMessageOut);
                    viewHolder.imgAvatar = (AppCompatImageView) convertView.findViewById(R.id.imgMessageOutAvatar);
                    viewHolder.tvName = (AppCompatTextView) convertView.findViewById(R.id.tvMessageOutName);
                    viewHolder.mMsgDirection = MESSAGE_OUT;
                    break;
                }
            }
            viewHolder.mMsgDirection = msgDirection;
            viewHolder.mMsgType = msgType;
            convertView.setTag(viewHolder);
            return convertView;
        }

    }

    private View inflateWeakHintView(int msgType, View convertView) {
        if ((convertView != null) && (((ViewHolder)convertView.getTag()).mMsgType == msgType)) {
            return convertView;
        }
        else {
            ViewHolder viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mMessageActivity).inflate(R.layout.item_message_extra, null);
            viewHolder.tvMessage = (AppCompatTextView) convertView.findViewById(R.id.tvMessageExtra);
            viewHolder.mMsgType = msgType;
            convertView.setTag(viewHolder);

            return convertView;
        }
    }

    private boolean isShowTimeWeakHint(long time) {
        boolean result = false;
        if (mMessageTime == 0) {
            result = true;
            mMessageTime = time;
        }
        else {
            Time prevTime = new Time();
            prevTime.set(mMessageTime);
            Time curTime = new Time();
            curTime.set(time);

            if ((prevTime.year == curTime.year) && (prevTime.month == curTime.month) && (prevTime.monthDay == curTime.monthDay) && (prevTime.hour == curTime.hour)) {
                if ((curTime.minute = prevTime.minute) > TIME_INTERVAL) result = true;
            }
            else result = true;
        }
        return result;
    }

    class ViewHolder {
        int mMsgType = 0;
        int mMsgDirection = 0;
        AppCompatImageView imgAvatar;
        AppCompatTextView tvMessage;
        AppCompatTextView tvName;
        AppCompatTextView tvWeakHint;

        public ViewHolder() {};
    }


}

