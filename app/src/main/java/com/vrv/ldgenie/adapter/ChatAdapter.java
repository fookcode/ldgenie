package com.vrv.ldgenie.adapter;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.vrv.imsdk.model.Chat;
import com.vrv.ldgenie.R;

import org.w3c.dom.Text;

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
        if(convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.chat_item, null);
            viewHolder = new ViewHolder();
            viewHolder.avatar = (ImageView)convertView.findViewById(R.id.chatItemAvatar);
            viewHolder.title = (TextView)convertView.findViewById(R.id.chatItemTitle);
            viewHolder.recentMessage = (TextView)convertView.findViewById(R.id.chatItemRecentMessage);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder =(ViewHolder) convertView.getTag();
        }
        Chat chat = chatList.get(position);
        viewHolder.avatar.setImageResource(R.drawable.ic_launcher);
        viewHolder.title.setText(chat.getAvatar());
        viewHolder.recentMessage.setText(chat.getLastMsg());

        return convertView;
    }

    class ViewHolder {

        public ImageView avatar;

        public TextView title;

        public TextView recentMessage;

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

