package com.vrv.litedood.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.vrv.imsdk.model.Contact;

/**
 * Created by kinee on 2016/5/10.
 */
public class FindContactsResultActivity extends AppCompatActivity {

    public static void startFindContactsResultActivity(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, FindContactsActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }
}
