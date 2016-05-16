package com.vrv.litedood.adapter;

import android.graphics.Bitmap;
import android.os.Message;
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
import com.vrv.imsdk.api.ConfigApi;
import com.vrv.imsdk.api.MsgImage;
import com.vrv.imsdk.model.ChatMsg;
import com.vrv.imsdk.util.SDKFileUtils;
import com.vrv.litedood.R;
import com.vrv.litedood.common.LiteDood;
import com.vrv.litedood.common.sdk.action.RequestHandler;
import com.vrv.litedood.common.sdk.action.RequestHelper;
import com.vrv.litedood.ui.activity.MessageActivity;

import java.io.File;
import java.util.List;

/**
 * Created by kinee on 2016/3/31.
 */
public class MessageAdapter extends BaseAdapter {
    private static String TAG = MessageAdapter.class.getSimpleName();

    private static final int MESSAGE_IN = 1;
    private static final int MESSAGE_OUT = 2;

    private static final int TIME_INTERVAL = 15;       //间隔时间大于15分钟的消息显示一个时间弱提示

    private static final int TYPE_HANDLER_GET_PICTURE_THUMB = 1;

    private final MessageActivity mMessageActivity;
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
        int direction = getItemViewType(position);
        String msg;

        switch (chatMsg.getMessageType()) {
            case ChatMsgApi.TYPE_HTML:
                msg = "[网页]";
                break;
            case ChatMsgApi.TYPE_TEXT:
                if ((convertView != null) &&
                        ((convertView.getTag() instanceof TextMsgViewHolder)) &&
                        (((TextMsgViewHolder)convertView.getTag()).mMsgDirection == direction) &&
                        (((TextMsgViewHolder)convertView.getTag()).mMsgType == ChatMsgApi.TYPE_TEXT)) {
                    if (!(isShowTimeWeakHint(position) && (((TextMsgViewHolder) convertView.getTag()).tvTimeWeakHint != null))) {  //
                        convertView = inflateTextView(position, chatMsg);
                    }
                    else ((TextMsgViewHolder) convertView.getTag()).tvTimeWeakHint.setText(LiteDood.convertTimeForMessage(mMessageActivity, chatMsg.getSendTime()));  //重用
                }
                else {
                    convertView = inflateTextView(position, chatMsg);

                }

                msg = ChatMsgApi.parseTxtJson(chatMsg.getMessage());
                ((TextMsgViewHolder)convertView.getTag()).mMsgType = ChatMsgApi.TYPE_TEXT;
                ((TextMsgViewHolder)convertView.getTag()).tvMessage.setText(msg);
                break;
            case ChatMsgApi.TYPE_AUDIO:
                msg = "[音频]";
                convertView = inflateTextView(position, chatMsg);
                ((TextMsgViewHolder)convertView.getTag()).tvMessage.setText(msg);
                break;
            case ChatMsgApi.TYPE_POSITION:
                msg = "[位置]";
                convertView = inflateTextView(position, chatMsg);
                ((TextMsgViewHolder)convertView.getTag()).tvMessage.setText(msg);
                break;
            case ChatMsgApi.TYPE_IMAGE:
//                msg = "[图片]";

                if ((convertView != null) &&
                        ((convertView.getTag() instanceof ImageMsgViewHolder)) &&
                        (((ImageMsgViewHolder)convertView.getTag()).mMsgDirection == direction) &&
                        (((ImageMsgViewHolder)convertView.getTag()).mMsgType == ChatMsgApi.TYPE_IMAGE)) {
                    if (isShowTimeWeakHint(position)) {
                        ((ImageMsgViewHolder) convertView.getTag()).tvTimeWeakHint.setText(LiteDood.convertTimeForMessage(mMessageActivity, chatMsg.getSendTime()));  //重用
                        ((ImageMsgViewHolder) convertView.getTag()).tvTimeWeakHint.setVisibility(View.VISIBLE);
                    }
                    else {
                        ((ImageMsgViewHolder) convertView.getTag()).tvTimeWeakHint.setText("");
                        ((ImageMsgViewHolder) convertView.getTag()).tvTimeWeakHint.setVisibility(View.GONE);
                    }
                }
                else {
                    convertView = inflateImageView(position, chatMsg);

                }

                MsgImage image = ChatMsgApi.parseImgJson(chatMsg.getMessage());
                String imageName = ConfigApi.decryptFile(image.getEncDecKey(), image.getThumbShowPath());
                File file = new File(imageName);
                if (file.exists()) {
                    Bitmap picture = LiteDood.getBitmapFromFile(imageName);
                    ((ImageMsgViewHolder) convertView.getTag()).ivImageMessage.setImageBitmap(picture);
                }
                else {
                    RequestHelper.downloadThumbImg(chatMsg, new MessageAdapterRequestHandler(TYPE_HANDLER_GET_PICTURE_THUMB, ((ImageMsgViewHolder) convertView.getTag()).ivImageMessage));
                }

                break;
            case ChatMsgApi.TYPE_FILE:
                msg = "[文件]";
                convertView = inflateTextView(position, chatMsg);
                ((TextMsgViewHolder)convertView.getTag()).tvMessage.setText(msg);
                break;
            case ChatMsgApi.TYPE_CARD:
                msg = "[名片]";
                convertView = inflateTextView(position, chatMsg);
                ((TextMsgViewHolder)convertView.getTag()).tvMessage.setText(msg);
                break;
            case ChatMsgApi.TYPE_WEAK_HINT:
                msg = ChatMsgApi.parseTxtJson(chatMsg.getMessage());
//                ConfigApi.decryptFile()
                if (msg.equals("")) msg = "[弱提示]";
                convertView = inflateWeakHintView(ChatMsgApi.TYPE_WEAK_HINT, convertView);
                ((TextMsgViewHolder)convertView.getTag()).tvMessage.setText(msg);
                break;
            case ChatMsgApi.TYPE_TO_APP:
                msg = "[TYPE_TO_APP]";
                convertView = inflateTextView(position, chatMsg);
                ((TextMsgViewHolder)convertView.getTag()).tvMessage.setText(msg);
                break;
            case ChatMsgApi.TYPE_APP_PUSH:
                msg = "[TYPE_APP_PUSH]";
                ((TextMsgViewHolder)convertView.getTag()).tvMessage.setText(msg);
                break;
            case ChatMsgApi.TYPE_INSTRUCTION:
                msg = "[TYPE_INSTRUCTION]";
                convertView = inflateTextView(position, chatMsg);
                ((TextMsgViewHolder)convertView.getTag()).tvMessage.setText(msg);
                break;
            case ChatMsgApi.TYPE_NEWS:
                msg = "[TYPE_NEWS]";
                convertView = inflateTextView(position, chatMsg);
                ((TextMsgViewHolder)convertView.getTag()).tvMessage.setText(msg);
                break;
            case ChatMsgApi.TYPE_MULTI:
                msg = "[组合消息]";
                convertView = inflateTextView(position, chatMsg);
                ((TextMsgViewHolder)convertView.getTag()).tvMessage.setText(msg);
                break;
            case ChatMsgApi.MESSAGE_TYPE_VIDEO:
                msg = "[视频]";
                convertView = inflateTextView(position, chatMsg);
                ((TextMsgViewHolder)convertView.getTag()).tvMessage.setText(msg);
                break;
            case ChatMsgApi.MESSAGE_TYPE_VOICE:
                msg = "[音频]";
                convertView = inflateTextView(position, chatMsg);
                ((TextMsgViewHolder)convertView.getTag()).tvMessage.setText(msg);
                break;
            case ChatMsgApi.TYPE_TASK:
                msg = "[任务]";
                convertView = inflateTextView(position, chatMsg);
                ((TextMsgViewHolder)convertView.getTag()).tvMessage.setText(msg);
                break;
            case ChatMsgApi.TYPE_REVOKE:
                msg = "[TYPE_REVOKE]";
                convertView = inflateTextView(position, chatMsg);
                ((TextMsgViewHolder)convertView.getTag()).tvMessage.setText(msg);
                break;
            case ChatMsgApi.TYPE_DYNAMIC:
                msg = "[TYPE_DYNAMIC]";
                convertView = inflateTextView(position, chatMsg);
                ((TextMsgViewHolder)convertView.getTag()).tvMessage.setText(msg);
                break;
            case ChatMsgApi.TYPE_RED_ENVELOPE:
                msg = "[红包]";
                convertView = inflateTextView(position, chatMsg);
                ((TextMsgViewHolder)convertView.getTag()).tvMessage.setText(msg);
                break;
            default:
                msg = "[未知消息]";
                convertView = inflateTextView(position, chatMsg);
                ((TextMsgViewHolder)convertView.getTag()).tvMessage.setText(msg);
                break;
        }

