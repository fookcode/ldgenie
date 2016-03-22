package com.vrv.ldgenie.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;

import com.vrv.ldgenie.R;
import com.vrv.ldgenie.common.widget.ButtonActiveFragmentOnClickListener;
import com.vrv.ldgenie.ui.activity.fragment.MessageFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

	public static final String TAG_MESSAGE_FRAGMENT = "MESSAGE_FRAGMENT";
	public static final String TAG_CONTACTS_FRAGMENT = "CONTACTS_FRAGMENT";
	public static final String TAG_PANDORA_FRAGMENT = "PANDORA_FRAGMENT";
    public static final String TAG_CURRENT_FRAGMENT = "CURRENT_FRAGMENT";

	private Map<String, Fragment> fragments = new HashMap<String, Fragment>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//无标题栏
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		//无状态栏
		//this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_main);

		//ActionBar图标起始位置横向：1/14 ActionBar纵向高度：8/90
		DisplayMetrics outMetrics = new DisplayMetrics();
		this.getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
		int height = outMetrics.heightPixels;
		int width = outMetrics.widthPixels;

		//设置上部状态条高度
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
		LayoutParams toolbarLayoutParams = toolbar.getLayoutParams();
        toolbarLayoutParams.height = height * 7 / 90;
        toolbar.setLayoutParams(toolbarLayoutParams);
        toolbar.setLogo(R.drawable.ic_launcher);
        setSupportActionBar(toolbar);

        //初始化装载聊天列表
        MessageFragment fragment = new MessageFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_layout, fragment, TAG_MESSAGE_FRAGMENT);
        fragmentTransaction.commit();

        fragments.put(TAG_MESSAGE_FRAGMENT, fragment);
        fragments.put(TAG_CURRENT_FRAGMENT, fragment);

		//设置下部动作条高度
		LinearLayout actionBar = (LinearLayout) findViewById(R.id.actionBar);
		//width = actionBar.getHeight();
		LayoutParams actionBarParams = actionBar.getLayoutParams();
		actionBarParams.height = height * 8 / 90;
		actionBar.setLayoutParams(actionBarParams);

		//设置动作按钮位置、大小
		List<Button> buttons = new ArrayList<Button>();
		Button btnMessage = (Button) findViewById(R.id.message);
        btnMessage.setTag(TAG_MESSAGE_FRAGMENT);
		buttons.add(btnMessage);

		Button btnContacts = (Button) findViewById(R.id.contacts);
        btnContacts.setTag(TAG_CONTACTS_FRAGMENT);
		buttons.add(btnContacts);

		Button btnPandora = (Button) findViewById(R.id.pandora);
		btnPandora.setTag(TAG_PANDORA_FRAGMENT);
		buttons.add(btnPandora);
		//Log.d("x", "z");

		for(Button btn: buttons) {
			LayoutParams layoutParams = btn.getLayoutParams();
			layoutParams.width = width / 3;
			btn.setLayoutParams(layoutParams);
            btn.setOnClickListener(new ButtonActiveFragmentOnClickListener(fragments, fragmentManager));
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}



}
