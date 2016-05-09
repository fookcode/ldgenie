package com.vrv.litedood.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.vrv.imsdk.model.ItemModel;
import com.vrv.litedood.R;
import com.vrv.litedood.common.LiteDood;

import java.util.ArrayList;

/**
 * Created by kinee on 2016/5/6.
 */
public class ItemModelSelectorSeekerAdapter<T extends ItemModel> extends BaseAdapter {
    private Context mContext;
    private ArrayList<T> mItemList;
    private ArrayList<Character> mNameLetters;

    public ItemModelSelectorSeekerAdapter(Context context, ArrayList<T> items) {
        this.mContext = context;
        mItemList = items;
        boolean bSpecial = false;
        Character mCurrentSpell = '#';
        if (mItemList != null) {
            mNameLetters = new ArrayList<>();
            for (T item : mItemList) {
                String name = item.getName();
                Character letter = Character.toUpperCase(LiteDood.PinYinUtil.getFirstSpell(String.valueOf(name.charAt(0))).toCharArray()[0]);
                if ((letter < 65) || (letter > 90)) bSpecial = true;
//                int ascii = name.charAt(0);
//                if (((ascii>= 65) && (ascii <=90)) || ((ascii >= 97) && (ascii <= 122))) {
//                    letter = Character.toUpperCase(name.charAt(0));
//                }
//                else {
//                    String[] letters = PinyinHelper.toHanyuPinyinStringArray(name.charAt(0));
//                    letter = Character.toUpperCase(letters[0].charAt(0));
//                }
                if ((letter >= 65) && (letter <= 90) && !mCurrentSpell.equals(letter)) {
                    mNameLetters.add(letter);
                    mCurrentSpell = letter;
                }
            }
            if(bSpecial) mNameLetters.add('#');
        }
    }

    @Override
    public int getCount() {
        if (mNameLetters != null)
            return mNameLetters.size();
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return mNameLetters.get(position);
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
