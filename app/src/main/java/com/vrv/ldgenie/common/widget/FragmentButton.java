//************************************************************//
//File Name : FragmentButton.java
//Author    : kinee
//Mailto    : kinee@163.com
//Comment   : 
//Date      : Mar 18, 2016
//************************************************************//

package com.vrv.ldgenie.common.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class FragmentButton extends Button {
	private String tag = "";
	

	public FragmentButton(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public FragmentButton(Context context, AttributeSet attrs) {
		super(context, attrs);

	}

	public FragmentButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public void setTag(String FragmentTag) {
		this.tag = FragmentTag;
		this.setOnClickListener(
                new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//Log.v(tag, v.getParent().getClass().getName());
                LinearLayout ll = (LinearLayout)v.getParent();

//                MessageFragment fragment = new MessageFragment();
//                FragmentTransaction ft = getFragmentManager().beginTransaction();
//                ft.replace(R.id.fragment_layout, fragment, TAG_CHAT_FRAGMENT);
//                ft.commit();
			}
		});
	}

}
