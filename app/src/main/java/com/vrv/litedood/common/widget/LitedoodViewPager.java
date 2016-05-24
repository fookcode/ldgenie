package com.vrv.litedood.common.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by yanqiyang on 2016/5/24.
 */
public class LitedoodViewPager extends ViewPager {

    private boolean enabled = false;

    public LitedoodViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LitedoodViewPager(Context context) {
        super(context);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (this.enabled) {
            return super.onTouchEvent(event);
        }

        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (this.enabled) {
            return super.onInterceptTouchEvent(event);
        }

        return false;
    }

    public void setPagingEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
