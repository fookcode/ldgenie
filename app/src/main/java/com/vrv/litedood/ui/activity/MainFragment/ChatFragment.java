//************************************************************//
//File Name : ChatFragment.java
//Author    : kinee
//Mailto    : kinee@163.com
//Comment   : 
//Date      : Mar 18, 2016
//************************************************************//

package com.vrv.litedood.ui.activity.MainFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.ListViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.vrv.imsdk.SDKManager;
import com.vrv.imsdk.model.Chat;
import com.vrv.imsdk.model.ChatList;
import com.vrv.imsdk.model.ListModel;
import com.vrv.litedood.R;
import com.vrv.litedood.adapter.ChatAdapter;
import com.vrv.litedood.ui.activity.MessageActivity;

import java.util.ArrayList;
import java.util.List;

public class ChatFragment extends Fragment {
    private static String TAG = ChatFragment.class.getSimpleName();

    private ChatList chatList;
    private List<Chat> chatQueue = new ArrayList<>();
    private ChatAdapter chatAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        chatList =  SDKManager.instance().getChatList();
        ArrayList<Chat> chatArrayList = chatList.getList();          //初次获取会话列表

        chatQueue.addAll(chatArrayList);
        chatAdapter = new ChatAdapter(getActivity(), chatQueue);   //初始化适配器

        setChatQueueChangeListener();                                  //添加监听，有新会话时刷新会话列表

    }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Log.v("container", String.valueOf(container.getClass().getName()));

        View result =  inflater.inflate(R.layout.fragment_chat, container, false);
        final ListViewCompat lvMessageGroup = (ListViewCompat)result.findViewById(R.id.listChat);
        lvMessageGroup.setAdapter(chatAdapter);
        lvMessageGroup.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Chat chat = (Chat)parent.getItemAtPosition(position);
                if (chat != null) {
                    //BaseInfoBean bean = BaseInfoBean.chat2BaseInfo(chat);
                    MessageActivity.startMessageActivity(ChatFragment.this.getActivity(), chat);
                }

            }
        });
        return result;
	}

    private void setChatQueueChangeListener() {
        chatList.setListener(new ListModel.OnChangeListener() {

            @Override
            public void notifyDataChange() {

                chatQueue.clear();
                chatQueue.addAll(SDKManager.instance().getChatList().getList());

                if (chatAdapter != null)
                    chatAdapter.notifyDataSetChanged();
            }

            @Override
            public void notifyItemChange(int i) {
                if(chatAdapter != null)
                    chatAdapter.notifyDataSetChanged();
            }
        });
    }

}
