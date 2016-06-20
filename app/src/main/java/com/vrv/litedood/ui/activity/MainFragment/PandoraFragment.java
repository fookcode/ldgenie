package com.vrv.litedood.ui.activity.MainFragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.vrv.litedood.R;

/**
 * Created by kinee on 2016/3/20.
 */
public class PandoraFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View result =  inflater.inflate(R.layout.fragment_pandora, container, false);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "正在努力施工，敬请期待...", Toast.LENGTH_SHORT).show();
            }
        };

        AppCompatImageView canteen = (AppCompatImageView)result.findViewById(R.id.ivPandoraCanteen);
        canteen.setOnClickListener(listener);
        AppCompatImageView meeting = (AppCompatImageView)result.findViewById(R.id.ivPandoraMeeting);
        meeting.setOnClickListener(listener);
        AppCompatImageView library = (AppCompatImageView)result.findViewById(R.id.ivPandoraLibrary);
        library.setOnClickListener(listener);

        return result;

    }
}
