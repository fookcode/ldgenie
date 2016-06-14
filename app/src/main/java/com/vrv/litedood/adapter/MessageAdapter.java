package com.vrv.litedood.adapter;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Message;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.format.Time;
import android.text.style.DynamicDrawableSpan;
import android.text.style.ImageSpan;
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
import com.vrv.litedood.LiteDoodApplication;
import com.vrv.litedood.R;
import com.vrv.litedood.common.LiteDood;
import com.vrv.litedood.common.sdk.action.RequestHandler;
import com.vrv.litedood.common.sdk.action.RequestHelper;
import com.vrv.litedood.ui.activity.MessageActivity;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by kinee on 2016/3/31.
 */
public class MessageAdapter extends BaseAdapter {
    private static String TAG = MessageAdapter.class.getSimpleName();

    private static final int MESSAGE_IN = 1;
    private static final int MESSAGE_OUT = 2;

    private static final int TIME_INTERVAL = 15;       //间隔时间大于15分钟的消息显示一个时间弱提示

    private static final int TYPE_HANDLER_GET_PICTURE_THUMB = 1;
    private static final int TYPE_HANDLER_SHOW_IMAGE = 2;

    private final MessageActivity mMessageActivity;
    private List<ChatMsg> mChatMsgList;

    private static final Bitmap mMyAvatar = LiteDood.getBitmapFromFile(LiteDoodApplication.getAppContext().getMyself().getAvatar());
    private static Bitmap mMemberAvatar = null;

    private Pattern mPattern = Pattern.compile("\\[\\S{1,3}\\]");
    private HashMap<String, ImageSpan> mFaces = new HashMap<>();

    public MessageAdapter(MessageActivity mMessageActivity, List<ChatMsg> chatMsgList) {
        this.mMessageActivity = mMessageActivity;
        this.mChatMsgList = chatMsgList;
        mMemberAvatar = LiteDood.getBitmapFromFile(mMessageActivity.getIntent().getStringExtra(MessageActivity.ID_USER_AVATAR));
    }

    @Override
    public int getCount() {
        int result = 0;
        if (mChatMsgList != null)
            result = mChatMsgList.size();
        return result;
    }

