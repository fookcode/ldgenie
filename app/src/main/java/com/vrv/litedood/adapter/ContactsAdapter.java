package com.vrv.litedood.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.vrv.imsdk.model.Contact;
import com.vrv.litedood.R;

import java.io.File;
import java.util.List;

/**
 * Created by kinee on 2016/4/3.
 */
public class ContactsAdapter extends BaseAdapter {
    private Context context;
    private List<Contact> listContact;

    public ContactsAdapter(Context context, List<Contact> listContact) {
        this.context = context;
        this.listContact = listContact;
    }

    @Override
    public int getCount() {
        int result = 0;
        if (listContact != null)
            result =  listContact.size();
        return result;
    }

    @Override
    public Object getItem(int position) {
        Contact result = null;
        if (getCount()>0)
            result = listContact.get(position);
        return result;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_contacts, null);
            viewHolder = new ViewHolder();
            viewHolder.ivContactAvatar = (AppCompatImageView)convertView.findViewById(R.id.contactAvatar);
            viewHolder.txtContactName = (AppCompatTextView)convertView.findViewById(R.id.contactName);
            viewHolder.txtContactSign = (AppCompatTextView)convertView.findViewById(R.id.contactSign);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder)convertView.getTag();
        }
        Contact contact = listContact.get(position);
        String avatarPath = contact.getAvatar();
        Bitmap bitmapAvatar;
        if ((null != avatarPath) && (!avatarPath.isEmpty())) {
            File fAvatar = new File(avatarPath);
            if ((fAvatar.isDirectory()) || (!fAvatar.exists()))
                viewHolder.ivContactAvatar.setImageResource(R.drawable.ic_launcher);
            else {
                bitmapAvatar = BitmapFactory.decodeFile(avatarPath);
                viewHolder.ivContactAvatar.setImageBitmap(bitmapAvatar);
            }
        }
        viewHolder.txtContactName.setText(contact.getName());
        viewHolder.txtContactSign.setText(contact.getSign());


        return convertView;
    }

    class ViewHolder {

        AppCompatImageView ivContactAvatar;
        AppCompatTextView txtContactName;
        AppCompatTextView txtContactSign;

        public ViewHolder() {};
    }
}
