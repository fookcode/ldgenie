package com.vrv.litedood.adapter;

import android.content.Context;
import android.os.Message;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;

import com.vrv.imsdk.model.Chat;
import com.vrv.litedood.R;
import com.vrv.litedood.common.LiteDood;
import com.vrv.litedood.common.sdk.action.RequestHandler;
import com.vrv.litedood.common.sdk.utils.ChatMsgUtil;

import java.util.List;

/**
 * Created by kinee on 2016/3/24.
 */
public class ChatAdapter extends BaseAdapter {

    public static final String TAG = ChatAdapter.class.getSimpleName();
    public static final int TYPE_GET_USER = 1;

//    private static final int CHAT_AVATAR_SIZE = 72;

    private Context context;
    private  List<Chat> chatList;

    public ChatAdapter(Context context, List<Chat> chatList) {
        this.context = context;
        this.chatList = chatList;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        if (chatList == null) return 0;
        else return chatList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (context == null) return null;

        ViewHolder viewHolder;
        if(convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_chat, null);
            viewHolder = new ViewHolder();

            viewHolder.avatar = (AppCompatImageView)convertView.findViewById(R.id.ivChatItemAvatar);

            viewHolder.count = (AppCompatTextView)convertView.findViewById(R.id.tvChatItemCountIndicator);
            viewHolder.name = (AppCompatTextView)convertView.findViewById(R.id.tvChatItemName);
            viewHolder.time = (AppCompatTextView)convertView.findViewById(R.id.tvChatItemTime);
            viewHolder.status = (ContentLoadingProgressBar)convertView.findViewById(R.id.clpbChatItemSendingSatusIndicator);
            viewHolder.recentMessage = (AppCompatTextView)convertView.findViewById(R.id.tvChatItemRecentMessage);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder =(ViewHolder) convertView.getTag();
        }
        Chat chat = chatList.get(position);
        //Log.v(TAG, chat.toString());

        //设置未读指示
        int unReadNum = chat.getUnReadNum();
        if (unReadNum > 0) {
            if (unReadNum > 99) {
                viewHolder.count.setText("..");
            }
            else viewHolder.count.setText(String.valueOf(unReadNum));
            viewHolder.count.setVisibility(View.VISIBLE);
        }
        else if (viewHolder.count.getVisibility() == View.VISIBLE){
            viewHolder.count.setVisibility(View.GONE);
        }

        //设置头像
        viewHolder.avatar.setImageBitmap(LiteDood.getBitmapFromFile(chat.getAvatar()));

        //设置聊天对方名称
        viewHolder.name.setText(chat.getName());

        //设置发送时间
        viewHolder.time.setText(LiteDood.convertTimeForChat(context, chat.getTime()));

        //设置消息发送状态
        if(chat.getMsgType() != 2) {
            viewHolder.status.setVisibility(View.VISIBLE);
        }
        else viewHolder.status.setVisibility(View.GONE);

        //设置消息体
        String prefix = "";
        if (chat.getWhereFrom() != null) {
            prefix = chat.getWhereFrom().trim().equals("") ? "" : chat.getWhereFrom() + ": ";
        }
        viewHolder.recentMessage.setText(prefix + ChatMsgUtil.lastMsgBrief(context, chat.getMsgType(), chat.getLastMsg()));

        return convertView;
    }

        @Override
        public Object getItem(int index) {
            return chatList.get(index);
        }

        public long getItemId(int index) {
            return index;
        }


    private class ViewHolder {

        public AppCompatImageView avatar;

        public AppCompatTextView count;

        public AppCompatTextView name;

        public AppCompatTextView time;

        public ContentLoadingProgressBar status;

        public AppCompatTextView recentMessage;

        public ViewHolder() {
        }
    }


    class ChatRequlestHandler extends RequestHandler {
        private int nType;
        private Context context;
        private ViewHolder viewHolder;

        public ChatRequlestHandler(Context context, ViewHolder holder, int nType) {
            this.context = context;
            this.viewHolder = holder;
            this.nType = nType;
        }

        @Override
        public void handleSuccess(Message msg) {
            switch (nType) {
                case TYPE_GET_USER : {
                    Log.v(TAG, msg.getData().toString());
                }
            }
        }
    }
}