        BaseViewHolder textMsgViewHolder = ((BaseViewHolder) convertView.getTag());
        //不是弱提示，那么肯定是对话，一定会有头像和名称组件
        if (chatMsg.getMessageType() != ChatMsgApi.TYPE_WEAK_HINT) {
            textMsgViewHolder.imgAvatar.setImageBitmap(LiteDood.getBitmapFromFile(chatMsg.getAvatar()));

            switch (mMessageActivity.getIntent().getIntExtra(MessageActivity.ID_MESSAGE_TYPE, 0)) {
                case MessageActivity.TYPE_MESSAGE_GROUP:              //群显示名称
                    String name = chatMsg.getName().trim();
                    if ((name.equals("") || name.equals("0"))) {
                        name = MessageActivity.getMemberName(chatMsg.getSendID());
                    }
                    if (!name.equals("")) {
                        textMsgViewHolder.tvName.setText(name);
                    } else {

                        textMsgViewHolder.tvName.setVisibility(View.GONE);
                    }
                    break;
                case MessageActivity.TYPE_MESSAGE_CHAT:              //点对点聊天不显示
                    textMsgViewHolder.tvName.setVisibility(View.GONE);
                    break;
                default:
                    textMsgViewHolder.tvName.setVisibility(View.GONE);
                    break;
            }
        }

