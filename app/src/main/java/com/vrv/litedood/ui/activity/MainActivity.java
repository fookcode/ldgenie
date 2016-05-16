package com.vrv.litedood.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.ListViewCompat;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.vrv.imsdk.SDKManager;
import com.vrv.imsdk.model.Contact;
import com.vrv.litedood.LiteDoodApplication;
import com.vrv.litedood.R;
import com.vrv.litedood.adapter.ConfigureAdapter;
import com.vrv.litedood.common.LiteDood;
import com.vrv.litedood.common.sdk.action.RequestHandler;
import com.vrv.litedood.common.sdk.action.RequestHelper;
import com.vrv.litedood.common.sdk.utils.BaseInfoBean;
import com.vrv.litedood.common.widget.ButtonActiveFragmentOnClickListener;
import com.vrv.litedood.ui.activity.MainFragment.ChatFragment;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


//mainid:4328622264

public class MainActivity extends AppCompatActivity {
    public static final String TAG = MainActivity.class.getSimpleName();

	public static final String TAG_MESSAGE_FRAGMENT = "MESSAGE_FRAGMENT";
	public static final String TAG_CONTACTS_FRAGMENT = "CONTACTS_FRAGMENT";
	public static final String TAG_PANDORA_FRAGMENT = "PANDORA_FRAGMENT";
    public static final String TAG_CURRENT_FRAGMENT = "CURRENT_FRAGMENT";

    private static final int TYPE_HANDLER_LOGOUT = 1;

    private Map<String, Fragment> mFragmentsMap = new HashMap<String, Fragment>();

    private int clientWidth, clientHeight;

    private DrawerLayout mConfigDrawer;
    private ActionBarDrawerToggle mConfigDrawerToggleListener;               //抽屉Listener
    private LinearLayoutCompat mConfigDrawerPanel;
    private ListViewCompat mConfigDrawerPanelListView;

    private Toolbar mToobar;

    private FragmentManager mFragmentManager;

