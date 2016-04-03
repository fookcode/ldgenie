package com.vrv.ldgenie.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;

import com.vrv.imsdk.model.Chat;
import com.vrv.ldgenie.R;
import com.vrv.ldgenie.common.sdk.utils.ChatMsgUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kinee on 2016/3/24.
 */
public class ChatAdapter extends BaseAdapter {

    private Context context;
    private  List<Chat> chatList;

    public ChatAdapter(Context context, List<Chat> chatList) {
        this.context = context;
        this.chatList = chatList;
    }

    @Override
    public int getCount() {
        if (chatList == null) return 0;
        else return chatList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        Bitmap bitmapAvatar;
        if(convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.chat_item, null);
            viewHolder = new ViewHolder();

            viewHolder.avatar = (AppCompatImageView)convertView.findViewById(R.id.chatItemAvatar);
            ViewGroup.LayoutParams layoutParams = viewHolder.avatar.getLayoutParams();
            layoutParams.width = 72;
            layoutParams.height = 72;
            viewHolder.avatar.setLayoutParams(layoutParams);

            viewHolder.title = (AppCompatTextView)convertView.findViewById(R.id.chatItemTitle);
            viewHolder.recentMessage = (AppCompatTextView)convertView.findViewById(R.id.chatItemRecentMessage);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder =(ViewHolder) convertView.getTag();
        }
        Chat chat = chatList.get(position);
        String avatarPath = chat.getAvatar();
        if ((null != avatarPath) && (!avatarPath.isEmpty())) {
           File fAvatar = new File(avatarPath);
            if ((fAvatar.isDirectory()) || (!fAvatar.exists()))
                viewHolder.avatar.setImageResource(R.drawable.ic_launcher);
            else {
                bitmapAvatar = BitmapFactory.decodeFile(avatarPath);
                viewHolder.avatar.setImageBitmap(bitmapAvatar);
            }
        }

        ArrayList relatedUser = chat.getRelatedUsers();
        //String describeContents = chat.describeContents() == null ? "null":chat.describeContents();
        //Log.v("aaaaaaaa", "relatedUsers:" + (relatedUser == null ? "好友":relatedUser.toString()) + " describeContents: " + describeContents  + " whereFrom: " +  chat.getWhereFrom());
        viewHolder.title.setText(chat.getName());
        Log.v("getAvatar", chat.getAvatar());
        viewHolder.recentMessage.setText(ChatMsgUtil.lastMsgBrief(context, chat.getMsgType(), chat.getLastMsg()));

        return convertView;
    }

    class ViewHolder {

        public AppCompatImageView avatar;

        public AppCompatTextView title;

        public AppCompatTextView recentMessage;

        public ViewHolder() {};

    }

    @Override
    public Object getItem(int index) {
        return chatList.get(index);
    }

    public long getItemId(int index) {
        return index;
    }
}

