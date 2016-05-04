package com.vrv.litedood.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.util.SortedList;
import android.support.v7.widget.ListViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.vrv.imsdk.SDKManager;
import com.vrv.imsdk.model.Contact;
import com.vrv.imsdk.model.Group;
import com.vrv.imsdk.model.GroupList;
import com.vrv.litedood.R;
import com.vrv.litedood.adapter.ContactsGroupAdapter;
import com.vrv.litedood.common.PinYinUtil;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.SortedSet;

/**
 * Created by kinee on 2016/5/3.
 */
public class ContactsGroupActivity extends AppCompatActivity {

    private final static String TAG =ContactsGroupActivity.class.getSimpleName();
    public final static int GROUP_VIEW_HEADER_ID = -31;
    private ArrayList<Group> mContactsGroupList;
    private GroupList mGroupList;
    private HashMap<Character, Integer> mSeekPositionMap = new HashMap<>();

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
        mContactsGroupList = mGroupList.getGroups();

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ContactsGroupAdapter contactsGroupAdapter = new ContactsGroupAdapter(this, reorganizeGroups(mContactsGroupList));
        setContentView(R.layout.activity_contacts_group);

        Toolbar toolbar = (Toolbar)findViewById(R.id.tbContactGroup);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ListViewCompat groupListView = (ListViewCompat)findViewById(R.id.lvContactsGroup);
        groupListView.setAdapter(contactsGroupAdapter);
    }

    private List<Group> reorganizeGroups(List<Group> groups) {
        if ((groups == null) || (groups.size() == 0)) return  groups;
        Character firstSpell = '#';
        ArrayList<Group> result = new ArrayList<>();
        mSeekPositionMap.clear();
        int index = 0;
        for(Group group : groups) {
            Character nameFirstSpell = Character.toUpperCase(PinYinUtil.getFirstSpell(group.getName()).charAt(0));
            if (!firstSpell.equals(nameFirstSpell)) {
                firstSpell = nameFirstSpell;
                Group c = new Group();
                c.setId(GROUP_VIEW_HEADER_ID);
                c.setName("#" + firstSpell);
                result.add(c);

                mSeekPositionMap.put(firstSpell, index);
                index ++;
            }
            index ++;
            result.add(group);

        }
        return result;
    }

    private class SortedGroupLIst extends AbstractList<Group> implements SortedSet<Group> {

        @Override
        public Group get(int location) {
            return null;
        }

        @Override
        public int size() {
            return 0;
        }

        @Override
        public Comparator<? super Group> comparator() {
            return null;
        }

        @Override
        public Group first() {
            return null;
        }

        @NonNull
        @Override
        public SortedSet<Group> headSet(Group end) {
            return null;
        }

        @Override
        public Group last() {
            return null;
        }

        @NonNull
        @Override
        public SortedSet<Group> subSet(Group start, Group end) {
            return null;
        }

        @NonNull
        @Override
        public SortedSet<Group> tailSet(Group start) {
            return null;
        }
    }
}