    private LinearLayoutCompat mMainActionBar;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.v(TAG, "onSaveInstanceState");
    }

    public static void startMainActivity(Activity activity) {
        Intent intent = new Intent();
        intent.setClass(activity, MainActivity.class);
        activity.startActivity(intent);

        activity.finish();
    }

	@Override
	protected void onCreate(Bundle savedInstanceState) {
        LiteDoodApplication.setMainActivity(this);
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
        mConfigDrawer = (DrawerLayout)findViewById(R.id.drawerConfig);
        mConfigDrawerPanel = (LinearLayoutCompat)findViewById(R.id.drawerConfigPanel);
        LayoutParams layoutParams = mConfigDrawerPanel.getLayoutParams();
        layoutParams.width = clientWidth * 4 / 5;
        mConfigDrawerPanel.setLayoutParams(layoutParams);

        mConfigDrawerPanelListView = (ListViewCompat)findViewById(R.id.drawerConfigListView);
        mConfigDrawerPanelListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //
            }
        });

        mConfigDrawerPanelListView.setAdapter(new ConfigureAdapter(this));//(new ArrayAdapter<String>(this, R.layout.item_drawer_list, getResources().getStringArray(R.array.configItems)));
        mConfigDrawerToggleListener = new ActionBarDrawerToggle(this, mConfigDrawer, R.string.drawer_open, R.string.drawer_close) {
            private void onDrawClosed(View v) {
                super.onDrawerClosed(v);
                //Toast.makeText(MainActivity.this, "onDrawClosed", Toast.LENGTH_SHORT).show();
            }

            private void onDrawOpened(View v) {
                super.onDrawerOpened(v);
                //Toast.makeText(MainActivity.this, "onDrawOpened", Toast.LENGTH_SHORT).show();
            }
        };
        mConfigDrawer.addDrawerListener(mConfigDrawerToggleListener);

        final Contact myself = LiteDoodApplication.getAppContext().getMyself();
        if (myself != null) {
            AppCompatImageView ivAvatar = (AppCompatImageView) findViewById(R.id.ivMyProfileAvatar);
            String avatarPath = myself.getAvatar();
            if ((null != avatarPath) && (!avatarPath.isEmpty())) {
                File fAvatar = new File(avatarPath);
                if ((fAvatar.isDirectory()) || (!fAvatar.exists())) {

                    ivAvatar.setImageResource(R.drawable.ic_launcher);
                    //boolean result = RequestHelper.getUserInfo(chat.getId(), new ChatRequlestHandler(context, viewHolder, TYPE_GET_USER));
                    //if (!result) {Log.v(TAG, "获取用户数据失败");}
                }
                else {
                    Bitmap bitmapAvatar = BitmapFactory.decodeFile(avatarPath);
                    ivAvatar.setImageDrawable(new BitmapDrawable(getResources(), LiteDood.getRoundedCornerBitmap(bitmapAvatar, 180)));
//                    ivAvatar.setImageBitmap(bitmapAvatar);
//                    ArrayList<Drawable> list = new ArrayList<>();
//                    list.add(new BitmapDrawable(getResources(), bitmapAvatar));
//                    list.add(getResources().getDrawable(R.drawable.sp_test_item));
//                    LayerDrawable ld = new LayerDrawable(list.toArray(new Drawable[list.size()]));
//                    StateListDrawable sld = new StateListDrawable();
//                    sld.addState(new int[] {}, ld);
//                    //ivAvatar.setImageDrawable(sld);
//                    ivAvatar.setBackgroundDrawable(sld);


                }
            }

            View.OnClickListener listener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ContactsCardActivity.startContactCardActivity(MainActivity.this, BaseInfoBean.contact2BaseInfo(myself), Intent.ACTION_EDIT);
                }
            };

            ivAvatar.setOnClickListener(listener);

            AppCompatTextView tvName = (AppCompatTextView)findViewById(R.id.tvMyProfileName);
            tvName.setText(myself.getName());
            tvName.setOnClickListener(listener);

            String sSign = myself.getSign();
            if(!sSign.isEmpty()) {
                AppCompatTextView tvSign = (AppCompatTextView)findViewById(R.id.tvMyProfileSign);
                tvSign.setText("”" + sSign);
            }
        }

        AppCompatButton btnLogout = (AppCompatButton)findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestHelper.logout(new MainRequestHandler(TYPE_HANDLER_LOGOUT));
                SDKManager.instance().destroy();
            }
        });
    }

    private void initToolbar() {
        mToobar = (Toolbar) findViewById(R.id.tbMain);
        mToobar.setNavigationIcon(R.drawable.ic_drawer);

        setSupportActionBar(mToobar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        AppCompatImageView ivMainMenu = (AppCompatImageView)findViewById(R.id.ivMainMenu);
        ivMainMenu.setOnClickListener(new View.OnClickListener() {
            private final static String TITLE ="title";
            private final static String ICON="icon";

            private HashMap<String, Object> newMenuItem(String title, int ResId) {
                HashMap<String, Object> item = new HashMap<String, Object>();
                item.put(TITLE, title);
                item.put(ICON, ResId);
                return item;
            }

            @Override
            public void onClick(View v) {
                final ListPopupWindow popupWindow = new ListPopupWindow(MainActivity.this);
                List<HashMap<String, Object>> data = new ArrayList<>();
                data.add(newMenuItem(getResources().getString(R.string.main_menu_item_add_user), R.drawable.ic_launcher));
                data.add(newMenuItem(getResources().getString(R.string.main_menu_item_add_group), R.drawable.ic_launcher));

                ListAdapter adapter = new SimpleAdapter(
                        MainActivity.this,
                        data,
                        R.layout.item_main_activity_menu, // 数组注入的布局
                        new String[] {TITLE, ICON}, // 布局中各视图数据源的KEY,是data中的map的key
                        new int[] {R.id.tvMainMenuItemText, R.id.ivMainMenuItemIcon}); //布局中的视图ResId集合，这最后两个参数值一一对应


                popupWindow.setAnchorView(v);
                popupWindow.setAdapter(adapter);
                popupWindow.setWidth(320);
                popupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        switch (position) {
                            case 0:
                                FindContactsActivity.startFindContactsActivity(MainActivity.this);
                                //Toast.makeText(MainActivity.this, "item1", Toast.LENGTH_SHORT).show();
                                if (popupWindow.isShowing()) popupWindow.dismiss();
                                break;
                            case 1:
                                ItemModelSelectorActivity.startItemModelSelectorActivity(MainActivity.this);
                                //Toast.makeText(MainActivity.this, "item2", Toast.LENGTH_SHORT).show();
                                if (popupWindow.isShowing()) popupWindow.dismiss();
                                break;
                        }
                    }
                });
                popupWindow.show();
            }
        });
    }

    private void initFragment() {
        //初始化装载聊天列表, 这部分操作可以使用ViewPager代替
        ChatFragment fragment = new ChatFragment();
        mFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.layoutFragmentContainer, fragment, TAG_MESSAGE_FRAGMENT);
        fragmentTransaction.commit();

        mFragmentsMap.put(TAG_MESSAGE_FRAGMENT, fragment);
        mFragmentsMap.put(TAG_CURRENT_FRAGMENT, fragment);
    }

    private void initActionBar() {
        //设置下部动作条高度
        mMainActionBar = (LinearLayoutCompat) findViewById(R.id.llActionBar);
        //width = mMainActionBar.getHeight();
        LayoutParams actionBarParams = mMainActionBar.getLayoutParams();
        actionBarParams.height = clientHeight * 8 / 90;
        mMainActionBar.setLayoutParams(actionBarParams);

        //设置动作按钮位置、大小
        List<AppCompatImageButton> buttons = new ArrayList<AppCompatImageButton>();
        AppCompatImageButton btnMessage = (AppCompatImageButton) findViewById(R.id.btnMessage);
        btnMessage.setTag(TAG_MESSAGE_FRAGMENT);
        buttons.add(btnMessage);

        AppCompatImageButton btnContacts = (AppCompatImageButton) findViewById(R.id.btnContacts);
        btnContacts.setTag(TAG_CONTACTS_FRAGMENT);
        buttons.add(btnContacts);

        AppCompatImageButton btnPandora = (AppCompatImageButton) findViewById(R.id.btnPandora);
        btnPandora.setTag(TAG_PANDORA_FRAGMENT);
        buttons.add(btnPandora);

        for(AppCompatImageButton btn: buttons) {
            LayoutParams layoutParams = btn.getLayoutParams();
            layoutParams.width = clientWidth / 3;
            btn.setLayoutParams(layoutParams);
            btn.setOnClickListener(new ButtonActiveFragmentOnClickListener(mFragmentsMap, mFragmentManager));
        }
    }

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.main, menu);
//		return true;
//	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up sl_main_message_button, so long
		// as you specify a parent activity in AndroidManifest.xml.
        if (mConfigDrawerToggleListener.onOptionsItemSelected(item)) {          //当HOME健点击时自动显示Drawer
            return true;
        }

