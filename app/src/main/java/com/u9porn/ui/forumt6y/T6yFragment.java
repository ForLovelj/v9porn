package com.u9porn.ui.forumt6y;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.u9porn.R;
import com.u9porn.ui.BaseFragment;

/**
 * A simple {@link Fragment} subclass.
 * @author flymegoc
 */
public class T6yFragment extends BaseFragment {


    public T6yFragment() {
        // Required empty public constructor
    }

    public static T6yFragment getInstance(){
        return new T6yFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_t6y, container, false);
    }

}
