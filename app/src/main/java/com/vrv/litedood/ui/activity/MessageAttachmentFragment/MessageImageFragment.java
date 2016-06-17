package com.vrv.litedood.ui.activity.MessageAttachmentFragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.vrv.litedood.LiteDoodApplication;
import com.vrv.litedood.R;
import com.vrv.litedood.common.LiteDood;
import com.vrv.litedood.common.sdk.action.RequestHandler;
import com.vrv.litedood.common.sdk.action.RequestHelper;
import com.vrv.litedood.ui.activity.MessageActivity;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by yanqiyang on 2016/5/24.
 */
public class MessageImageFragment extends Fragment {
    private static final String TAG = MessageImageFragment.class.getSimpleName();

    private static final int ALUMB_REQUEST_CODE = 78;

    private ArrayList<HashMap<String, Object>> mImageFiles;
    private int pos = 0;
    private Activity mActivity;
    private long mTargetID;

    private ArrayList<MessageImagePageFragment> mMessageImagePageFragment = new ArrayList<>();

    public static ArrayList<String> mSelectedImages = new ArrayList<>();

    public MessageImageFragment() {
        super();

        mSelectedImages.clear();
        mImageFiles = LiteDood.getLastImagesPath(LiteDoodApplication.getMainActivity());
    }

    public void setParam(long targetid, Activity activity) {
        mTargetID = targetid;
        mActivity = activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        View result = inflater.inflate(R.layout.fragment_message_attachment_image, null);
        final ViewPager viewPager = (ViewPager)result.findViewById(R.id.vpMessageAttachmentImage);
        addPage(2);
        final ImagePagerAdapter ipa = new ImagePagerAdapter(getFragmentManager());
        viewPager.setAdapter(ipa);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                mCurPosition = position;
//                Log.v(TAG, String.valueOf(position));
            }

            @Override
            public void onPageSelected(int position) {
                //Log.v(TAG, String.valueOf(position));
                if (viewPager.getAdapter().getCount() - position == 1)
                    addPage(1);
                ipa.notifyDataSetChanged();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        AppCompatButton btnSend = (AppCompatButton)result.findViewById(R.id.btnMessageAttachmentSendImage);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendImage(mSelectedImages);
            }
        });

        AppCompatButton btnOpenAlumb = (AppCompatButton)result.findViewById(R.id.btnMessageAttachmentOpenAlbum);
        btnOpenAlumb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent getImage = new Intent(Intent.ACTION_GET_CONTENT);
                getImage.addCategory(Intent.CATEGORY_OPENABLE);
                getImage.setType("image/jpeg");
                startActivityForResult(getImage, ALUMB_REQUEST_CODE);
            }
        });
        return result;
    }

    private void sendImage(final ArrayList<String> images) {
        if (images.size() <= 0) {
            Toast.makeText(LiteDoodApplication.getMainActivity(), "请选择需要发送的图片", Toast.LENGTH_SHORT).show();
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setTitle("发送确认");
        builder.setMessage("确定要将所选的 " + String.valueOf(images.size()) + " 张图片发送给对方？");
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            MessageImageRequestHandler handler = new MessageImageRequestHandler();
            @Override
            public void onClick(DialogInterface dialog, int which) {

                for(String path : images) {
                    RequestHelper.sendImg(mTargetID, path, handler);
                }
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        if ((resultCode != 0) && data != null) {
            switch (requestCode) {
                case ALUMB_REQUEST_CODE:
                    ArrayList<String> images = new ArrayList<>();
                    images.add(LiteDood.getRealPathFromUri(mActivity, data.getData()));
                    sendImage(images);
                    break;
            }
        }

    }

    public void addPage(int pageCount) {

        if (mMessageImagePageFragment.size() >= 5) return;

        int PAGE_IMAGE_COUNT = 3;

        for (int i = 0; i < pageCount; i ++) {
            if (pos <= mImageFiles.size()) {
                ArrayList<HashMap<String, Object>> pageBitmapList = new ArrayList<>();
                int p = 0;
                while (p < PAGE_IMAGE_COUNT) {
                    HashMap map = mImageFiles.get(pos);
                    if (map == null) break;
                    Bitmap bitmap = LiteDood.getScaleBitmapFromFile(map.get("path").toString());
                    if (bitmap != null) {
                        HashMap<String, Object> file = new HashMap<>();
                        file.put("bitmap", bitmap);
                        file.put("path", map.get("path").toString());
                        file.put("pos", p);
                        pageBitmapList.add(file);
                        p++;
                    }
                    pos++;

                }
                MessageImagePageFragment imagePageFragment = new MessageImagePageFragment();
                mMessageImagePageFragment.add(imagePageFragment);
                imagePageFragment.setImageList(pageBitmapList, mMessageImagePageFragment.size());
            }
        }
    }

    private class ImagePagerAdapter extends FragmentPagerAdapter {

        public ImagePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mMessageImagePageFragment.get(position);
        }

        @Override
        public int getCount() {
            return mMessageImagePageFragment.size();
        }
    }

    class MessageImageRequestHandler extends RequestHandler {
        @Override
        public void handleSuccess(Message msg) {
            //Toast.makeText(mActivity, msg.getData().toString(), Toast.LENGTH_SHORT).show();
        }
    }
}
