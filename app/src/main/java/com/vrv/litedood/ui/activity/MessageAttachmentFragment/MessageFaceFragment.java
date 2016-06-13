package com.vrv.litedood.ui.activity.MessageAttachmentFragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewParentCompat;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vrv.litedood.R;
import com.vrv.litedood.common.LiteDood;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yanqiyang on 2016/5/24.
 */
public class MessageFaceFragment extends Fragment {

    private static final String TAG = MessageFaceFragment.class.getSimpleName();

    public MessageFaceFragment() {
        super();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        final View result =  inflater.inflate(R.layout.fragment_message_attachment_face, null);

        ViewPager vpFaces = (ViewPager)result.findViewById(R.id.vpMessageAttachmentFace);
        final List<Fragment> fragments = createFaceFragments();
        vpFaces.setAdapter(new FacePagerAdapter(getActivity().getSupportFragmentManager(), fragments));

        final AppCompatTextView tvFacePageIndicator = (AppCompatTextView)result.findViewById(R.id.tvFacePageIndicator);
        tvFacePageIndicator.setText(getIndicatorString(0, fragments.size()));

        vpFaces.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.v(TAG, String.valueOf(position));

                tvFacePageIndicator.setText(getIndicatorString(position, fragments.size()));
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        return result;

    }

    private String getIndicatorString(int position, int count) {
        String dot = "·";
        String selectedDot = "•";
        String indicator = "";
        for (int i = 0; i < count; i ++) {
            if (position == i) indicator += selectedDot;
            else indicator += dot;
        }
        return  indicator;
    }

    private List<Fragment> createFaceFragments() {
        ArrayList<Fragment> fragments = new ArrayList<>();
        ArrayList<Bitmap[]> faceForPage = LiteDood.getFaces();
        for (int i = 0; i < faceForPage.size(); i ++) {
            MessageFacePageFragment facePage = new MessageFacePageFragment();
            facePage.setFaces(faceForPage.get(i));
            fragments.add(facePage);
        }
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
            return super.getPageTitle(position);
        }
    }
}
