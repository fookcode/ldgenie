package com.vrv.litedood.ui.activity.MessageAttachmentFragment;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vrv.litedood.LiteDoodApplication;
import com.vrv.litedood.R;

/**
 * Created by kinee on 2016/6/5.
 */
public class MessageFacePageFragment extends Fragment {
    private Bitmap[] mFaces;
    private String[] mCodes;
    public MessageFacePageFragment() {
        super();
    }

    public void setFaces(Bitmap[] faces, String[] codes) {
        mFaces = faces;
        mCodes = codes;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View result = inflater.inflate(R.layout.fragment_message_attachment_face_page, null);
        if (mFaces != null){
            for (int i = 1; i <= mFaces.length; i ++) {
                String name = "face" + String.valueOf(i);
                AppCompatImageView face = (AppCompatImageView) result.findViewWithTag(name);
                if (mFaces[i-1] != null) {
                    face.setImageBitmap(mFaces[i-1]);
                    face.setTag(i-1);
                    face.setClickable(true);
                    face.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final String faceName = mCodes[(int)v.getTag()];
                            AppCompatEditText edtMessage = (AppCompatEditText) getActivity().findViewById(R.id.edtMessage);
                            Editable inputContent = edtMessage.getText();
                            int startPosition = inputContent.length();
                            inputContent.append(faceName);

                            SpannableStringBuilder ssb = new SpannableStringBuilder(inputContent);
                            BitmapDrawable dr = new BitmapDrawable(LiteDoodApplication.getMainActivity().getResources(),mFaces[(int)v.getTag()]);
                            dr.setBounds(0,0, dr.getIntrinsicWidth(),dr.getIntrinsicHeight());

                            ImageSpan face = new ImageSpan(dr, ImageSpan.ALIGN_BOTTOM);
                            ssb.setSpan(face, startPosition, startPosition + faceName.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                            //edtMessage.setCompoundDrawablesWithIntrinsicBounds(null, null, dr, null);
                            edtMessage.setText(ssb);
                            edtMessage.setSelection(startPosition + faceName.length());

                        }
                    });
                }
                else break;
            }
        }
        return result;
    }


}
