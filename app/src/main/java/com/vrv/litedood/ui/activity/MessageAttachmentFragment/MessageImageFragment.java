package com.vrv.litedood.ui.activity.MessageAttachmentFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vrv.litedood.LiteDoodApplication;
import com.vrv.litedood.R;
import com.vrv.litedood.common.LiteDood;

import java.util.ArrayList;

/**
 * Created by yanqiyang on 2016/5/24.
 */
public class MessageImageFragment extends Fragment {
    private ArrayList<String> mImageFiles;

    public MessageImageFragment() {
        super();
        mImageFiles = LiteDood.getAllShownImagesPath(LiteDoodApplication.getMainActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        View result = inflater.inflate(R.layout.fragment_message_attachement_image, null);
        AppCompatEditText tvImageFiles = (AppCompatEditText)result.findViewById(R.id.tvImageFiles);
        SpannableStringBuilder ssb = new SpannableStringBuilder();
        ssb.append(String.valueOf(mImageFiles.size())+"\n");
        for (String path : mImageFiles) {
            ssb.append(path + "\n");
        }
        tvImageFiles.setText(ssb);

        return result;
    }
}
