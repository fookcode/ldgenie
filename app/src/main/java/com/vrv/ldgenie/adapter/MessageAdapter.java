package com.vrv.ldgenie.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.vrv.imsdk.SDKManager;
import com.vrv.imsdk.api.ChatMsgApi;
import com.vrv.imsdk.model.ChatMsg;
import com.vrv.ldgenie.R;

import java.io.File;
import java.util.List;

/**
 * Created by kinee on 2016/3/31.
 */
public class MessageAdapter extends BaseAdapter {
    private static long myID = -1;
    private static final int MESSAGE_IN = 1;
    private static final int MESSAGE_OUT = 2;

    private Context context;
    private List<ChatMsg> chatMsgList;

    public MessageAdapter(Context context, List<ChatMsg> chatMsgList) {
        this.context = context;
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
        return SDKManager.instance().getAuth().isMyself(chatMsg.getSendID()) ? MESSAGE_OUT : MESSAGE_IN;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ChatMsg chatMsg = chatMsgList.get(position);
        int type = getItemViewType(position);
        ViewHolder viewHolder = null;
        String msg;

        convertView = null;
        switch (type) {
            case MESSAGE_IN: {
                convertView = LayoutInflater.from(context).inflate(R.layout.message_in_item, null);
                viewHolder = new ViewHolder();
                viewHolder.tvMessage = (AppCompatTextView)convertView.findViewById(R.id.tvMessageIn);
                viewHolder.imgAvatar = (AppCompatImageView)convertView.findViewById(R.id.imgMessageInAvatar);
                break;
            }
            case MESSAGE_OUT: {
                convertView = LayoutInflater.from(context).inflate(R.layout.message_out_item, null);
                viewHolder = new ViewHolder();
                viewHolder.tvMessage = (AppCompatTextView)convertView.findViewById(R.id.tvMessageOut);
                viewHolder.imgAvatar = (AppCompatImageView)convertView.findViewById(R.id.imgMessageOutAvatar);
                break;
            }
        }

        String avatarPath = chatMsg.getAvatar();
        if ((null != avatarPath) && (!avatarPath.isEmpty())) {
            File fAvatar = new File(avatarPath);
            if ((fAvatar.isDirectory()) || (!fAvatar.exists()))
                viewHolder.imgAvatar.setImageResource(R.drawable.ic_launcher);
            else {
                Bitmap bitmapAvatar = BitmapFactory.decodeFile(avatarPath);
                viewHolder.imgAvatar.setImageBitmap(bitmapAvatar);
            }
        }


        switch (chatMsg.getMessageType()) {
            case ChatMsgApi.TYPE_TEXT:
                msg = ChatMsgApi.parseTxtJson(chatMsg.getMessage());
                break;
            case ChatMsgApi.TYPE_IMAGE:
//                holder.mChatMsgImg.setVisibility(View.VISIBLE);
//                holder.mChatMsgTxt.setVisibility(View.GONE);
//                msg="";
//                msgImageview=holder.mChatMsgImg;
//                showImg(chatMsg);
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

        public ViewHolder() {};
    }

}

