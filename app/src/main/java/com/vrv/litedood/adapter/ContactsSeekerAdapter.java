package com.vrv.litedood.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.vrv.imsdk.model.Contact;
import com.vrv.litedood.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kinee on 2016/4/28.
 */
public class ContactsSeekerAdapter extends BaseAdapter {
    private static final String TAG = ContactsSeekerAdapter.class.getSimpleName();

    private Context mContext;
    private List<Contact> mContacts;

    private Character mCurrentSpell = '#';
    private static ArrayList<Character> mNameLetters = new ArrayList<>();

    public ContactsSeekerAdapter(Context context, List<Contact> contacts) {
        super();
        this.mContacts = contacts;
        this.mContext = context;
        if (mContacts != null) {
            mNameLetters.clear();
            for (Contact contact : mContacts) {
                Character letter = Character.toUpperCase(contact.getPinyin().charAt(0));
//                String name = contact.getName();
//                Character letter = Character.toUpperCase(PinYinUtil.getFirstSpell(String.valueOf(name.charAt(0))).toCharArray()[0]);
//                int ascii = name.charAt(0);
//                if (((ascii>= 65) && (ascii <=90)) || ((ascii >= 97) && (ascii <= 122))) {
//                    letter = Character.toUpperCase(name.charAt(0));
//                }
//                else {
//                    String[] letters = PinyinHelper.toHanyuPinyinStringArray(name.charAt(0));
//                    letter = Character.toUpperCase(letters[0].charAt(0));
//                }
                if (!mCurrentSpell.equals(letter)) {
                    mNameLetters.add(letter);
                    mCurrentSpell = letter;
                }
            }
            Log.v(TAG, mNameLetters.toString());
        }
    }

    @Override
    public int getCount() {
        return mNameLetters == null ? 0 : mNameLetters.size();
    }

    @Override
    public Object getItem(int position) {
        return mNameLetters == null ? null: mNameLetters.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        AppCompatTextView item = null;
        if (convertView != null) {
            item = (AppCompatTextView) convertView.getTag();
        }
        else {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_contacts_seeker, null);
            item = (AppCompatTextView) convertView.findViewById(R.id.tvContactsSeekerItem);
            convertView.setTag(item);
        }
        item.setText(mNameLetters.get(position).toString());

        return convertView;
    }


}
