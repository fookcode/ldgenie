package com.vrv.litedood.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.ListViewCompat;
import android.text.format.DateFormat;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;

import com.vrv.imsdk.model.Contact;
import com.vrv.litedood.R;
import com.vrv.litedood.ui.activity.ContactCardActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by kinee on 2016/4/30.
 */
public class ContactCardAdapter extends BaseAdapter {
    private static final String TAG = ContactCardActivity.class.getSimpleName();

    private static final String NAME = "name";
    private static final String VALUE = "value";
    private static final  String TYPE = "type";

    private enum ITEM_TYPE {image, line, list};

    private static final String[]  mContactItems = {
            "avatar",
            "name",
            "gender",
            "birth",
            "phones",
            "emails",
            "sign",
            "remark"
    };

    private Context mContext;
    private Contact mContact;

    private static HashMap<String, HashMap<String, Object>> mContactMap = new HashMap<>();

    public ContactCardAdapter(Context context, Contact contact) {
        mContact = contact;
        mContext = context;
        refreshContactDataHolder();
        Log.v(TAG, "");
    }

    private void refreshContactDataHolder() {
        //头像
        HashMap<String, Object> avatarHolder = mContactMap.get(mContactItems[0]);
        if  (avatarHolder != null) {
            avatarHolder.put(VALUE, mContact.getAvatar() == null ? "": mContact.getAvatar());
        }
        else {
            avatarHolder = new HashMap<>();
            avatarHolder.put(NAME, "头像");
            avatarHolder.put(VALUE, mContact.getAvatar());
            avatarHolder.put(TYPE, ITEM_TYPE.image);
            mContactMap.put(mContactItems[0], avatarHolder);
        }

        //名称
        HashMap<String, Object> nameHolder = mContactMap.get(mContactItems[1]);
        if ( nameHolder != null) {
            nameHolder.put(VALUE, mContact.getName());
        }
        else {
            nameHolder = new HashMap<>();
            nameHolder.put(NAME, "名称");
            nameHolder.put(VALUE, mContact.getName());
            nameHolder.put(TYPE, ITEM_TYPE.line);
            mContactMap.put(mContactItems[1], nameHolder);

        }

        //性别
        HashMap<String, Object> genderHolder = mContactMap.get(mContactItems[2]);
        final byte gender = mContact.getGender();
        String genderStr = "";
        switch(gender) {
            case 1 :
                genderStr = "男";
                break;
            case 2:
                genderStr = "女";
                break;
            default:
                genderStr = "-";
        }
        if ( genderHolder != null) {
            genderHolder.put(VALUE,genderStr);
        }
        else {
            genderHolder = new HashMap<>();
            genderHolder.put(NAME, "性别");
            genderHolder.put(VALUE, genderStr);
            genderHolder.put(TYPE, ITEM_TYPE.line);
            mContactMap.put(mContactItems[2], genderHolder);
        }

        //生日
        HashMap<String, Object> birthHolder = mContactMap.get(mContactItems[3]);

        long birth = mContact.getBirth();
        String birthStr = "-";
        if (birth != 0)
        {
            Time birthTime  = new Time();
            birthTime.set(birth);
            birthStr = birthTime.format("%Y-%m-%d");
        }

        if ( birthHolder != null) {
            birthHolder.put(VALUE, birthStr);
        }
        else {
            birthHolder = new HashMap<>();
            birthHolder.put(NAME, "生日");

            birthHolder.put(VALUE, birthStr);
            birthHolder.put(TYPE, ITEM_TYPE.line);
            mContactMap.put(mContactItems[3], birthHolder);

        }


        //电话
        HashMap<String, Object> phoneHolder = mContactMap.get(mContactItems[4]);
        if ( phoneHolder != null) {
            phoneHolder.put(VALUE, mContact.getPhones());
        }
        else {
            phoneHolder = new HashMap<>();
            phoneHolder.put(NAME, "电话");
            phoneHolder.put(VALUE, mContact.getPhones());
            phoneHolder.put(TYPE, ITEM_TYPE.list);
            mContactMap.put(mContactItems[4], phoneHolder);

        }

        //邮件地址
        HashMap<String, Object> emailHolder = mContactMap.get(mContactItems[5]);
        if ( emailHolder != null) {
            emailHolder.put(VALUE, mContact.getEmails());
        }
        else {
            emailHolder = new HashMap<>();
            emailHolder.put(NAME, "电子邮件");
            emailHolder.put(VALUE, mContact.getEmails());
            emailHolder.put(TYPE, ITEM_TYPE.list);
            mContactMap.put(mContactItems[5], emailHolder);

        }

        //签名
        HashMap<String, Object> signHolder = mContactMap.get(mContactItems[6]);
        if ( signHolder != null) {
            signHolder.put(VALUE, mContact.getSign());
        }
        else {
            signHolder = new HashMap<>();
            signHolder.put(NAME, "个性签名");
            signHolder.put(VALUE, mContact.getSign());
            signHolder.put(TYPE, ITEM_TYPE.line);
            mContactMap.put(mContactItems[6], signHolder);

        }

        //备注
        HashMap<String, Object> remarkHolder = mContactMap.get(mContactItems[7]);
        if ( remarkHolder != null) {
            remarkHolder.put(VALUE, mContact.getRemark());
        }
        else {
            remarkHolder = new HashMap<>();
            remarkHolder.put(NAME, "备注");
            remarkHolder.put(VALUE, mContact.getRemark());
            remarkHolder.put(TYPE, ITEM_TYPE.line);
            mContactMap.put(mContactItems[7], remarkHolder);
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
        View result;
        if (convertView == null) {
            result = createNewItemView(item);
        }
        else {
            if (((ViewHolder)convertView.getTag()).type == item.get(TYPE)) {
                ViewHolder viewHolder = (ViewHolder) convertView.getTag();

                if (viewHolder.type == ITEM_TYPE.line) {
                    viewHolder.tvContactCardLineItemValue.setText(item.get(VALUE).toString());
                    viewHolder.tvContactCardItemName.setText(item.get(NAME).toString());
                } else if(viewHolder.type == ITEM_TYPE.line.list){
                    viewHolder.lvContactCardListItemValue.setAdapter(new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, (ArrayList<String>) item.get(VALUE)));
                } else if (viewHolder.type == ITEM_TYPE.line.image) {
                    setAvatar(item, viewHolder);
                    ;
                }
                result = convertView;
            }
            else {
                result = createNewItemView(item);
            }
        }
        return result;
    }

