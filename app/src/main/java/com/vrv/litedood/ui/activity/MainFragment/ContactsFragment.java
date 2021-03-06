//************************************************************//
//File Name : ContactsFragment.java
//Author    : kinee
//Mailto    : kinee@163.com
//Comment   : 
//Date      : Mar 18, 2016
//************************************************************//

package com.vrv.litedood.ui.activity.MainFragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.ListViewCompat;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.vrv.imsdk.SDKManager;
import com.vrv.imsdk.model.Contact;
import com.vrv.imsdk.model.ContactList;
import com.vrv.imsdk.model.ListModel;
import com.vrv.litedood.LiteDoodApplication;
import com.vrv.litedood.R;
import com.vrv.litedood.adapter.ContactsAdapter;
import com.vrv.litedood.adapter.ContactsSeekerAdapter;
import com.vrv.litedood.common.sdk.utils.BaseInfoBean;
import com.vrv.litedood.ui.activity.ContactsCardActivity;
import com.vrv.litedood.ui.activity.ContactsGroupActivity;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ContactsFragment extends Fragment {

    public static final int CONTACTS_VIEW_HEADER_ID = -30;

    private static List<Contact> mContactList;
    private List<Contact> mContactQueue = new ArrayList<>();
    private ContactsAdapter mContactsAdapter;
    private ContactsSeekerAdapter mContactsSeekerAdapter;

    private static HashMap<Character, Integer> mSeekPositionMap = new HashMap<>();

    static {
        mContactList = SDKManager.instance().getContactList().getList();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        ContactList contactList = SDKManager.instance().getContactList();


        mContactQueue.addAll(reorganizeContacts(mContactList));

        mContactsAdapter = new ContactsAdapter(getActivity(), mContactQueue);

        mContactsSeekerAdapter = new ContactsSeekerAdapter(getActivity(), mContactList);

        contactList.setListener(new ListModel.OnChangeListener() {
            @Override
            public void notifyDataChange() {
                mContactQueue.clear();
                mContactQueue.addAll(reorganizeContacts(SDKManager.instance().getContactList().getList()));
                if (mContactsAdapter != null) mContactsAdapter.notifyDataSetChanged();
            }

            @Override
            public void notifyItemChange(int i) {
                if (mContactsAdapter != null) mContactsAdapter.notifyDataSetChanged();
            }
        });

        super.onCreate(savedInstanceState);
    }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_contacts, container, false);
		final ListViewCompat lvContactsList = (ListViewCompat)view.findViewById(R.id.lvContacts);
        lvContactsList.setAdapter(mContactsAdapter);
        lvContactsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Contact contact = contactQueue.get(position);
                Contact contact = (Contact)parent.getItemAtPosition(position);
                BaseInfoBean bean = BaseInfoBean.contact2BaseInfo(contact);
                ContactsCardActivity.startContactCardActivity(getActivity(), bean, Intent.ACTION_VIEW);
            }
        });

        LinearLayoutCompat linearLayoutCompat = (LinearLayoutCompat)view.findViewById(R.id.llContactGroupBar);
        linearLayoutCompat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContactsGroupActivity.startContactsGroupActivity(ContactsFragment.this.getActivity());
            }
        });

        ListViewCompat lvContactsSeeker = (ListViewCompat)view.findViewById(R.id.lvContactsSeeker);
        lvContactsSeeker.setAdapter(mContactsSeekerAdapter);
        lvContactsSeeker.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Character spellFirst = (Character) parent.getItemAtPosition(position);
                if (spellFirst != null) {
                    Integer pos = mSeekPositionMap.get(spellFirst);
                    if (pos != null)
                        lvContactsList.setSelection(pos);
                }
            }
        });
        return view;
	}

    private List<Contact> reorganizeContacts(List<Contact> contacts) {
        if ((contacts == null) || (contacts.size() == 0)) return  contacts;
        ArrayList<Contact> specialList = new ArrayList<>();
        Character firstSpell = '#';
        ArrayList<Contact> result = new ArrayList<>();
        mSeekPositionMap.clear();
        int index = 0;
        for(Contact contact : contacts) {
            Character nameFirstSpell = Character.toUpperCase(contact.getPinyin().charAt(0));
            if ((nameFirstSpell<65) || (nameFirstSpell> 90)) {
                specialList.add(contact);
            }
            else {
                if (!firstSpell.equals(nameFirstSpell)) {
                    firstSpell = nameFirstSpell;
                    Contact c = new Contact();
                    c.setId(CONTACTS_VIEW_HEADER_ID);
                    c.setName("#" + firstSpell);
                    result.add(c);

                    mSeekPositionMap.put(firstSpell, index);
                    index++;
                }
                index++;

                result.add(contact);
            }

        }
        if (specialList.size() > 0) {
            Contact c = new Contact();
            c.setId(CONTACTS_VIEW_HEADER_ID);
            c.setName("#" + "#");
            result.add(c);
            result.addAll(specialList);
            mSeekPositionMap.put('#', index);
        }
        return result;
    }

}
