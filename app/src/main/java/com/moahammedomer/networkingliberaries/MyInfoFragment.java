package com.moahammedomer.networkingliberaries;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class MyInfoFragment extends Fragment {


    View fragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragment = inflater.inflate(R.layout.fragment_my_info, container, false);
        TextView title = getActivity().findViewById(R.id.toolbar_title);
        title.setText(R.string.my_info_nav);
        return fragment;
    }

}
