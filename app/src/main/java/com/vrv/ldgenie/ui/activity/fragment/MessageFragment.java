//************************************************************//
//File Name : MessageFragment.java
//Author    : kinee
//Mailto    : kinee@163.com
//Comment   : 
//Date      : Mar 18, 2016
//************************************************************//

package com.vrv.ldgenie.ui.activity.fragment;

import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.vrv.imsdk.SDKManager;
import com.vrv.imsdk.model.Chat;
import com.vrv.ldgenie.R;
import com.vrv.ldgenie.action.RequestHandler;
import com.vrv.ldgenie.action.RequestHelper;
import com.vrv.ldgenie.adapter.ChatAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageFragment extends Fragment {

    private List<Chat> messageQueue = new ArrayList<Chat>();
    private ChatAdapter chatAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        RequestHelper.login("18602701898", "12345678", "vrv", "0086", new RequestHandler() {
            @Override
            public void handleSuccess(Message msg) {
                Toast.makeText(getActivity(), "RequestHandler", Toast.LENGTH_SHORT);
            }
        });
        messageQueue.addAll(SDKManager.instance().getChatList().getList());


        chatAdapter = new ChatAdapter(getActivity(), messageQueue);
        super.onCreate(savedInstanceState);
    }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Log.v("container", String.valueOf(container.getClass().getName()));

        View result =  inflater.inflate(R.layout.fragment_message, container, false);
        final ListView lvMessageGroup = (ListView)result.findViewById(R.id.messageGroupList);
        lvMessageGroup.setAdapter(chatAdapter);
        lvMessageGroup.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //
            }
        });


        return result;
	}


}
