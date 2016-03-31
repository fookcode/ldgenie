package com.vrv.ldgenie.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.vrv.imsdk.model.Chat;
import com.vrv.ldgenie.R;

/**
 * Created by kinee on 2016/3/31.
 */
public class MessageActivity extends AppCompatActivity {
    private static final String WHO = "MESSAGE_WHO";
    private Chat who;
    private Toolbar toolbarMessage;

    public static void startMessageActivity(Activity activity, Chat who) {
        Intent intent = new Intent();
        intent.putExtra(WHO, who);
        intent.setClass(activity, MessageActivity.class);
        activity.startActivity(intent);
        activity.finish();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        who = getIntent().getParcelableExtra(WHO);

        initToolbar();

    }

    private void initToolbar() {
        toolbarMessage = (Toolbar)findViewById(R.id.toolbarMessage);
        setSupportActionBar(toolbarMessage);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(who.getName());

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                //NavUtils.navigateUpFromSameTask(this);
                MainActivity.startMainActivity(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
