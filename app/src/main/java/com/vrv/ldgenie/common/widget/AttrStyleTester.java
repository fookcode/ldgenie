package com.vrv.ldgenie.common.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.vrv.ldgenie.R;

/**
 * Created by kinee on 2016/3/28.
 */
public class AttrStyleTester extends ImageButton {
    public AttrStyleTester(Context context, AttributeSet set) {
        super(context, set);
        //测试以下函数用法
        //参数set，类型AttributeSet,在Layout定义时为组件设置的所有显示定义的属性值集合
        //参数R.styleable.attrStyleTest，类型int[]，所有需要取值的数据集合，这里一般为自定义的styleable属性的集合，集合内的值是通过androidStudio转换过的attr.ID
        //参数0,类型int,网上说是在Theme中搜索此属性，未证实。
        //参数R.style.selfStyle，类型int，如果在第一个参数中未找到第二个参数中所指定的某个属性，则在此参数的定义中查找其默认值
        TypedArray array = context.obtainStyledAttributes(set, R.styleable.testAttr, 0, R.style.testStyle);
        Log.v(this.getClass().getName(), String.valueOf(array.getIndexCount()));          //实际为R.styleable.attrStyleTest定义中的属性数量和

        //arry为TypeArray类型，也就是把attr.ID进行了转义成实际值，从此转义的数组中取回我们想要的属性定义值
        String testString = array.getString(R.styleable.testAttr_attr1);

        if (null == testString) testString = "null";
        Log.v(this.getClass().getName(), testString);

        testString = array.getString(R.styleable.testAttr_attr3);
        if (null == testString) testString = "null";
        Log.v(this.getClass().getName(), testString);
        array.recycle();
    }


}
