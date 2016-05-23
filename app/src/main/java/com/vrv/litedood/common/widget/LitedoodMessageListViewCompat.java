package com.vrv.litedood.common.widget;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.ListViewCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Window;

/**
 * Created by kinee on 2016/5/21.
 */
public class LitedoodMessageListViewCompat extends ListViewCompat {
    public final static String TAG = LitedoodMessageListViewCompat.class.getSimpleName();
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

        float g = getResources().getDisplayMetrics().density;
        Log.v(TAG, "density: " + String.valueOf(g));
        float g1 = (float)getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT;
        Log.v(TAG, "densityDpi / Density_Default: " + String.valueOf(g1));

        Rect rect= new Rect();
        getWindowVisibleDisplayFrame(rect);

        int contentViewHeight = ((AppCompatActivity)getContext()).getWindow().findViewById(Window.ID_ANDROID_CONTENT).getHeight();//顶层GroupView的高度，不含status和title，此例中也就是ListView的父对象
        Log.v(TAG, "Android Content View height: " + String.valueOf(contentViewHeight));

        if (((AppCompatActivity)getContext()).getWindow().getDecorView() == this.getRootView()) {
            Log.v(TAG, "getWindow().getDecorView() == View.getRootView()");
        }
        int keyboardHeight = this.getRootView().getHeight() - (((LinearLayoutCompat)this.getParent()).getHeight() + rect.top);
        float keyboardHeightDP = keyboardHeight / g;
        Log.v(TAG, "dp: " + String.valueOf(keyboardHeightDP));



    }
}
