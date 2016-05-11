package com.vrv.litedood.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.vrv.imsdk.model.Contact;
import com.vrv.litedood.R;
import com.vrv.litedood.common.LiteDood;
import com.vrv.litedood.ui.activity.MainFragment.ContactsFragment;

import java.util.List;

/**
 * Created by kinee on 2016/4/3.
 */
public class ContactsAdapter extends BaseAdapter {
    private Context mContext;
    private List<Contact> mContactList;
    private enum ITEM_TYPE {HEADER, CONTACT};
    private Character mSpellTitle = '#';

    public ContactsAdapter(Context context, List<Contact> listContact) {
        this.mContext = context;
        this.mContactList = listContact;
    }

    @Override
    public int getCount() {
        int result = 0;
        if (mContactList != null)
            result =  mContactList.size();
        return result;
    }

    @Override
    public Object getItem(int position) {
        Contact result = null;
        if (getCount()>0)
            result = mContactList.get(position);
        return result;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        ITEM_TYPE result = ITEM_TYPE.CONTACT;
        Contact contact = mContactList.get(position);
        if ((contact.getName().charAt(0) == '#') && (contact.getId() == ContactsFragment.CONTACTS_VIEW_HEADER_ID)) {
            result = ITEM_TYPE.HEADER;
        }
        return result.ordinal();
    }

    @Override
    public int getViewTypeCount() {
        return ITEM_TYPE.values().length;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Contact contact = mContactList.get(position);
        ViewHolder viewHolder = null;
        if (convertView != null) viewHolder = (ViewHolder) convertView.getTag();

        if (getItemViewType(position) == ITEM_TYPE.HEADER.ordinal()) {
            if (viewHolder != null) {
                if (viewHolder.mType == ITEM_TYPE.HEADER) {
                    viewHolder.tvContactHeader.setText(String.valueOf(contact.getName().charAt(1)));
                    return convertView;
                }

            }
            viewHolder = new ViewHolder(ITEM_TYPE.HEADER);
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_list_header, null);
            viewHolder.tvContactHeader = (AppCompatTextView) convertView.findViewById(R.id.tvListHeader);
            viewHolder.tvContactHeader.setText(String.valueOf(contact.getName().charAt(1)));
            convertView.setTag(viewHolder);

        }
        else {
            if ((convertView != null) && ((ViewHolder)convertView.getTag()).mType == ITEM_TYPE.CONTACT) {
                viewHolder = (ViewHolder)convertView.getTag();
            }
            else
            {
                viewHolder = new ViewHolder(ITEM_TYPE.CONTACT);
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_contacts, null);
                viewHolder.ivContactAvatar = (AppCompatImageView) convertView.findViewById(R.id.contactAvatar);
                viewHolder.tvContactName = (AppCompatTextView) convertView.findViewById(R.id.contactName);
                viewHolder.tvContactSign = (AppCompatTextView) convertView.findViewById(R.id.contactSign);
                convertView.setTag(viewHolder);
            }

            viewHolder.ivContactAvatar.setImageBitmap(LiteDood.getAvatarBitmap(contact.getAvatar()));
            viewHolder.tvContactName.setText(contact.getName());
            viewHolder.tvContactSign.setText(contact.getSign());

        }
        return convertView;
    }

    class ViewHolder {

        ITEM_TYPE mType;

        AppCompatImageView ivContactAvatar;
        AppCompatTextView tvContactName;
        AppCompatTextView tvContactSign;
        AppCompatTextView tvContactHeader;

        public ViewHolder(ITEM_TYPE type) {
            this.mType = type;
        };
    }
}
