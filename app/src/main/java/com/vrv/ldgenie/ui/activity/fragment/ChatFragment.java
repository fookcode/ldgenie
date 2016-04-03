//************************************************************//
//File Name : ChatFragment.java
//Author    : kinee
//Mailto    : kinee@163.com
//Comment   : 
//Date      : Mar 18, 2016
//************************************************************//

package com.vrv.ldgenie.ui.activity.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.vrv.imsdk.SDKManager;
import com.vrv.imsdk.model.Chat;
import com.vrv.imsdk.model.ListModel;
import com.vrv.ldgenie.R;
import com.vrv.ldgenie.common.sdk.action.RequestHandler;
import com.vrv.ldgenie.common.sdk.action.RequestHelper;
import com.vrv.ldgenie.adapter.ChatAdapter;
import com.vrv.ldgenie.common.sdk.utils.BaseInfoBean;
import com.vrv.ldgenie.ui.activity.MessageActivity;

import java.util.ArrayList;
import java.util.List;

public class ChatFragment extends Fragment {

    private List<Chat> messageQueue = new ArrayList<Chat>();
    private ChatAdapter chatAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        ArrayList<Chat> chatArrayList = SDKManager.instance().getChatList().getList();
        messageQueue.addAll(chatArrayList);
        setNotifyListener();
        chatAdapter = new ChatAdapter(getActivity(), messageQueue);

         super.onCreate(savedInstanceState);
    }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Log.v("container", String.valueOf(container.getClass().getName()));

        View result =  inflater.inflate(R.layout.fragment_chat, container, false);
        final ListView lvMessageGroup = (ListView)result.findViewById(R.id.listChat);
        lvMessageGroup.setAdapter(chatAdapter);
        lvMessageGroup.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Chat chat = (Chat)parent.getItemAtPosition(position);
                if (chat != null) {
                    BaseInfoBean bean = BaseInfoBean.chat2BaseInfo(chat);
                    MessageActivity.startMessageActivity(ChatFragment.this.getActivity(), bean);
                }

            }
        });
        return result;
	}

    private void setNotifyListener() {
        SDKManager.instance().getChatList().setListener(new ListModel.OnChangeListener() {

            @Override
            public void notifyDataChange() {
                messageQueue.clear();
                messageQueue.addAll(SDKManager.instance().getChatList().getList());
                if (chatAdapter != null)
                    chatAdapter.notifyDataSetChanged();
            }

            @Override
            public void notifyItemChange(int i) {
                chatAdapter.notifyDataSetChanged();
            }
        });
    }

}
