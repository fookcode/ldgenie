package com.vrv.litedood.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.ListViewCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.vrv.imsdk.SDKManager;
import com.vrv.imsdk.model.Contact;
import com.vrv.litedood.LiteDoodApplication;
import com.vrv.litedood.R;
import com.vrv.litedood.adapter.ItemModelSelectorSeekerAdapter;
import com.vrv.litedood.adapter.ItemModelSelectorAdapter;
import com.vrv.litedood.common.LiteDood;
import com.vrv.litedood.common.sdk.action.RequestHandler;
import com.vrv.litedood.common.sdk.action.RequestHelper;

import java.util.ArrayList;

/**
 * Created by kinee on 2016/5/8.
 */
public class ItemModelSelectorActivity extends AppCompatActivity {
    private final String TAG = ItemModelSelectorActivity.class.getSimpleName();

    private ArrayList<Long> mItemSelectedList;

    public static void startItemModelSelectorActivity(AppCompatActivity activity) {
        Intent intent = new Intent();
        intent.setClass(activity, ItemModelSelectorActivity.class);
        activity.startActivity(intent);
    }

    public static void startItemModelSelectorActivity(AppCompatActivity activity, ArrayList<Integer> itemSelectedList) {
        Intent intent = new Intent();
        intent.putIntegerArrayListExtra("itemSelectedList", itemSelectedList);
        intent.setClass(activity, ItemModelSelectorActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_model_selector);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tbItemModelSelector);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setTitle("发起群聊");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ArrayList<Contact> userList = SDKManager.instance().getContactList().getList();

        if (userList != null) {
            ItemModelSelectorAdapter<Contact> adapter = new ItemModelSelectorAdapter<>(this, LiteDood.reorganizeGroups(userList, Contact.class));
            adapter.setCheckBoxVisibility(View.VISIBLE);
            final ListViewCompat lvItemModelSelector = (ListViewCompat) findViewById(R.id.lvItemModelSelector);
            adapter.setCheckedChangeListener( new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    setSelectedItem((Long)buttonView.getTag(), isChecked);
                }
            });
            lvItemModelSelector.setAdapter(adapter);
            lvItemModelSelector.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (view.getTag() != null) {
                        ItemModelSelectorAdapter.ViewHolder viewHolder = (ItemModelSelectorAdapter.ViewHolder)view.getTag();

                        if (viewHolder.mType == ItemModelSelectorAdapter.ITEM_TYPE.ITEM) {

                            final AppCompatCheckBox checker = (AppCompatCheckBox) view.findViewById(R.id.cbItemModelChecker);
                            AppCompatButton btnCreateGroup = (AppCompatButton)findViewById(R.id.btnCreateGroup);
                            if (!checker.isChecked()) {
                                checker.setChecked(true);
                                if (!mItemSelectedList.contains(id))
                                    mItemSelectedList.add(id);

                                btnCreateGroup.setEnabled(true);
                            }
                            else {
                                checker.setChecked(false);
                                mItemSelectedList.remove(id);
                                if (mItemSelectedList.size()<=1) {
                                    btnCreateGroup.setEnabled(false);
                                }
                            }
                        }
                    }
                }
            });

            ItemModelSelectorSeekerAdapter seekerAdapter = new ItemModelSelectorSeekerAdapter(this, userList);
            final ListViewCompat lvItemModelSelectorSeeker = (ListViewCompat)findViewById(R.id.lvItemModelSelectorSeeker);
            lvItemModelSelectorSeeker.setAdapter(seekerAdapter);
            lvItemModelSelectorSeeker.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Character spellFirst = (Character) parent.getItemAtPosition(position);
                    if (spellFirst != null) {
                        Integer pos = LiteDood.getSeekPositionMap().get(spellFirst);
                        if (pos != null)
                            lvItemModelSelector.setSelection(pos);
                    }
                }
            });
        }

        mItemSelectedList = new ArrayList<>();
        mItemSelectedList.add(LiteDoodApplication.getAppContext().getMyself().getId());

        setCreateGroupAction();
    }

    public void setSelectedItem(long id, boolean isSelected) {
        AppCompatButton btnCreateGroup = (AppCompatButton) findViewById(R.id.btnCreateGroup);
        if (isSelected) {
            if (!mItemSelectedList.contains(id))
                mItemSelectedList.add(id);
            if (!btnCreateGroup.isEnabled()) btnCreateGroup.setEnabled(true);
        }
        else {
            mItemSelectedList.remove(id);
            if (mItemSelectedList.size() <= 1) btnCreateGroup.setEnabled(false);
        }
    }

    private void setCreateGroupAction() {
        AppCompatButton btnCreateGroup =(AppCompatButton) findViewById(R.id.btnCreateGroup);
        btnCreateGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemSelectedList.size() > 1)
                    RequestHelper.createGroup(mItemSelectedList, new ItemModelSelectorHandler(ItemModelSelectorActivity.this));
            }
        });
    }

    class ItemModelSelectorHandler extends RequestHandler {
        private Activity mActivity;

        public ItemModelSelectorHandler(Activity activity) {
            mActivity = activity;
        }
        @Override
        public void handleSuccess(Message msg) {
            Toast.makeText(ItemModelSelectorActivity.this, "群创建成功", Toast.LENGTH_SHORT);

        }
    }
}
