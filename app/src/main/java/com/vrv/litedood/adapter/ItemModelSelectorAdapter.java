package com.vrv.litedood.adapter;

import android.content.Context;
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

import java.util.ArrayList;

/**
 * Created by kinee on 2016/5/8.
 */
public class ItemModelSelectorAdapter <T extends ItemModel> extends BaseAdapter {

    public static final String TAG = ItemModelSelectorAdapter.class.getSimpleName();

    public enum ITEM_TYPE {HEADER, ITEM};

    private Context mContext;
    private ArrayList<T> mList;
    private int mCheckBoxVisibility = View.GONE;

    private CompoundButton.OnCheckedChangeListener mCheckedChangeListener = null;

    public ItemModelSelectorAdapter(Context context, ArrayList<T> itemList) {
        super();
        this.mContext = context;
        mList = itemList;
    }

    public void setCheckedChangeListener(CompoundButton.OnCheckedChangeListener mCheckedChangeListener) {
        this.mCheckedChangeListener = mCheckedChangeListener;
    }

    public void setCheckBoxVisibility(int mCheckBoxVisibility) {
        this.mCheckBoxVisibility = mCheckBoxVisibility;
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
                convertView.setTag(viewHolder);


            }

            //设置头像
            viewHolder.ivItemModelAvatar.setImageBitmap(LiteDood.getBitmapFromFile(item.getAvatar()));

            //通用组件头像及名称设置
            viewHolder.tvItemModelName.setText(item.getName());
            viewHolder.cbItemModelChecker.setTag(item.getId());

            //非通用组件CheckBox设置
            viewHolder.cbItemModelChecker.setVisibility(mCheckBoxVisibility);
            if (mCheckedChangeListener != null)
                viewHolder.cbItemModelChecker.setOnCheckedChangeListener(mCheckedChangeListener);
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
