package com.moahammedomer.networkingliberaries;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;


public class ContactUsFragment extends Fragment {


    public ContactUsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragment =  inflater.inflate(R.layout.fragment_contact_us, container, false);
        TextView title = getActivity().findViewById(R.id.toolbar_title);
        title.setText(R.string.contact_us_title);
        return fragment;
    }

}