    private View createNewItemView(HashMap item) {
        ViewHolder viewHolder;
        View newItemView = null;

        viewHolder = new ViewHolder();
        if (item.get(TYPE) == ITEM_TYPE.line) {
            newItemView = LayoutInflater.from(mContext).inflate(R.layout.item_contact_card_line_item, null);
            viewHolder.tvContactCardItemName = (AppCompatTextView) newItemView.findViewById(R.id.tvContactCardLineItemName);
            viewHolder.tvContactCardItemName.setText(item.get(NAME) == null ? "" : item.get(NAME).toString());
            viewHolder.tvContactCardLineItemValue = (AppCompatTextView) newItemView.findViewById(R.id.tvContactCardLineItemValue);
            viewHolder.tvContactCardLineItemValue.setText(item.get(VALUE) == null ? "" : item.get(VALUE).toString());
            viewHolder.type = ITEM_TYPE.line;
        } else if (item.get(TYPE) == ITEM_TYPE.list) {
            newItemView = LayoutInflater.from(mContext).inflate(R.layout.item_contact_card_list_item, null);

            viewHolder.tvContactCardItemName = (AppCompatTextView) newItemView.findViewById(R.id.tvContactCardListItemName);
            viewHolder.tvContactCardItemName.setText(item.get(NAME) == null ? "" : item.get(NAME).toString());
            viewHolder.lvContactCardListItemValue = (ListViewCompat) newItemView.findViewById(R.id.lvContactCardListItemValue);
            if (item.get(VALUE) != null)
                viewHolder.lvContactCardListItemValue.setAdapter(new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, (ArrayList<String>) item.get(VALUE)));
            viewHolder.type = ITEM_TYPE.list;
        }
        else if (item.get(TYPE) == ITEM_TYPE.image) {
            newItemView = LayoutInflater.from(mContext).inflate(R.layout.item_contact_card_image_item, null);
            viewHolder.tvContactCardItemName = (AppCompatTextView) newItemView.findViewById(R.id.tvContactCardImageItemName);
            viewHolder.tvContactCardItemName.setText(item.get(NAME) == null ? "" : item.get(NAME).toString());
            viewHolder.ivContactCardImageItemValue = (AppCompatImageView)newItemView.findViewById(R.id.ivContactCardImageItevValue);
            setAvatar(item, viewHolder);

        }
        newItemView.setTag(viewHolder);

        return newItemView;
    }

    private void setAvatar(HashMap item, ViewHolder viewHolder) {
        Bitmap bitmapAvatar;
        String avatarPath = item.get(VALUE).toString();
        if ((null != avatarPath) && (!avatarPath.isEmpty())) {
            File fAvatar = new File(avatarPath);
            if ((fAvatar.isDirectory()) || (!fAvatar.exists()))
                viewHolder.ivContactCardImageItemValue.setImageResource(R.drawable.ic_launcher);
            else {
                bitmapAvatar = BitmapFactory.decodeFile(avatarPath);
                viewHolder.ivContactCardImageItemValue.setImageBitmap(bitmapAvatar);
            }
        }
    }

    class ViewHolder {
        ITEM_TYPE type;
        AppCompatImageView ivContactCardImageItemValue;
        AppCompatTextView tvContactCardItemName;
        AppCompatTextView tvContactCardLineItemValue;
        ListViewCompat lvContactCardListItemValue;
    }
}