//		int id = item.getItemId();
//		if (id == R.id.action_settings) {
//			return true;
//		}

		return super.onOptionsItemSelected(item);
	}

    @Override
    protected void onStop() {
        super.onStop();
        Log.v(TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.v(TAG, "onDestory");

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.v(TAG, "onPause");

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.v(TAG, "onResume");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.v(TAG, "onRestart");

    }

    class MainRequestHandler extends RequestHandler {

        private int mType;

        public MainRequestHandler(int type) {
            mType = type;
        }

        @Override
        public void handleSuccess(Message msg) {
            switch (mType) {
                case TYPE_HANDLER_LOGOUT:
                    LoginActivity.startLoginActivity(MainActivity.this, true);
                    LiteDoodApplication.getAppContext().setMyself(null);
                    break;

                default:
                    break;

            }
        }

        @Override
        public void handleFailure(int code, String message) {
            super.handleFailure(code, message);
            switch (mType) {
                case TYPE_HANDLER_LOGOUT:
                    Log.v(TAG, "退出异常 " + code + ": " + message);

                    String hint = "退出异常";
                    if ((message != null) && (!message.isEmpty())) {
                        hint = hint + ": " + message;
                    } else {
                        hint = hint + ", 请与管理员联系";
                    }
                    Toast.makeText(MainActivity.this, hint, Toast.LENGTH_SHORT).show();
                    break;

            }
        }

    }

}
