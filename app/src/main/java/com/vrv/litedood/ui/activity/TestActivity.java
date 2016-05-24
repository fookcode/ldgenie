package com.vrv.litedood.ui.activity;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.vrv.litedood.R;

/**
 * Created by yanqiyang on 2016/5/24.
 */
public class TestActivity extends AppCompatActivity {
    private static final String TAG = TestActivity.class.getCanonicalName();

    public TestActivity() {
        super();
        setContentView(R.layout.a_test_layout);
        FragmentManager manager = getSupportFragmentManager();
        Log.v(TAG, String.valueOf(manager.getFragments().size()));
    }


}
