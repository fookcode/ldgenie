package com.vrv.litedood.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.vrv.litedood.R;
import com.vrv.litedood.ui.activity.ForgotPasswordFragment.Step1Fragment;
import com.vrv.litedood.ui.activity.ForgotPasswordFragment.Step2Fragment;
import com.vrv.litedood.ui.activity.ForgotPasswordFragment.StepFinishFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kinee on 2016/4/19.
 */
public class ForgotPasswordActivity extends AppCompatActivity {
    public static int STEP1 = 0;
    public static int STEP2 = 1;
    public static int STEP_FINISHED = 2;

    private FPFragmentPagerAdapter adapter;
    private List<Fragment> fragments = new ArrayList<>();

    public static void startForgotPasswordActivity(Activity activity) {
        Intent intent = new Intent();
        intent.setClass(activity, ForgotPasswordActivity.class);
        activity.startActivity(intent);
        activity.finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home :{
                LoginActivity.startLoginActivity(this);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        ViewPager pager = (ViewPager)findViewById(R.id.vpForgotPassword);

        Step1Fragment step1 = new Step1Fragment();
        Step2Fragment step2 = new Step2Fragment();
        StepFinishFragment stepFinish = new StepFinishFragment();
        fragments.add(step1);
        fragments.add(step2);
        fragments.add(stepFinish);
        adapter = new FPFragmentPagerAdapter(getSupportFragmentManager(), fragments, this);
        pager.setAdapter(adapter);
        pager.setCurrentItem(STEP1);

        Toolbar toolbar = (Toolbar)findViewById(R.id.tbForgotPassword);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    class FPFragmentPagerAdapter extends FragmentPagerAdapter {
        private List<Fragment> pages;
        private Activity activity;

        public FPFragmentPagerAdapter(FragmentManager fm, List<Fragment> pages, Activity activity) {
            super(fm);
            this.pages = pages;
            this.activity = activity;
        }

        @Override
        public Fragment getItem(int position) {
            return pages.get(position);
        }

        @Override
        public int getCount() {
            if(pages != null) return pages.size();
            return 0;
        }
    }
}
