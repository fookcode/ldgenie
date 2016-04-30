package com.vrv.litedood.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.ListViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;

import com.vrv.imsdk.model.Contact;
import com.vrv.litedood.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by kinee on 2016/4/30.
 */
public class ContactCardAdapter extends BaseAdapter {
    private static final String NAME = "name";
    private static final String VALUE = "value";
    private static final  String TYPE = "type";
    private static enum ITEM_TYPE {line, list};
    private static final String[]  mContactItems = {
//            "srcAvatar",
//            "nickID",
            "name",
            "gender",
            "birth",
            "phones",
            "emails",
            "sign",
            "remark"
    };

    private Contact mContact;
    private Context mContext;

    private HashMap<String, HashMap<String, Object>> mContactMap = new HashMap<>();

    public ContactCardAdapter(Context context, Contact contact) {
        mContact = contact;
        mContext = context;
        refreshContactDataHolder();
    }

    private void refreshContactDataHolder() {

        //名称
        HashMap<String, Object> nameHolder = mContactMap.get(mContactItems[0]);
        if ( nameHolder != null) {
            nameHolder.put(VALUE, mContact.getNickID());
        }
        else {
            nameHolder = new HashMap<>();
            nameHolder.put(NAME, "名称");
            nameHolder.put(VALUE, mContact.getNickID());
            nameHolder.put(TYPE, ITEM_TYPE.line);
            mContactMap.put(mContactItems[0], nameHolder);

        }

        //性别
        HashMap<String, Object> genderHolder = mContactMap.get(mContactItems[1]);
        if ( genderHolder != null) {
            genderHolder.put(VALUE, mContact.getGender()  == '1' ? "男": "女");
        }
        else {
            genderHolder = new HashMap<>();
            genderHolder.put(NAME, "性别");
            genderHolder.put(VALUE, mContact.getGender()  == '1' ? "男": "女");
            genderHolder.put(TYPE, ITEM_TYPE.line);
            mContactMap.put(mContactItems[1], genderHolder);

        }

        //生日
        HashMap<String, Object> birthHolder = mContactMap.get(mContactItems[2]);
        if ( birthHolder != null) {
            birthHolder.put(VALUE, mContact.getBirth());
        }
        else {
            birthHolder = new HashMap<>();
            birthHolder.put(NAME, "生日");
            birthHolder.put(VALUE, mContact.getPhones());
            birthHolder.put(TYPE, ITEM_TYPE.line);
            mContactMap.put(mContactItems[2], birthHolder);

        }


        //电话
        HashMap<String, Object> phoneHolder = mContactMap.get(mContactItems[3]);
        if ( phoneHolder != null) {
            phoneHolder.put(VALUE, mContact.getPhones());
        }
        else {
            phoneHolder = new HashMap<>();
            phoneHolder.put(NAME, "电话");
            phoneHolder.put(VALUE, mContact.getPhones());
            phoneHolder.put(TYPE, ITEM_TYPE.list);
            mContactMap.put(mContactItems[3], phoneHolder);

        }

        //邮件地址
        HashMap<String, Object> emailHolder = mContactMap.get(mContactItems[4]);
        if ( emailHolder != null) {
            emailHolder.put(VALUE, mContact.getEmails());
        }
        else {
            emailHolder = new HashMap<>();
            emailHolder.put(NAME, "电子邮件");
            emailHolder.put(VALUE, mContact.getEmails());
            emailHolder.put(TYPE, ITEM_TYPE.list);
            mContactMap.put(mContactItems[4], emailHolder);

        }

        //签名
        HashMap<String, Object> signHolder = mContactMap.get(mContactItems[5]);
        if ( signHolder != null) {
            signHolder.put(VALUE, mContact.getSign());
        }
        else {
            signHolder = new HashMap<>();
            signHolder.put(NAME, "个性签名");
            signHolder.put(VALUE, mContact.getSign());
            signHolder.put(TYPE, ITEM_TYPE.line);
            mContactMap.put(mContactItems[5], signHolder);

        }

        //备注
        HashMap<String, Object> remarkHolder = mContactMap.get(mContactItems[6]);
        if ( remarkHolder != null) {
            remarkHolder.put(VALUE, mContact.getRemark());
        }
        else {
            remarkHolder = new HashMap<>();
            remarkHolder.put(NAME, "个性签名");
            remarkHolder.put(VALUE, mContact.getRemark());
            remarkHolder.put(TYPE, ITEM_TYPE.line);
            mContactMap.put(mContactItems[5], remarkHolder);
        }

    }

    @Override
    public int getCount() {
        return mContactItems.length;
    }

    @Override
    public Object getItem(int position) {
        return mContactMap.get(mContactItems[position]);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HashMap<String, Object> item = (HashMap)getItem(position);
        ViewHolder viewHolder;

        if (convertView == null) {
             viewHolder = new ViewHolder();
            if (item.get(TYPE) == ITEM_TYPE.line) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_contact_card_line_item, null);
                viewHolder.tvContactCardItemName = (AppCompatTextView) convertView.findViewById(R.id.tvContactCardLineItemName);
                viewHolder.tvContactCardItemName.setText(item.get(NAME) == null? "" : item.get(NAME).toString());
                viewHolder.tvContactCardLineItemValue = (AppCompatTextView) convertView.findViewById(R.id.tvContactCardLineItemValue);
                viewHolder.tvContactCardLineItemValue.setText(item.get(VALUE) == null ? "" : item.get(VALUE).toString());
                viewHolder.type = ITEM_TYPE.line;
            }
            else {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_contact_card_list_item, null);

                viewHolder.tvContactCardItemName = (AppCompatTextView) convertView.findViewById(R.id.tvContactCardListItemName);
                viewHolder.tvContactCardItemName.setText(item.get(NAME) == null ? "": item.get(NAME).toString());
                viewHolder.lvContactCardListItemValue = (ListViewCompat) convertView.findViewById(R.id.lvContactCardListItemValue);
                if (item.get(VALUE) != null)
                    viewHolder.lvContactCardListItemValue.setAdapter(new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, (ArrayList<String>)item.get(VALUE)));
                viewHolder.type = ITEM_TYPE.list;
            }
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();

            if ((item.get(TYPE) == ITEM_TYPE.line)  && (viewHolder.type == ITEM_TYPE.line)) {
                viewHolder.tvContactCardLineItemValue.setText(item.get(VALUE).toString());
            }
            else if ((item.get(TYPE) == ITEM_TYPE.list)  && (viewHolder.type == ITEM_TYPE.list)){
                if (item.get(VALUE) != null)
                    viewHolder.lvContactCardListItemValue.setAdapter(new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, (ArrayList<String>)item.get(VALUE)));

            }
            else {

            }

        }

        return convertView;
    }

    class ViewHolder {
        ITEM_TYPE type;
        AppCompatTextView tvContactCardItemName;
        AppCompatTextView tvContactCardLineItemValue;
        ListViewCompat lvContactCardListItemValue;
    }
}
