package com.vrv.litedood.common.widget;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;

import com.vrv.litedood.ui.activity.MainActivity;
import com.vrv.litedood.R;
import com.vrv.litedood.ui.activity.MainFragment.ContactsFragment;
import com.vrv.litedood.ui.activity.MainFragment.PandoraFragment;

import java.util.Map;

/**
 * Created by kinee on 2016/3/20.
 */
public class ButtonActiveFragmentOnClickListener implements View.OnClickListener {
    private Map<String, Fragment> fragments;
    private FragmentManager fragmentManager;

    public ButtonActiveFragmentOnClickListener(Map fragments, FragmentManager manager) {
        this.fragments = fragments;
        this.fragmentManager = manager;
    }

    @Override
    public void onClick(View v) {
        Button button = (Button) v;
        Fragment currentFragment = fragments.get(MainActivity.TAG_CURRENT_FRAGMENT);        //获取当前视图，准备切换
        Fragment selectedFragment = fragments.get(button.getTag());
        if (selectedFragment == currentFragment) return;                   //点击当前功能的按钮，不做任何切换动作，返回

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();     //切换视图开始
        if (selectedFragment == null) {
            if (MainActivity.TAG_CONTACTS_FRAGMENT.equals(button.getTag().toString())) {               //联系人按钮响应
                selectedFragment = new ContactsFragment();
                fragmentTransaction.add(R.id.layoutFragmentContainer, selectedFragment, MainActivity.TAG_CONTACTS_FRAGMENT);
                fragments.put(MainActivity.TAG_CONTACTS_FRAGMENT, selectedFragment);
            } else if (MainActivity.TAG_PANDORA_FRAGMENT.equals(button.getTag().toString())) {         //潘多拉按钮响应
                selectedFragment = new PandoraFragment();
                fragmentTransaction.add(R.id.layoutFragmentContainer, selectedFragment, MainActivity.TAG_PANDORA_FRAGMENT);
                fragments.put(MainActivity.TAG_PANDORA_FRAGMENT, selectedFragment);
            }
        }
        fragmentTransaction.hide(currentFragment);                        //隐藏原视图
        fragmentTransaction.show(selectedFragment);
        fragmentTransaction.commit();                                     //显示新视图

        fragments.put(MainActivity.TAG_CURRENT_FRAGMENT, selectedFragment);                 //设置当前视图，以便下次切换时使用

    /*
        //以上是onClick新逻辑，2016/3/21
        //以下是onClick原逻辑，2016/3/20
        if (sl_main_button.getTag() == MainActivity.TAG_CHAT_FRAGMENT) {
            Fragment chatFragment = fragments.get(sl_main_button.getTag());
            if (chatFragment == currentFragment) {
                return;
            }
            else {

                fragmentTransaction.hide(currentFragment);
            }
            if (chatFragment != null) {
                fragmentTransaction.show(chatFragment);

            }
            fragments.put(MainActivity.TAG_CURRENT_FRAGMENT, chatFragment);
        }

        if (sl_main_button.getTag() == MainActivity.TAG_CONTACTS_FRAGMENT) {
            Fragment contactsFragment = fragments.get(sl_main_button.getTag());
            if (contactsFragment == currentFragment) {
                return;
            }
            else {
                fragmentTransaction.hide(currentFragment);
            }
            if (contactsFragment != null) {
                fragmentTransaction.show(contactsFragment);
            }
            else {
                fragmentTransaction.hide(currentFragment);
                contactsFragment = new ContactsFragment();
                fragmentTransaction.add(R.id.fragment_layout, contactsFragment, MainActivity.TAG_CONTACTS_FRAGMENT);
                fragmentTransaction.show(contactsFragment);
                fragments.put(MainActivity.TAG_CONTACTS_FRAGMENT, contactsFragment);
            }
            fragments.put(MainActivity.TAG_CURRENT_FRAGMENT, contactsFragment);
        }

        if (sl_main_button.getTag() == MainActivity.TAG_PANDORA_FRAGMENT) {
            Fragment pandoraFragment = fragments.get(sl_main_button.getTag());
            if (pandoraFragment == currentFragment) {
                return;
            }
            else {
                fragmentTransaction.hide(currentFragment);
            }
            if (pandoraFragment != null) {
                fragmentTransaction.show(pandoraFragment);
            }
            else {
                fragmentTransaction.hide(currentFragment);
                pandoraFragment = new PandoraFragment();
                fragmentTransaction.add(R.id.fragment_layout, pandoraFragment, MainActivity.TAG_PANDORA_FRAGMENT);
                fragmentTransaction.show(pandoraFragment);
                fragments.put(MainActivity.TAG_PANDORA_FRAGMENT, pandoraFragment);
            }
            fragments.put(MainActivity.TAG_CURRENT_FRAGMENT, pandoraFragment);
        }
        fragmentTransaction.commit();
    }
    */
    }
}
