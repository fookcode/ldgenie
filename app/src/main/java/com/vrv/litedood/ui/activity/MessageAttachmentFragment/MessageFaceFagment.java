package com.vrv.litedood.ui.activity.MessageAttachmentFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vrv.litedood.R;

/**
 * Created by yanqiyang on 2016/5/24.
 */
public class MessageFaceFagment extends Fragment {

    public MessageFaceFagment() {
        super();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_message_attachment_face, null);
    }
}
