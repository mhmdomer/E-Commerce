package com.moahammedomer.networkingliberaries;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

public class TabFragment extends Fragment {

    RecyclerView recyclerView;
    ProgressBar progressBar;
    MyRecyclerViewAdapter.OnItemClickListener listener;
    View fragment;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if(fragment == null) {
            fragment = inflater.inflate(R.layout.fragment_tab, container, false);
            recyclerView = fragment.findViewById(R.id.recycler);
            progressBar = fragment.findViewById(R.id.progress);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(layoutManager);
            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                    layoutManager.getOrientation());
            if (getActivity() != null)
                dividerItemDecoration.setDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.list_devider));
            recyclerView.addItemDecoration(dividerItemDecoration);
            // for caching images
            recyclerView.setHasFixedSize(true);
            recyclerView.setItemViewCacheSize(20);
            recyclerView.setDrawingCacheEnabled(true);
            recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        }else {
            recyclerView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        }
        return fragment;
    }


}
