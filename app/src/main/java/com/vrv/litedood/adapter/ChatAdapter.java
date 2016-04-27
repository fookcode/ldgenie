package com.vrv.litedood.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;

import com.vrv.imsdk.model.Chat;
import com.vrv.litedood.R;
import com.vrv.litedood.common.sdk.action.RequestHandler;
import com.vrv.litedood.common.sdk.utils.ChatMsgUtil;
import com.vrv.litedood.ui.activity.MainFragment.ChatFragment;

import java.io.File;
import java.util.HashMap;
import java.util.List;

/**
 * Created by kinee on 2016/3/24.
 */
public class ChatAdapter extends BaseAdapter {

    public static final String TAG = ChatAdapter.class.getSimpleName();
    public static final int TYPE_GET_USER = 1;

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

        if (context == null) return null;

        ViewHolder viewHolder;
        Bitmap bitmapAvatar;
        if(convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_chat, null);
            viewHolder = new ViewHolder();

            viewHolder.avatar = (AppCompatImageView)convertView.findViewById(R.id.chatItemAvatar);
            ViewGroup.LayoutParams layoutParams = viewHolder.avatar.getLayoutParams();
            layoutParams.width = 72;
            layoutParams.height = 72;
            viewHolder.avatar.setLayoutParams(layoutParams);

            viewHolder.count = (AppCompatTextView)convertView.findViewById(R.id.chatItemCountIndicator);
            viewHolder.name = (AppCompatTextView)convertView.findViewById(R.id.chatItemName);
            viewHolder.recentMessage = (AppCompatTextView)convertView.findViewById(R.id.chatItemRecentMessage);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder =(ViewHolder) convertView.getTag();
        }
        Chat chat = chatList.get(position);
        int unReadNum = chat.getUnReadNum();

        if (unReadNum > 0) {
            viewHolder.count.setText(String.valueOf(unReadNum));
            viewHolder.count.setVisibility(View.VISIBLE);
        }
        else if (viewHolder.count.getVisibility() == View.VISIBLE){
            viewHolder.count.setVisibility(View.GONE);
        }

        String avatarPath = chat.getAvatar();
        if ((null != avatarPath) && (!avatarPath.isEmpty())) {
           File fAvatar = new File(avatarPath);
            if ((fAvatar.isDirectory()) || (!fAvatar.exists())) {

                viewHolder.avatar.setImageResource(R.drawable.ic_launcher);
                //boolean result = RequestHelper.getUserInfo(chat.getId(), new ChatRequlestHandler(context, viewHolder, TYPE_GET_USER));
                //if (!result) {Log.v(TAG, "获取用户数据失败");}
            }
            else {
                bitmapAvatar = BitmapFactory.decodeFile(avatarPath);
                viewHolder.avatar.setImageBitmap(bitmapAvatar);
            }
        }

        viewHolder.name.setText(chat.getName());
        viewHolder.recentMessage.setText(ChatMsgUtil.lastMsgBrief(context, chat.getMsgType(), chat.getLastMsg()));

        return convertView;
    }

        @Override
        public Object getItem(int index) {
            return chatList.get(index);
        }

        public long getItemId(int index) {
            return index;
        }


    class ViewHolder {

        public AppCompatImageView avatar;

        public AppCompatTextView count;

        public AppCompatTextView name;

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

