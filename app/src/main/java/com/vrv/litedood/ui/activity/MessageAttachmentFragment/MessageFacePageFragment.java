package com.vrv.litedood.ui.activity.MessageAttachmentFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vrv.litedood.R;

/**
 * Created by kinee on 2016/6/5.
 */
public class MessageFacePageFragment extends Fragment {
    public MessageFacePageFragment() {
        super();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View result = inflater.inflate(R.layout.fragment_message_attachement_face_page, null);
        return result;
    }
}
