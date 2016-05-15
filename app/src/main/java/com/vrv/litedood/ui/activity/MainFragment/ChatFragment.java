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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.vrv.imsdk.SDKManager;
import com.vrv.imsdk.api.ChatMsgApi;
import com.vrv.imsdk.model.Chat;
import com.vrv.imsdk.model.ChatList;
import com.vrv.imsdk.model.ListModel;
import com.vrv.litedood.R;
import com.vrv.litedood.adapter.ChatAdapter;
import com.vrv.litedood.common.sdk.action.RequestHelper;
import com.vrv.litedood.common.sdk.utils.ChatMsgUtil;
import com.vrv.litedood.ui.activity.MessageActivity;

import java.util.ArrayList;
import java.util.List;

public class ChatFragment extends Fragment {
    private static String TAG = ChatFragment.class.getSimpleName();

    private ChatList mChatList;
    private List<Chat> mChatQueue =  new ArrayList<>();;
    private ChatAdapter mChatAdapter;
    private ListViewCompat mChatView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mChatList =  SDKManager.instance().getChatList();

        mChatQueue.addAll(mChatList.getList());     //初始化获取对话列表

        mChatAdapter = new ChatAdapter(getActivity(), mChatQueue);   //初始化适配器

        //添加监听，有新会话时刷新会话列表
        mChatList.setListener(new ListModel.OnChangeListener() {

            @Override
            public void notifyDataChange() {
                ArrayList<Chat> chatList = mChatList.getList();

                mChatQueue.clear();
                mChatQueue.addAll(chatList);

                if ((mChatQueue.size() > 0) && (mChatAdapter != null)) {
                    mChatAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void notifyItemChange(int i) {
                if (mChatAdapter != null) {
                    //View chatItemView = (View) mChatView.getView(i);
                    //ChatAdapter.ViewHolder chatItem = (ChatAdapter.ViewHolder)chatItemView.getTag();

                    mChatAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Log.v("container", String.valueOf(container.getClass().getName()));

        View result =  inflater.inflate(R.layout.fragment_chat, container, false);
         mChatView = (ListViewCompat)result.findViewById(R.id.lvChat);
        mChatView.setAdapter(mChatAdapter);
        mChatView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Chat chat = (Chat)parent.getItemAtPosition(position);
                Log.v(TAG, chat.toString());
                if (chat != null) {
                    //BaseInfoBean bean = BaseInfoBean.chat2BaseInfo(chat);
                    MessageActivity.startMessageActivity(ChatFragment.this.getActivity(), chat);
                }

            }
        });
        return result;
	}

}
