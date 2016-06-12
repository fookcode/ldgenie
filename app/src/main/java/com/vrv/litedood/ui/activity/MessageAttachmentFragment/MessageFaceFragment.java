package com.vrv.litedood.ui.activity.MessageAttachmentFragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewParentCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vrv.litedood.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yanqiyang on 2016/5/24.
 */
public class MessageFaceFragment extends Fragment {

    public MessageFaceFragment() {
        super();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        View result =  inflater.inflate(R.layout.fragment_message_attachment_face, null);
        ViewPager vpFaces = (ViewPager)result.findViewById(R.id.vpMessageAttachmentFace);
        vpFaces.setAdapter(new FacePagerAdapter(getActivity().getSupportFragmentManager(), createFaceFragments()));

        return result;

    }

    private List<Fragment> createFaceFragments() {
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(new MessageFacePageFragment());
        return fragments;

    }

    class FacePagerAdapter extends FragmentPagerAdapter {
        private List<Fragment> mFragments;

        public FacePagerAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            mFragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            if (mFragments != null)
                return mFragments.size();
            else return 0;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "hello";//super.getPageTitle(position);
        }
    }
}
