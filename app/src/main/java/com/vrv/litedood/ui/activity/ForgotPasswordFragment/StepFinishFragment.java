package com.vrv.litedood.ui.activity.ForgotPasswordFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vrv.litedood.R;
import com.vrv.litedood.ui.activity.LoginActivity;

/**
 * Created by kinee on 2016/4/19.
 */
public class StepFinishFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forgot_password_step_finish, null);
        view.findViewById(R.id.btnStepFinished).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginActivity.startLoginActivity(getActivity());
            }
        });


        return view;
        //return super.onCreateView(inflater, container, savedInstanceState);
    }
}
