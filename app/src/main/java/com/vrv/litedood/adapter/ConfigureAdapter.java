package com.vrv.litedood.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.vrv.litedood.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by kinee on 2016/4/21.
 */
public class ConfigureAdapter extends BaseAdapter {

    private final static int TYPE_TITLE = 0;
    private final static int TYPE_BOOLEAN = 1;
    private final static int TYPE_INTEGER = 2;
    private final static int TYPE_STRING = 3;
    private final static int TYPE_NULL = 9;

    private ArrayList<HashMap> mConfigureItems = new ArrayList<>();

    class ConfigureParameters {

        private boolean bSoundOn = true;
        private boolean bVibrationOn = false;

        public ArrayList<HashMap> toList() {
            ArrayList<HashMap> result = new ArrayList<>();
            result.add(newItem("声音设置", TYPE_TITLE, null, R.drawable.ic_config_sound));
            result.add(newItem("提示音", TYPE_BOOLEAN, bSoundOn, 0));
            result.add(newItem("震动", TYPE_BOOLEAN, bVibrationOn, 0));
//
//            result.add(newItem("聊天设置", TYPE_TITLE, null));
//            result.add(newItem("其它", TYPE_BOOLEAN, true));

            result.add(newItem("关于", TYPE_TITLE, null, R.drawable.ic_about));
            result.add(newItem("关于"+ context.getResources().getString(R.string.app_name), TYPE_NULL, null, 0));

            return result;
        }

        private HashMap<String, Object> newItem(String name, int type, Object value, int ResId) {
            HashMap<String, Object> result = new HashMap<>();
            result.put("name", name);
            result.put("type", type);
            result.put("value", value);
            result.put("resid", ResId);
            return result;
        }

        public void setSoundOn(boolean bSoundOn) {
            this.bSoundOn = bSoundOn;
        }

        public void setVibrationOn(boolean bVibrationOn) {
            this.bVibrationOn = bVibrationOn;
        }

        public boolean getSoundOn() {
            return bSoundOn;
        }

        public boolean getVibrationOn() {
            return bVibrationOn;
        }

    }

    private Context context;

    public ConfigureAdapter(Context context) {
        this.context = context;

        mConfigureItems.addAll(new ConfigureParameters().toList());
    }

    @Override
    public int getCount() {
        return mConfigureItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mConfigureItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HashMap<String, Object> item = (HashMap<String, Object>)getItem(position);
        int type = (int)item.get("type");
        switch (type) {
            case TYPE_TITLE:
                convertView = LayoutInflater.from(context).inflate(R.layout.item_config_title, null);
                AppCompatTextView tvConfigCategoryTitle = (AppCompatTextView)convertView.findViewById(R.id.tvConfigCategoryTitle);
                tvConfigCategoryTitle.setText((String)item.get("name"));
                AppCompatImageView ivConfigCategoryTitleImage = (AppCompatImageView)convertView.findViewById(R.id.ivConfigCategoryTitleImage);
                if((int)item.get("resid") != 0) {
                    ivConfigCategoryTitleImage.setImageResource((int)item.get("resid"));
                }
                break;
            case TYPE_BOOLEAN:
                convertView = LayoutInflater.from(context).inflate(R.layout.item_config_item, null);
                AppCompatTextView tvConfigItemName = (AppCompatTextView)convertView.findViewById(R.id.tvConfigItemName);
                tvConfigItemName.setText((String)item.get("name"));
                AppCompatCheckBox cbConfigItemValue = (AppCompatCheckBox)convertView.findViewById(R.id.cbConfigItemValue);
                cbConfigItemValue.setChecked((boolean)item.get("value"));
                break;
            case TYPE_INTEGER:

                break;
            case TYPE_STRING:

                break;
            case TYPE_NULL:
                convertView = LayoutInflater.from(context).inflate(R.layout.item_config_item, null);
                AppCompatTextView tvConfigItemName1 = (AppCompatTextView)convertView.findViewById(R.id.tvConfigItemName);
                tvConfigItemName1.setText((String)item.get("name"));
                AppCompatCheckBox cbConfigItemValue1 = (AppCompatCheckBox)convertView.findViewById(R.id.cbConfigItemValue);
                cbConfigItemValue1.setVisibility(View.INVISIBLE);
                break;
        }

        return convertView;
    }
}
