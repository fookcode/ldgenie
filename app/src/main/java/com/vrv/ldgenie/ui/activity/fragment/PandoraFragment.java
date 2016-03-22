package com.vrv.ldgenie.ui.activity.fragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vrv.ldgenie.R;

/**
 * Created by kinee on 2016/3/20.
 */
public class PandoraFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pandora, container, false);
    }
}
