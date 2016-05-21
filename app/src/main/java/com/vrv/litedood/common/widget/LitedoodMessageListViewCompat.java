package com.vrv.litedood.common.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.ListViewCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by kinee on 2016/5/21.
 */
public class LitedoodMessageListViewCompat extends ListViewCompat {
    public LitedoodMessageListViewCompat(Context context) {
        super(context);
    }

    public LitedoodMessageListViewCompat(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public LitedoodMessageListViewCompat(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        if (h<oldh) {
            this.setSelection(this.getCount() -1);
        }
    }
}