    @Override
    public Object getItem(int position) {
        if(mChatMsgList != null) return mChatMsgList.get(position);
        else return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        ChatMsg chatMsg = mChatMsgList.get(position);
        long id = chatMsg.getSendID() == 0?chatMsg.getId():chatMsg.getSendID();

        return  SDKManager.instance().getAuth().isMyself(id) ? MESSAGE_OUT : MESSAGE_IN;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ChatMsg chatMsg = mChatMsgList.get(position);
        Log.v(TAG, chatMsg == null?"":chatMsg.toString() + chatMsg.getMessage());
        int direction = getItemViewType(position);
        String msg;

        switch (chatMsg.getMessageType()) {
            case ChatMsgApi.TYPE_HTML:
                msg = "[网页]";
                break;
            case ChatMsgApi.TYPE_TEXT:
                if ((convertView != null) &&  //释放图片资源
                         (((BaseViewHolder)convertView.getTag()).mMsgType == ChatMsgApi.TYPE_IMAGE) &&
                            (((ImageMsgViewHolder)convertView.getTag()).ivImageMessage != null)) {
                    ((ImageMsgViewHolder)convertView.getTag()).ivImageMessage.setImageBitmap(null);

                    //convertView = null;
                }
                if ((convertView != null) &&
                        (((BaseViewHolder)convertView.getTag()).mMsgType == ChatMsgApi.TYPE_TEXT)&&
                            (((BaseViewHolder)convertView.getTag()).mMsgDirection == direction)) {
                    if (!(isShowTimeWeakHint(position) && (((TextMsgViewHolder) convertView.getTag()).tvTimeWeakHint != null))) {  //
                        convertView = inflateTextView(position, chatMsg);
                    }
                    else ((TextMsgViewHolder) convertView.getTag()).tvTimeWeakHint.setText(LiteDood.convertTimeForMessage(mMessageActivity, chatMsg.getSendTime()));  //重用
                }
                else {
                    convertView = inflateTextView(position, chatMsg);

                }

                ((TextMsgViewHolder)convertView.getTag()).mMsgType = ChatMsgApi.TYPE_TEXT;

                msg = ChatMsgApi.parseTxtJson(chatMsg.getMessage());
                SpannableString sMsg = new SpannableString(msg);
                Matcher m = mPattern.matcher(msg);
                while (m.find()) {

                    Drawable dr = LiteDood.getFaceFromCode(m.group());
                    if (dr != null) {
                        ImageSpan imageSpan = new ImageSpan(dr, ImageSpan.ALIGN_BASELINE);
                        sMsg.setSpan(imageSpan, m.start(), m.start() + m.group().length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                    }
                 }
                ((TextMsgViewHolder)convertView.getTag()).tvMessage.setText(sMsg);
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
                        (((BaseViewHolder)convertView.getTag()).mMsgType == ChatMsgApi.TYPE_IMAGE)  &&
                                (((BaseViewHolder)convertView.getTag()).mMsgDirection == direction)) {
                    ((ImageMsgViewHolder) convertView.getTag()).ivImageMessage.setImageResource(R.drawable.ic_image);
                    if (isShowTimeWeakHint(position)) {                      //可重用，对时间提示标签进行处理
                        ((ImageMsgViewHolder) convertView.getTag()).tvTimeWeakHint.setText(LiteDood.convertTimeForMessage(mMessageActivity, chatMsg.getSendTime()));
                        ((ImageMsgViewHolder) convertView.getTag()).tvTimeWeakHint.setVisibility(View.VISIBLE);
                    }
                    else {
                        ((ImageMsgViewHolder) convertView.getTag()).tvTimeWeakHint.setText("");
                        ((ImageMsgViewHolder) convertView.getTag()).tvTimeWeakHint.setVisibility(View.GONE);
                    }
                }
                else {                                                      //不可重用，新建View
                    convertView = inflateImageView(position, chatMsg);

                }

                //填充图片源
                MsgImage image = ChatMsgApi.parseImgJson(chatMsg.getMessage());
                if (!setMessageImageShow(image.getEncDecKey(), image.getThumbShowPath(), ((ImageMsgViewHolder) convertView.getTag()).ivImageMessage)) {
                    //没有设置成功说明图片没有下载或没有解码，重新找服务器获取
                    RequestHelper.downloadThumbImg(chatMsg, new MessageAdapterRequestHandler(TYPE_HANDLER_GET_PICTURE_THUMB,
                            ((ImageMsgViewHolder) convertView.getTag()).ivImageMessage, image.getEncDecKey()));
                }
//
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
                //msg = "[消息撤回]";
//                convertView = inflateTextView(position, chatMsg);
//                ((TextMsgViewHolder)convertView.getTag()).tvMessage.setText(msg);
//                ConfigApi.decryptFile()
                msg = ChatMsgApi.parseTxtJson(chatMsg.getMessage());
                if (msg.equals("")) msg = "[消息撤回]";
                else msg = msg + "撤回了一条消息";
                convertView = inflateWeakHintView(ChatMsgApi.TYPE_WEAK_HINT, convertView);
                ((TextMsgViewHolder)convertView.getTag()).tvMessage.setText(msg);
                break;
            case ChatMsgApi.TYPE_DYNAMIC:
                msg = "[动态表情]";
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

        BaseViewHolder baseViewHolder = ((BaseViewHolder) convertView.getTag());
        //不是弱提示，那么肯定是对话，一定会有头像和名称组件
        if ((chatMsg.getMessageType() != ChatMsgApi.TYPE_WEAK_HINT) && (chatMsg.getMessageType() != ChatMsgApi.TYPE_REVOKE)) {
            //baseViewHolder.imgAvatar.setImageBitmap(LiteDood.getBitmapFromFile(chatMsg.getAvatar()));


            switch (mMessageActivity.getIntent().getIntExtra(MessageActivity.ID_MESSAGE_TYPE, 0)) {
                case MessageActivity.TYPE_MESSAGE_GROUP:              //群显示名称
                    final Bitmap friendAvatar = LiteDood.getBitmapFromFile(MessageActivity.getMemberAvatar(chatMsg.getSendID()));
                    switch (baseViewHolder.mMsgDirection) {
                        case MESSAGE_IN:
                            baseViewHolder.imgAvatar.setImageBitmap(friendAvatar);
                            break;
                        case MESSAGE_OUT:
                            baseViewHolder.imgAvatar.setImageBitmap(mMyAvatar);
                            break;
                    }
                    String name = chatMsg.getName().trim();
                    if ((name.equals("") || name.equals("0"))) {
                        name = MessageActivity.getMemberName(chatMsg.getSendID());
                    }
                    if (!name.equals("")) {
                        baseViewHolder.tvName.setText(name);
                    } else {

                        baseViewHolder.tvName.setVisibility(View.GONE);
                    }
                    break;
                case MessageActivity.TYPE_MESSAGE_CHAT:              //点对点聊天不显示
                    switch (baseViewHolder.mMsgDirection) {
                        case MESSAGE_IN:

                            baseViewHolder.imgAvatar.setImageBitmap(mMemberAvatar); //chatMsg.getAvatar()));
                            break;
                        case MESSAGE_OUT:
                            baseViewHolder.imgAvatar.setImageBitmap(mMyAvatar);     //自己
                            break;
                    }
                    baseViewHolder.tvName.setVisibility(View.GONE);
                    break;
                default:
                    baseViewHolder.tvName.setVisibility(View.GONE);
                    break;
            }
        }

        return convertView;
    }

    //弱提示
    private View inflateWeakHintView(int msgType, View convertView) {
        if ((convertView != null) && (((BaseViewHolder)convertView.getTag()).mMsgType == msgType)) {
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

    //将加密图片解码，并设置到ImageView中显示
    private boolean setMessageImageShow(String decKey, String path, View imageView) {
        boolean result = false;
        String imageName = ConfigApi.decryptFile(decKey, path);
        File file = new File(imageName);
        if (file.exists()) {
            Bitmap picture = LiteDood.getBitmapFromFile(imageName);

            if (imageView != null) {
                ((AppCompatImageView) imageView).setImageBitmap(picture);
                result = true;
            }
        }
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
        private String mDecKey;
        private String mEncFilePath;

        public MessageAdapterRequestHandler(int type, View imageMessageView, String decKey) {
            mType = type;
            mView = imageMessageView;
            mDecKey = decKey;
        }

        @Override
        public void handleSuccess(final Message msg) {
            mEncFilePath = msg.getData().toString();
            switch (mType) {
                case TYPE_HANDLER_GET_PICTURE_THUMB:
                   setMessageImageShow(mDecKey, mEncFilePath, mView);
                    break;
                case TYPE_HANDLER_SHOW_IMAGE:
                    break;

            }
        }

        @Override
        public void handleFailure(int code, String message) {
            super.handleFailure(code, message);
            switch (mType) {
                case TYPE_HANDLER_GET_PICTURE_THUMB:
                    if(mView != null) ((AppCompatImageView)mView).setImageResource(R.drawable.ic_image_failure);
                    break;
            }
        }
    }

}