        return convertView;
    }

    //注入普通文字聊天视图
    private View inflateTextView(int position, ChatMsg chatMsg) {
            View result = null;
            TextMsgViewHolder textMsgViewHolder = new TextMsgViewHolder();
            switch (getItemViewType(position)) {
                case MESSAGE_IN:
                    if (isShowTimeWeakHint(position)) {
                        result = LayoutInflater.from(mMessageActivity).inflate(R.layout.item_message_text_in_with_time, null);
                        textMsgViewHolder.tvTimeWeakHint = (AppCompatTextView)result.findViewById(R.id.tvMessageExtra);
                        textMsgViewHolder.tvTimeWeakHint.setText(LiteDood.convertTimeForMessage(mMessageActivity, chatMsg.getSendTime()));
                    }
                    else {
                        result = LayoutInflater.from(mMessageActivity).inflate(R.layout.item_message_text_in, null);
                    }
                    textMsgViewHolder.tvMessage = (AppCompatTextView) result.findViewById(R.id.tvMessageIn);
                    textMsgViewHolder.imgAvatar = (AppCompatImageView) result.findViewById(R.id.imgMessageInAvatar);
                    textMsgViewHolder.tvName = (AppCompatTextView) result.findViewById(R.id.tvMessageInName);
                    textMsgViewHolder.mMsgDirection = MESSAGE_IN;
                    break;

                case MESSAGE_OUT:
                    if (isShowTimeWeakHint(position)) {
                        result = LayoutInflater.from(mMessageActivity).inflate(R.layout.item_message_text_out_with_time, null);
                        textMsgViewHolder.tvTimeWeakHint = (AppCompatTextView)result.findViewById(R.id.tvMessageExtra);
                        textMsgViewHolder.tvTimeWeakHint.setText(LiteDood.convertTimeForMessage(mMessageActivity, chatMsg.getSendTime()));
                    }
                    else {
                        result = LayoutInflater.from(mMessageActivity).inflate(R.layout.item_message_text_out, null);
                    }
                    textMsgViewHolder.tvMessage = (AppCompatTextView) result.findViewById(R.id.tvMessageOut);
                    textMsgViewHolder.imgAvatar = (AppCompatImageView) result.findViewById(R.id.imgMessageOutAvatar);
                    textMsgViewHolder.tvName = (AppCompatTextView) result.findViewById(R.id.tvMessageOutName);
                    textMsgViewHolder.mMsgDirection = MESSAGE_OUT;
                    break;

            }
            textMsgViewHolder.mMsgDirection = getItemViewType(position);
            textMsgViewHolder.mMsgType = chatMsg.getMessageType();
            result.setTag(textMsgViewHolder);
            return result;


    }

    private View inflateWeakHintView(int msgType, View convertView) {
        if ((convertView != null) && (((TextMsgViewHolder)convertView.getTag()).mMsgType == msgType)) {
            return convertView;
        }
        else {
            TextMsgViewHolder textMsgViewHolder = new TextMsgViewHolder();
            convertView = LayoutInflater.from(mMessageActivity).inflate(R.layout.item_message_extra, null);
            textMsgViewHolder.tvMessage = (AppCompatTextView) convertView.findViewById(R.id.tvMessageExtra);
            textMsgViewHolder.mMsgType = msgType;
            convertView.setTag(textMsgViewHolder);

            return convertView;
        }
    }

    private View inflateImageView(int position, ChatMsg chatMsg) {
        View result = null;
        ImageMsgViewHolder imageMsgViewHolder = new ImageMsgViewHolder();
        switch (getItemViewType(position)) {
            case MESSAGE_IN:
                result = LayoutInflater.from(mMessageActivity).inflate(R.layout.item_message_image_in, null);
                if (isShowTimeWeakHint(position)) {

                    imageMsgViewHolder.tvTimeWeakHint = (AppCompatTextView)result.findViewById(R.id.tvMessageExtra);
                    imageMsgViewHolder.tvTimeWeakHint.setText(LiteDood.convertTimeForMessage(mMessageActivity, chatMsg.getSendTime()));
                    imageMsgViewHolder.tvTimeWeakHint.setVisibility(View.VISIBLE);
                }
                else {
                    imageMsgViewHolder.tvTimeWeakHint = (AppCompatTextView)result.findViewById(R.id.tvMessageExtra);
                    imageMsgViewHolder.tvTimeWeakHint.setVisibility(View.GONE);
                }
                imageMsgViewHolder.ivImageMessage = (AppCompatImageView) result.findViewById(R.id.ivMessageIn);
                imageMsgViewHolder.imgAvatar = (AppCompatImageView) result.findViewById(R.id.imgMessageInAvatar);
                imageMsgViewHolder.tvName = (AppCompatTextView) result.findViewById(R.id.tvMessageInName);
                imageMsgViewHolder.mMsgDirection = MESSAGE_IN;
                break;

            case MESSAGE_OUT:

                result = LayoutInflater.from(mMessageActivity).inflate(R.layout.item_message_image_out, null);
                if (isShowTimeWeakHint(position)) {
                    imageMsgViewHolder.tvTimeWeakHint = (AppCompatTextView)result.findViewById(R.id.tvMessageExtra);
                    imageMsgViewHolder.tvTimeWeakHint.setText(LiteDood.convertTimeForMessage(mMessageActivity, chatMsg.getSendTime()));
                    imageMsgViewHolder.tvTimeWeakHint.setVisibility(View.VISIBLE);
                }
                else {
                    imageMsgViewHolder.tvTimeWeakHint = (AppCompatTextView)result.findViewById(R.id.tvMessageExtra);
                    imageMsgViewHolder.tvTimeWeakHint.setVisibility(View.GONE);
                }
                imageMsgViewHolder.ivImageMessage = (AppCompatImageView) result.findViewById(R.id.ivMessageOut);
                imageMsgViewHolder.imgAvatar = (AppCompatImageView) result.findViewById(R.id.imgMessageOutAvatar);
                imageMsgViewHolder.tvName = (AppCompatTextView) result.findViewById(R.id.tvMessageOutName);
                imageMsgViewHolder.mMsgDirection = MESSAGE_OUT;
                break;

        }
        imageMsgViewHolder.mMsgDirection = getItemViewType(position);
        imageMsgViewHolder.mMsgType = chatMsg.getMessageType();
        result.setTag(imageMsgViewHolder);
        return result;

    }

    private boolean isShowTimeWeakHint(int position) {
        boolean result = false;
        if (position == 0) {
            result = true;
        }
        else {
            Time prevTime = new Time();
            ChatMsg prevMsg = (ChatMsg)getItem(position -1);
            prevTime.set(prevMsg.getSendTime());
            Time curTime = new Time();
            ChatMsg curMsg = (ChatMsg)getItem(position);
            curTime.set(curMsg.getSendTime());

            if ((prevTime.year == curTime.year) && (prevTime.month == curTime.month) && (prevTime.monthDay == curTime.monthDay) && (prevTime.hour == curTime.hour)) {
                if (Math.abs(curTime.minute - prevTime.minute) > TIME_INTERVAL) {
                    result = true;
                }
            }
            else {
                result = true;
            }
        }

        return result;
    }

    private class BaseViewHolder {
        int mMsgType = 0;
        int mMsgDirection = 0;
        AppCompatImageView imgAvatar;
        AppCompatTextView tvName;
        AppCompatTextView tvTimeWeakHint;
    }

    private class TextMsgViewHolder extends BaseViewHolder {
        AppCompatTextView tvMessage;
    }

    private class ImageMsgViewHolder extends BaseViewHolder {
        AppCompatImageView ivImageMessage;
    }

    class MessageAdapterRequestHandler extends RequestHandler {

        private int mType;
        private View mView;

        public MessageAdapterRequestHandler(int type, View imageMessageView) {
            mType = type;
            mView = imageMessageView;
        }

        @Override
        public void handleSuccess(Message msg) {
            switch (mType) {
                case TYPE_HANDLER_GET_PICTURE_THUMB:
                    Bitmap picture = LiteDood.getBitmapFromFile(msg.getData().toString());
                    if (mView != null)
                        ((AppCompatImageView)mView).setImageBitmap(picture);
                    break;

            }
        }

        @Override
        public void handleFailure(int code, String message) {
            super.handleFailure(code, message);
            switch (mType) {
                case TYPE_HANDLER_GET_PICTURE_THUMB:
                    if(mView != null) ((AppCompatImageView)mView).setImageResource(R.drawable.ic_launcher);
                    break;
            }
        }
    }

}

