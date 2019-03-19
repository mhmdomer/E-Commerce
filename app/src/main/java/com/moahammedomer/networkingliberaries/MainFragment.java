package com.moahammedomer.networkingliberaries;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainFragment extends Fragment {


    public static TabLayout tab;
    public static ViewPager pager;
    public static ViewPagerAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View fragment =  inflater.inflate(R.layout.fragment_main, container, false);
        TextView title = getActivity().findViewById(R.id.toolbar_title);
        title.setText(R.string.products_toolbar_title);
        Log.e("main", "main fragment starting..");
        pager = fragment.findViewById(R.id.pager);
        pager.setOffscreenPageLimit(4);
        adapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());
        adapter.addFragment(new AllFragment(), "All");
        adapter.addFragment(new Category1Fragment(), "Category1");
        adapter.addFragment(new Category2Fragment(), "Category2");
        adapter.addFragment(new Category3Fragment(), "Category3");
        pager.setAdapter(adapter);

        tab = fragment.findViewById(R.id.tab_layout);
        tab.setTabMode(TabLayout.MODE_SCROLLABLE);
        tab.setupWithViewPager(pager);
        return fragment;
    }

    class ViewPagerAdapter extends FragmentPagerAdapter{

        private List<String> titleList = new ArrayList<>();
        private List<Fragment> fragmentList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            return fragmentList.get(i);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        public void addFragment(Fragment fragment, String title){
            fragmentList.add(fragment);
            titleList.add(title);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titleList.get(position);
        }
    }

}
