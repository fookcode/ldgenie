package com.vrv.litedood.ui.activity.MessageAttachmentFragment;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatImageView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.bumptech.glide.load.engine.prefill.BitmapPreFiller;
import com.vrv.litedood.R;
import com.vrv.litedood.common.LiteDood;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by kinee on 2016/6/17.
 */
public class MessageImagePageFragment extends Fragment {

    private ArrayList<HashMap<String, Object>> mImageList;
    private int mIndex = -1;

    public void setImageList(ArrayList<HashMap<String, Object>> imageList, int fragmentIndex) {
        mImageList = imageList;
        mIndex = fragmentIndex;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View result = inflater.inflate(R.layout.fragment_message_attachment_image_page, null);
        for (int i = 0; i <= mImageList.size() -1 ; i ++) {
            AppCompatImageView imageView = (AppCompatImageView) result.findViewWithTag("image" + String.valueOf(i + 1));
            imageView.setImageBitmap((Bitmap) mImageList.get(i).get("bitmap"));
            AppCompatCheckBox checkBox = (AppCompatCheckBox)result.findViewWithTag("check" + String.valueOf(i + 1));
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    String checkName = buttonView.getTag().toString();
                    if (isChecked) {
                        MessageImageFragment.mSelectedImages.add(mImageList.get(Integer.parseInt(checkName.substring(checkName.length() -1))-1).get("path").toString());
                    }
                    else {
                        MessageImageFragment.mSelectedImages.remove(mImageList.get(Integer.parseInt(checkName.substring(checkName.length() -1))-1).get("path").toString());
                    }
                }
            });
        }
        return result;

    }
}
