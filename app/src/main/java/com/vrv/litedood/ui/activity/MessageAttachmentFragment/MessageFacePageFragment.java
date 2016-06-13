package com.vrv.litedood.ui.activity.MessageAttachmentFragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatImageView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vrv.litedood.R;

import java.util.ArrayList;

/**
 * Created by kinee on 2016/6/5.
 */
public class MessageFacePageFragment extends Fragment {
    private Bitmap[] mFaces;

    public MessageFacePageFragment() {
        super();
    }

    public void setFaces(Bitmap[] faces) {
        mFaces = faces;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View result = inflater.inflate(R.layout.fragment_message_attachement_face_page, null);
        if (mFaces != null){
            for (int i = 1; i <= mFaces.length; i ++) {
                String name = "face" + String.valueOf(i);
                AppCompatImageView face = (AppCompatImageView) result.findViewWithTag(name);
                if (mFaces[i-1] != null) {
                    face.setImageBitmap(mFaces[i-1]);
                }
                else break;
            }
        }
        return result;
    }

}
