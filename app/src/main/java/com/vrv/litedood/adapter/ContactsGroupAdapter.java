package com.vrv.litedood.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.vrv.imsdk.model.Group;
import com.vrv.litedood.R;
import com.vrv.litedood.common.LiteDood;

import java.util.List;

/**
 * Created by kinee on 2016/5/3.
 */
public class ContactsGroupAdapter extends BaseAdapter {
    private Context mContext;
    private List<Group> mGroupList;
    private enum ITEM_TYPE {HEADER, GROUP};

    public ContactsGroupAdapter(Context context, List<Group> listGroup) {
        this.mContext = context;
        this.mGroupList = listGroup;
    }

    @Override
    public int getCount() {
        int result = 0;
        if (mGroupList != null)
            result =  mGroupList.size();
        return result;
    }

    @Override
    public Object getItem(int position) {
        Group result = null;
        if (getCount()>0)
            result = mGroupList.get(position);
        return result;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        ITEM_TYPE result = ITEM_TYPE.GROUP;
        Group group = mGroupList.get(position);
        if ((group.getName().charAt(0) == '#') && (group.getId() == LiteDood.LIST_VIEW_HEADER_ITEM)) {
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
        Group group = mGroupList.get(position);
        ViewHolder viewHolder = null;
        if (convertView != null) viewHolder = (ViewHolder) convertView.getTag();

        if (getItemViewType(position) == ITEM_TYPE.HEADER.ordinal()) {
            if (viewHolder != null) {
                if (viewHolder.mType == ITEM_TYPE.HEADER) {
                    viewHolder.tvListHeader.setText(String.valueOf(group.getName().charAt(1)));
                    return convertView;
                }

            }
            viewHolder = new ViewHolder(ITEM_TYPE.HEADER);
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_list_header, null);
            viewHolder.tvListHeader = (AppCompatTextView) convertView.findViewById(R.id.tvListHeader);
            viewHolder.tvListHeader.setText(String.valueOf(group.getName().charAt(1)));
            convertView.setTag(viewHolder);

        }
        else {
            if ((convertView != null) && ((ViewHolder)convertView.getTag()).mType == ITEM_TYPE.GROUP) {
                viewHolder = (ViewHolder)convertView.getTag();
            }
            else
            {
                viewHolder = new ViewHolder(ITEM_TYPE.GROUP);
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_contacts_group, null);
                viewHolder.ivGroupAvatar = (AppCompatImageView) convertView.findViewById(R.id.ivGroupAvatar);
                viewHolder.tvGroupName = (AppCompatTextView) convertView.findViewById(R.id.tvGroupName);
                convertView.setTag(viewHolder);
            }

            viewHolder.ivGroupAvatar.setImageBitmap(LiteDood.getAvatarBitmap(group.getAvatar()));
            viewHolder.tvGroupName.setText(group.getName());

        }
        return convertView;
    }

    class ViewHolder {

        ITEM_TYPE mType;

        AppCompatImageView ivGroupAvatar;
        AppCompatTextView tvGroupName;
        AppCompatTextView tvListHeader;

        public ViewHolder(ITEM_TYPE type) {
            this.mType = type;
        };
    }
}
