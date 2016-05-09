package com.vrv.litedood.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;

import com.vrv.imsdk.model.ItemModel;
import com.vrv.litedood.R;
import com.vrv.litedood.common.LiteDood;
import com.vrv.litedood.ui.activity.ItemModelSelectorActivity;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by kinee on 2016/5/8.
 */
public class ItemModelSelectorAdapter <T extends ItemModel> extends BaseAdapter {

    public static final String TAG = ItemModelSelectorAdapter.class.getSimpleName();

    public enum ITEM_TYPE {HEADER, ITEM};

    private Context mContext;
    private ArrayList<T> mList;

    public ItemModelSelectorAdapter(Context context, ArrayList<T> itemList) {
        super();
        this.mContext = context;
        mList = itemList;
    }

    @Override
    public int getItemViewType(int position) {
        ITEM_TYPE result = ITEM_TYPE.ITEM;
        T item = mList.get(position);
        if ((item.getName().charAt(0) == '#') && (item.getId() == LiteDood.LIST_VIEW_HEADER_ITEM)) {
            result = ITEM_TYPE.HEADER;
        }
        return result.ordinal();
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public Object getItem(int position) {
        if (mList != null)
            return mList.get(position);
        else return null;
    }

    @Override
    public long getItemId(int position) {
        return mList == null ? 0: mList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final T item = (T)getItem(position);
        ViewHolder viewHolder = null;
        if (convertView != null) {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (getItemViewType(position) == ITEM_TYPE.HEADER.ordinal()) {
            if (viewHolder != null) {
                if (viewHolder.mType == ITEM_TYPE.HEADER) {
                    viewHolder.tvListHeader.setText(String.valueOf(item.getName().charAt(1)));
                    return convertView;
                }
            }
            viewHolder = new ViewHolder(ITEM_TYPE.HEADER);
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_list_header, null);
            viewHolder.tvListHeader = (AppCompatTextView) convertView.findViewById(R.id.tvListHeader);
            viewHolder.tvListHeader.setText(String.valueOf(item.getName().charAt(1)));
            convertView.setTag(viewHolder);

        }
        else {
            if ((convertView != null) && ((ViewHolder)convertView.getTag()).mType == ITEM_TYPE.ITEM) {
                viewHolder = (ViewHolder)convertView.getTag();
            }
            else {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_item_model_selector, null);
                viewHolder = new ViewHolder(ITEM_TYPE.ITEM);
                viewHolder.ivItemModelAvatar = (AppCompatImageView) convertView.findViewById(R.id.ivItemModelAvatar);
                viewHolder.tvItemModelName = (AppCompatTextView) convertView.findViewById(R.id.tvItemModelName);
                viewHolder.cbItemModelChecker = (AppCompatCheckBox) convertView.findViewById((R.id.cbItemModelChecker));
                viewHolder.cbItemModelChecker.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        ((ItemModelSelectorActivity)mContext).setSelectedItem(item.getId(), isChecked);
                    }
                });
                convertView.setTag(viewHolder);
            }

            //设置头像
            String avatarPath = item.getAvatar();
            if ((null != avatarPath) && (!avatarPath.isEmpty())) {
                File fAvatar = new File(avatarPath);
                if ((fAvatar.isDirectory()) || (!fAvatar.exists())) {

                    viewHolder.ivItemModelAvatar.setImageResource(R.drawable.ic_launcher);
                    //boolean result = RequestHelper.getUserInfo(chat.getId(), new ChatRequlestHandler(context, viewHolder, TYPE_GET_USER));
                    //if (!result) {Log.v(TAG, "获取用户数据失败");}
                } else {
                    Bitmap bitmapAvatar = BitmapFactory.decodeFile(avatarPath);
                    viewHolder.ivItemModelAvatar.setImageBitmap(bitmapAvatar);
                }
            }
            viewHolder.tvItemModelName.setText(item.getName());
        }
        return convertView;
    }

    public class ViewHolder {
        public ITEM_TYPE mType;
        public AppCompatTextView tvListHeader;
        public AppCompatImageView ivItemModelAvatar;
        public AppCompatTextView tvItemModelName;
        public AppCompatCheckBox cbItemModelChecker;

        public ViewHolder(ITEM_TYPE type) {
            mType = type;
        }
    }
}
