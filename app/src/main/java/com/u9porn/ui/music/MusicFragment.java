package com.u9porn.ui.music;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.u9porn.R;
import com.u9porn.ui.BaseFragment;

/**
 * A simple {@link Fragment} subclass.
 *
 * @author flymegoc
 */
public class MusicFragment extends BaseFragment {


    public MusicFragment() {
        // Required empty public constructor
    }

    public static MusicFragment getInstance() {
        return new MusicFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_music, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
