//************************************************************//
//File Name : MessageFragment.java
//Author    : kinee
//Mailto    : kinee@163.com
//Comment   : 
//Date      : Mar 18, 2016
//************************************************************//

package com.vrv.ldgenie.ui.activity.fragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vrv.ldgenie.R;

public class MessageFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_chat, container, false);
	}
}
