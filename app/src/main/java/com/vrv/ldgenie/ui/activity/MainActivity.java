package com.vrv.ldgenie.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.ListViewCompat;

import com.vrv.imsdk.SDKManager;
import com.vrv.ldgenie.R;
import com.vrv.ldgenie.bpo.GenieRequestHandler;
import com.vrv.ldgenie.common.sdk.action.RequestHelper;
import com.vrv.ldgenie.common.widget.ButtonActiveFragmentOnClickListener;
import com.vrv.ldgenie.ui.activity.fragment.ChatFragment;

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

    private int clientWidth, clientHeight;

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;               //抽屉Listener
    private LinearLayoutCompat drawerPanel;
    private  ListViewCompat drawerListView;

    private Toolbar  toolBar;

    private FragmentManager fragmentManager;

    private LinearLayoutCompat actionBar;

    public static void startMainActivity(Activity activity) {
        Intent intent = new Intent();
        intent.setClass(activity, MainActivity.class);
        activity.startActivity(intent);
        activity.finish();
    }

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        //使用Theme,以下两个设置由Theme完成
		//无标题栏
		//this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		//无状态栏
		//this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.activity_main);

		getClientSize();

        initDrawer();

        initToolbar();

        initFragment();

		initActionBar();

	}

    private void getClientSize() {
        //ActionBar图标起始位置横向：1/14 ActionBar纵向高度：8/90
        DisplayMetrics outMetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        clientHeight = outMetrics.heightPixels;
        clientWidth = outMetrics.widthPixels;
    }

    private void initDrawer() {
        drawerLayout = (DrawerLayout)findViewById(R.id.configDrawer);
        drawerPanel = (LinearLayoutCompat)findViewById(R.id.drawerPanel);
        LayoutParams layoutParams = drawerPanel.getLayoutParams();
        layoutParams.width = clientWidth * 4 / 5;
        drawerPanel.setLayoutParams(layoutParams);

        drawerListView = (ListViewCompat)findViewById(R.id.drawerListView);
        drawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.v(view.getClass().getName(), String.valueOf(view.getId()) + String.valueOf(position));
                getSupportActionBar().setDisplayShowTitleEnabled(false);
            }
        });

        drawerListView.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, getResources().getStringArray(R.array.configItems)));
        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout, R.string.drawer_open, R.string.drawer_close) {
            private void onDrawClosed(View v) {
                super.onDrawerClosed(v);
                //Toast.makeText(MainActivity.this, "onDrawClosed", Toast.LENGTH_SHORT).show();
            }

            private void onDrawOpened(View v) {
                super.onDrawerOpened(v);
                //Toast.makeText(MainActivity.this, "onDrawOpened", Toast.LENGTH_SHORT).show();
            }
        };
        drawerLayout.addDrawerListener(actionBarDrawerToggle);

        AppCompatButton btnLogout = (AppCompatButton)findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SDKManager.instance().destroy();
                RequestHelper.logout(new GenieRequestHandler(GenieRequestHandler.HANDLER_LOGOUT, MainActivity.this));
            }
        });
    }

    private void initToolbar() {
        //设置上部状态条高度
       toolBar = (Toolbar) findViewById(R.id.toolBar);
        //LayoutParams toolbarLayoutParams = toolbar.getLayoutParams();
        //toolbarLayoutParams.height = height * 7 / 90;
        //toolbar.setLayoutParams(toolbarLayoutParams);
        //toolbar.setLogo(R.drawable.ic_launcher);
        setSupportActionBar(toolBar);
        toolBar.setNavigationIcon(R.drawable.ic_drawer);
        toolBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        ActionBar supportActionBarr = getSupportActionBar();
        //supportActionBarr.setDisplayShowTitleEnabled(false);
        //supportActionBarr.setDisplayHomeAsUpEnabled(true);
        supportActionBarr.setHomeButtonEnabled(true);
    }

    private void initFragment() {
        //初始化装载聊天列表, 这部分操作可以使用ViewPager代替
        ChatFragment fragment = new ChatFragment();
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_layout, fragment, TAG_MESSAGE_FRAGMENT);
        fragmentTransaction.commit();

        fragments.put(TAG_MESSAGE_FRAGMENT, fragment);
        fragments.put(TAG_CURRENT_FRAGMENT, fragment);
    }

    private void initActionBar() {
        //设置下部动作条高度
        actionBar = (LinearLayoutCompat) findViewById(R.id.actionBar);
        //width = actionBar.getHeight();
        LayoutParams actionBarParams = actionBar.getLayoutParams();
        actionBarParams.height = clientHeight * 8 / 90;
        actionBar.setLayoutParams(actionBarParams);

        //设置动作按钮位置、大小
        List<AppCompatButton> buttons = new ArrayList<AppCompatButton>();
        AppCompatButton btnMessage = (AppCompatButton) findViewById(R.id.message);
        btnMessage.setTag(TAG_MESSAGE_FRAGMENT);
        buttons.add(btnMessage);

        AppCompatButton btnContacts = (AppCompatButton) findViewById(R.id.contacts);
        btnContacts.setTag(TAG_CONTACTS_FRAGMENT);
        buttons.add(btnContacts);

        AppCompatButton btnPandora = (AppCompatButton) findViewById(R.id.pandora);
        btnPandora.setTag(TAG_PANDORA_FRAGMENT);
        buttons.add(btnPandora);

        for(AppCompatButton btn: buttons) {
            LayoutParams layoutParams = btn.getLayoutParams();
            layoutParams.width = clientWidth / 3;
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
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {          //当HOME健点击时自动显示Drawer
            return true;
        }

//		int id = item.getItemId();
//		if (id == R.id.action_settings) {
//			return true;
//		}

		return super.onOptionsItemSelected(item);
	}

}
