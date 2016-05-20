package com.vrv.litedood.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.ListViewCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;

import com.vrv.imsdk.SDKManager;
import com.vrv.imsdk.model.Group;
import com.vrv.imsdk.model.GroupList;
import com.vrv.litedood.R;
import com.vrv.litedood.adapter.ContactsGroupAdapter;
import com.vrv.litedood.adapter.ItemModelSelectorSeekerAdapter;
import com.vrv.litedood.common.LiteDood;
import com.vrv.litedood.common.sdk.utils.BaseInfoBean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by kinee on 2016/5/3.
 */
public class ContactsGroupActivity extends AppCompatActivity {

    private final static String TAG =ContactsGroupActivity.class.getSimpleName();
    private static ArrayList<Group> mContactsGroupList = new ArrayList<>();
    private GroupList mGroupList;

    public static void startContactsGroupActivity(Activity activity) {
        Intent intent = new Intent();
        intent.setClass(activity, ContactsGroupActivity.class);
        activity.startActivity(intent);
        if (!(activity instanceof MainActivity)) {
            activity.finish();
        }

    }

    public ContactsGroupActivity() {
        super();

        mGroupList = SDKManager.instance().getGroupList();
        ArrayList groups = mGroupList.getGroups();
        if (mContactsGroupList.size() != groups.size()) {
            mContactsGroupList.clear();;
            mContactsGroupList.addAll(groups);
            Collections.sort(mContactsGroupList, new Comparator<Group>() {
                @Override
                public int compare(Group lhs, Group rhs) {
                    int n1, n2;
                    String name1 = lhs.getName();
                    if ((name1 != null) && (name1.length() > 0)) {
                        n1 = Character.toLowerCase(LiteDood.PinYinUtil.getFullSpell(String.valueOf(name1.charAt(0))).charAt(0));
                    } else n1 = 0;
                    String name2 = rhs.getName();
                    if ((name2 != null) && (name2.length() > 0)) {
                        n2 = Character.toLowerCase(LiteDood.PinYinUtil.getFullSpell(String.valueOf(name2.charAt(0))).charAt(0));
                    } else n2 = 0;
                    return n1 - n2;
                }
            });
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ContactsGroupAdapter contactsGroupAdapter = new ContactsGroupAdapter(this, LiteDood.reorganizeGroups(mContactsGroupList, Group.class));
        setContentView(R.layout.activity_contacts_group);

        Toolbar toolbar = (Toolbar)findViewById(R.id.tbContactGroup);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        setSupportActionBar(toolbar);
        ActionBar bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setTitle(R.string.contacts_group_activity_title);


        final ListViewCompat lvGroupList = (ListViewCompat)findViewById(R.id.lvContactsGroup);
        lvGroupList.setAdapter(contactsGroupAdapter);
        lvGroupList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                MessageActivity.startMessageActivity(ContactsGroupActivity.this,
                        (Group)lvGroupList.getItemAtPosition(position));
//                ContactsGroupCardActivity.startContactGroupCardActivity(ContactsGroupActivity.this,
//                        ((Group)lvGroupList.getItemAtPosition(position)).getId());
            }
        });

        final ListViewCompat groupSeekerListView = (ListViewCompat)findViewById(R.id.lvContactsGroupSeeker);
        groupSeekerListView.setAdapter(new ItemModelSelectorSeekerAdapter(this, mContactsGroupList));
        groupSeekerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Character spellFirst = (Character) parent.getItemAtPosition(position);
                if (spellFirst != null) {
                    Integer pos = LiteDood.getSeekPositionMap().get(spellFirst);
                    if (pos != null)
                        lvGroupList.setSelection(pos);
                }
            }
        });

        final AppCompatImageView ivContactGroupAdd = (AppCompatImageView)findViewById(R.id.ivContactGroupAdd);
        ivContactGroupAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                adapter.show();
                ItemModelSelectorActivity.startItemModelSelectorActivity(ContactsGroupActivity.this);
            }
        });
    }

//    private List<Group> reorganizeGroups(List<Group> groups) {
//        if ((groups == null) || (groups.size() == 0)) return  groups;
//        Character firstSpell = '#';
//        ArrayList<Group> result = new ArrayList<>();
//        ArrayList<Group> specialList = new ArrayList<>();
//        mSeekPositionMap.clear();
//        int index = 0;
//        for(Group group : groups) {
//            Character nameFirstSpell = Character.toUpperCase(PinYinUtil.getFirstSpell(group.getName()).charAt(0));
//
//            if ((nameFirstSpell<65) || (nameFirstSpell> 90)) {
//                specialList.add(group);
//            }
//            else {
//                if (!firstSpell.equals(nameFirstSpell)) {
//                    firstSpell = nameFirstSpell;
//                    Group c = new Group();
//                    c.setId(GROUP_VIEW_HEADER_ID);
//                    c.setName("#" + firstSpell);
//                    result.add(c);
//
//                    mSeekPositionMap.put(firstSpell, index);
//                    index++;
//                }
//                index++;
//                result.add(group);
//            }
//
//        }
//        if (specialList.size() > 0) {
//            Group c = new Group();
//            c.setId(GROUP_VIEW_HEADER_ID);
//            c.setName("#" + "#");
//            result.add(c);
//            result.addAll(specialList);
//            mSeekPositionMap.put('#', index);
//        }
//        return result;
//    }


}
