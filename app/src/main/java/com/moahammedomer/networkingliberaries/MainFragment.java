package com.moahammedomer.networkingliberaries;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MainFragment extends Fragment implements TabLayout.OnTabSelectedListener{


    public TabLayout tab;
    public ViewPager pager;
    public ViewPagerAdapter adapter;
    public TabFragment allCategoriesFragment = null;
    public TabFragment category1Fragment = null;
    public TabFragment category2Fragment = null;
    public TabFragment category3Fragment = null;
    public static final String CAT1 = "Category1";
    public static final String CAT2 = "Category2";
    public static final String CAT3 = "Category3";
    public static final String NAME_EXTRA = "name";
    public static final String IMAGE_EXTRA = "image";
    public static final String DESCRIPTION_EXTRA = "description";
    public static final String PRICE_EXTRA = "price";
    public static final String COUNTRY_EXTRA = "country";
    public static final String CATEGORY_EXTRA = "category";
    public static boolean error = false;
    public ArrayList<Product> allProducts;
    public ArrayList<Product> firstCategoryList;
    public ArrayList<Product> secondCategoryList;
    public ArrayList<Product> thirdCategoryList;
    View fragment;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (fragment == null){
            fragment =  inflater.inflate(R.layout.fragment_main, container, false);
        }
        TextView title = Objects.requireNonNull(getActivity()).findViewById(R.id.toolbar_title);
        title.setText(R.string.products_toolbar_title);
        pager = fragment.findViewById(R.id.pager);
        // to load all tab fragments without navigating to them
        pager.setOffscreenPageLimit(4);
        // fixed a potential problem by passing getChildFragmentManager() , tab fragment where not
        // showing after renavigating to them
        adapter = new ViewPagerAdapter(getChildFragmentManager());
        // don't created a new fragment if there is already exist one
        if (allCategoriesFragment ==  null){
            allCategoriesFragment = new TabFragment();
            category1Fragment = new TabFragment();
            category2Fragment = new TabFragment();
            category3Fragment = new TabFragment();
        }
        adapter.addFragment(allCategoriesFragment, getString(R.string.all_categories_title));
        adapter.addFragment(category1Fragment, getString(R.string.category1_title));
        adapter.addFragment(category2Fragment, getString(R.string.category2_title));
        adapter.addFragment(category3Fragment, getString(R.string.category3_title));
        pager.setAdapter(adapter);

        tab = fragment.findViewById(R.id.tab_layout);
        tab.setTabMode(TabLayout.MODE_SCROLLABLE);
        tab.setupWithViewPager(pager);
        if (MainActivity.start || error){
            allProducts = new ArrayList<>();
            firstCategoryList = new ArrayList<>();
            secondCategoryList = new ArrayList<>();
            thirdCategoryList = new ArrayList<>();
            MainActivity.start = false;
            loadProductList();
        }
        return fragment;
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
        //TODO go to the top of the current fragment
    }

    class ViewPagerAdapter extends FragmentPagerAdapter{

        private List<String> titleList = new ArrayList<>();
        private List<Fragment> fragmentList = new ArrayList<>();

        ViewPagerAdapter(FragmentManager fm) {
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

        void addFragment(Fragment fragment, String title){
            fragmentList.add(fragment);
            titleList.add(title);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titleList.get(position);
        }
    }


    public void loadProductList(){

        if (allCategoriesFragment.progressBar != null && allCategoriesFragment.recyclerView != null){
            hideList();
        }
        Log.e("main", "requesting");
        String response_url = MainActivity.SERVER_URL + "request_data.php";
        StringRequest request = new StringRequest(Request.Method.POST, response_url,
                response -> {
                    this.error = false;
                    try {
                        JSONArray array = new JSONArray(response);
                        allProducts.clear();
                        firstCategoryList.clear();
                        secondCategoryList.clear();
                        thirdCategoryList.clear();
                        for(int i = 0; i < array.length(); i++){
                            JSONObject object = array.getJSONObject(i);
                            allProducts.add(new Product(object.getInt("id"),
                                    object.getString("name"),
                                    object.getDouble("price"),
                                    object.getString("description"),
                                    object.getString("category"),
                                    object.getString("country")));
                        }
                        for (int i = 0; i < allProducts.size(); i++){
                            switch (allProducts.get(i).getCategory()){
                                case MainFragment.CAT1:
                                    firstCategoryList.add(allProducts.get(i));
                                    break;
                                case MainFragment.CAT2:
                                    secondCategoryList.add(allProducts.get(i));
                                    break;
                                case MainFragment.CAT3:
                                    thirdCategoryList.add(allProducts.get(i));
                                    break;
                            }
                        }
                        if (getActivity() != null){
                            makeAndAttachAdapters();
                        }
                    } catch (JSONException e) {
                        if (getActivity() != null){
                            Toast.makeText(getActivity().getApplicationContext(), "An error has accured", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, error -> {
                    this.error = true;
                    if (getActivity() != null){
                        Toast.makeText(getActivity().getApplicationContext(), error.getMessage() + "error accured", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getActivity().getApplicationContext(), NoInternetConnection.class));
                        MainActivity.start = true;
                        getActivity().finish();
                        this.error = false;
                    }
                }){
            @Override
            protected Map<String, String> getParams() {
                return new HashMap<>();
            }
        };

        MySingleton.getInstance(getActivity()).addToRequestQueue(request);
    }

    private void makeAndAttachAdapters() {
        if (getActivity() != null){
            // use getApplicationContext to load the list even if the user navigated to any part of the app
            MyRecyclerViewAdapter allCategoriesAdapter = new MyRecyclerViewAdapter(Glide.with(getActivity().getApplicationContext()),
                    getActivity().getApplicationContext(),
                    allProducts,
                    initListener(getActivity().getApplicationContext(), allProducts));
            MyRecyclerViewAdapter firstCategoryAdapter = new MyRecyclerViewAdapter(Glide.with(getActivity().getApplicationContext()),
                    getActivity().getApplicationContext(),
                    firstCategoryList,
                    initListener(getActivity().getApplicationContext(), firstCategoryList));
            MyRecyclerViewAdapter secondCategoryAdapter = new MyRecyclerViewAdapter(Glide.with(getActivity().getApplicationContext()),
                    getActivity().getApplicationContext(),
                    secondCategoryList,
                    initListener(getActivity().getApplicationContext(), secondCategoryList));
            MyRecyclerViewAdapter thirdCategoryAdapter = new MyRecyclerViewAdapter(Glide.with(getActivity().getApplicationContext()),
                    getActivity().getApplicationContext(),
                    thirdCategoryList,
                    initListener(getActivity().getApplicationContext(), thirdCategoryList));
            displayList();
            allCategoriesFragment.recyclerView.setAdapter(allCategoriesAdapter);
            category1Fragment.recyclerView.setAdapter(firstCategoryAdapter);
            category2Fragment.recyclerView.setAdapter(secondCategoryAdapter);
            category3Fragment.recyclerView.setAdapter(thirdCategoryAdapter);
        }
    }

    private void displayList() {
        allCategoriesFragment.progressBar.setVisibility(View.GONE);
        category1Fragment.progressBar.setVisibility(View.GONE);
        category2Fragment.progressBar.setVisibility(View.GONE);
        category3Fragment.progressBar.setVisibility(View.GONE);
        allCategoriesFragment.recyclerView.setVisibility(View.VISIBLE);
        category1Fragment.recyclerView.setVisibility(View.VISIBLE);
        category2Fragment.recyclerView.setVisibility(View.VISIBLE);
        category3Fragment.recyclerView.setVisibility(View.VISIBLE);
    }

    private void hideList() {
        allCategoriesFragment.progressBar.setVisibility(View.VISIBLE);
        category1Fragment.progressBar.setVisibility(View.VISIBLE);
        category2Fragment.progressBar.setVisibility(View.VISIBLE);
        category3Fragment.progressBar.setVisibility(View.VISIBLE);
        allCategoriesFragment.recyclerView.setVisibility(View.GONE);
        category1Fragment.recyclerView.setVisibility(View.GONE);
        category2Fragment.recyclerView.setVisibility(View.GONE);
        category3Fragment.recyclerView.setVisibility(View.GONE);
    }

    public static MyRecyclerViewAdapter.OnItemClickListener  initListener(Context context, ArrayList<Product> list){
        return (view, position) ->{
            Intent intent = new Intent(context, ProductDetailActivity.class);
            Product product = list.get(position);
            intent.putExtra(NAME_EXTRA, product.getName());
            intent.putExtra(PRICE_EXTRA, product.getPrice());
            intent.putExtra(DESCRIPTION_EXTRA, product.getDescription());
            intent.putExtra(COUNTRY_EXTRA, product.getCountry());
            intent.putExtra(CATEGORY_EXTRA, product.getCategory());
            intent.putExtra(IMAGE_EXTRA, product.getImageUrl());
            context.startActivity(intent);

        };
    }

    public void setAllFragmentListener(MyRecyclerViewAdapter.OnItemClickListener listener){
        this.allCategoriesFragment.listener = listener;
    }
//    public void setFirstCategoryListFragmentListener(MyRecyclerViewAdapter.OnItemClickListener listener){
//        this.category1Fragment.listener = listener;
//    }
//    public void setSecondCategoryListFragmentListener(MyRecyclerViewAdapter.OnItemClickListener listener){
//        this.category2Fragment.listener = listener;
//    }
//    public void setThirdCategoryListFragmentListener(MyRecyclerViewAdapter.OnItemClickListener listener){
//        this.category3Fragment.listener = listener;
//    }
    public void setAllFragmentAdapter(MyRecyclerViewAdapter adapter){
        this.allCategoriesFragment.recyclerView.setAdapter(adapter);
    }
//    public void setCategory1FragmentAdapter(MyRecyclerViewAdapter adapter){
//        this.category1Fragment.recyclerView.setAdapter(adapter);
//    }
//    public void setCategory2FragmentAdapter(MyRecyclerViewAdapter adapter){
//        this.category2Fragment.recyclerView.setAdapter(adapter);
//    }
//    public void setCategory3FragmentAdapter(MyRecyclerViewAdapter adapter){
//        this.category3Fragment.recyclerView.setAdapter(adapter);
//    }
    public ArrayList<Product> getAllProducts(){
        return this.allProducts;
    }

}
