//************************************************************//
//File Name : ContactsFragment.java
//Author    : kinee
//Mailto    : kinee@163.com
//Comment   : 
//Date      : Mar 18, 2016
//************************************************************//

package com.vrv.ldgenie.ui.activity.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.ListViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.vrv.imsdk.SDKManager;
import com.vrv.imsdk.model.Contact;
import com.vrv.imsdk.model.ContactList;
import com.vrv.imsdk.model.ListModel;
import com.vrv.ldgenie.R;
import com.vrv.ldgenie.adapter.ContactsAdapter;
import com.vrv.ldgenie.common.sdk.utils.BaseInfoBean;
import com.vrv.ldgenie.ui.activity.MessageActivity;

import java.util.ArrayList;
import java.util.List;

public class ContactsFragment extends Fragment {
    private List<Contact> contactQueue = new ArrayList<>();
    private ContactsAdapter contactsAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        ContactList contactList = SDKManager.instance().getContactList();
        contactQueue.addAll(contactList.getList());
        contactsAdapter = new ContactsAdapter(getActivity(), contactQueue);
        setContactDataChangedListener(contactList);

        super.onCreate(savedInstanceState);
    }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_contacts, container, false);
		ListViewCompat lvContacts = (ListViewCompat)view.findViewById(R.id.listContacts);
        lvContacts.setAdapter(contactsAdapter);
        lvContacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Contact contact = contactQueue.get(position);
                Contact contact = (Contact)parent.getItemAtPosition(position);
                BaseInfoBean bean = BaseInfoBean.contact2BaseInfo(contact);
                MessageActivity.startMessageActivity(getActivity(), bean);
            }
        });
        return view;
	}

    private void setContactDataChangedListener(ContactList contactList) {
        contactList.setListener(new ListModel.OnChangeListener() {
            @Override
            public void notifyDataChange() {
                contactQueue.clear();
                contactQueue.addAll(SDKManager.instance().getContactList().getList());
                if (contactsAdapter != null) contactsAdapter.notifyDataSetChanged();
            }

            @Override
            public void notifyItemChange(int i) {
                if (contactsAdapter != null) contactsAdapter.notifyDataSetChanged();
            }
        });
    }
}
