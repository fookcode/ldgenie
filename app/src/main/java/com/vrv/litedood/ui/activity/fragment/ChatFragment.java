//************************************************************//
//File Name : ChatFragment.java
//Author    : kinee
//Mailto    : kinee@163.com
//Comment   : 
//Date      : Mar 18, 2016
//************************************************************//

package com.vrv.litedood.ui.activity.fragment;

import android.content.ContentResolver;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.ListViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.vrv.imsdk.SDKManager;
import com.vrv.imsdk.model.Chat;
import com.vrv.imsdk.model.ListModel;
import com.vrv.litedood.R;
import com.vrv.litedood.adapter.ChatAdapter;
import com.vrv.litedood.adapter.MessageAdapter;
import com.vrv.litedood.ui.activity.MessageActivity;

import java.util.ArrayList;
import java.util.List;

public class ChatFragment extends Fragment {
    private static String TAG = ChatFragment.class.getSimpleName();

    private ContentResolver resolver;

    private List<Chat> chatQueue = new ArrayList<Chat>();
    private ChatAdapter chatAdapter;


    private MessageAdapter messageAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        ArrayList<Chat> chatArrayList = SDKManager.instance().getChatList().getList();
        chatQueue.addAll(chatArrayList);
        setNotifyListener();
        chatAdapter = new ChatAdapter(getActivity(), chatQueue);
        resolver = getActivity().getContentResolver();
        super.onCreate(savedInstanceState);
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

    private void setNotifyListener() {
        SDKManager.instance().getChatList().setListener(new ListModel.OnChangeListener() {

            @Override
            public void notifyDataChange() {

                chatQueue.clear();
                chatQueue.addAll(SDKManager.instance().getChatList().getList());

                if (chatAdapter != null)
                    chatAdapter.notifyDataSetChanged();
            }

            @Override
            public void notifyItemChange(int i) {
                chatAdapter.notifyDataSetChanged();
            }
        });
    }

    private void saveMessageToDB(List<Chat> chatList) {
        //resolver.insert()
    }
}
