package com.moahammedomer.networkingliberaries;


import android.content.Context;
import android.content.Intent;
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
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainFragment extends Fragment implements TabLayout.OnTabSelectedListener{


    public static TabLayout tab;
    public static ViewPager pager;
    public static ViewPagerAdapter adapter;
    public static AllFragment allFragment = null;
    public static Category1Fragment category1Fragment = null;
    public static Category2Fragment category2Fragment = null;
    public static Category3Fragment category3Fragment = null;
    public static final String CAT1 = "Category1";
    public static final String CAT2 = "Category2";
    public static final String CAT3 = "Category3";
    private String response_url = MainActivity.SERVER_URL + "request_data.php";
    public static final String NAME_EXTRA = "name";
    public static final String IMAGE_EXTRA = "image";
    public static final String DESCRIPTION_EXTRA = "description";
    public static final String PRICE_EXTRA = "price";
    public static final String COUNTRY_EXTRA = "country";
    public static final String CATEGORY_EXTRA = "category";
    public static ArrayList<Product> allProducts;
    public static ArrayList<Product> firstCategoryList;
    public static ArrayList<Product> secondCategoryList;
    public static ArrayList<Product> thirdCategoryList;
    View fragment;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (fragment == null){
            fragment =  inflater.inflate(R.layout.fragment_main, container, false);
        }
        TextView title = getActivity().findViewById(R.id.toolbar_title);
        title.setText(R.string.products_toolbar_title);
        pager = fragment.findViewById(R.id.pager);
        // to load all tab fragments without navigating to them
        pager.setOffscreenPageLimit(4);
        // fixed a potential problem by passing getChildFragmentManager() , tab fragment where not
        // showing after renavigating to them
        adapter = new ViewPagerAdapter(getChildFragmentManager());
        // don't created a new fragment if there is already exist one
        if (allFragment ==  null){
            allFragment = new AllFragment();
            category1Fragment = new Category1Fragment();
            category2Fragment = new Category2Fragment();
            category3Fragment = new Category3Fragment();
        }
        adapter.addFragment(allFragment, getString(R.string.all_categories_title));
        adapter.addFragment(category1Fragment, getString(R.string.category1_title));
        adapter.addFragment(category2Fragment, getString(R.string.category2_title));
        adapter.addFragment(category3Fragment, getString(R.string.category3_title));
        pager.setAdapter(adapter);

        tab = fragment.findViewById(R.id.tab_layout);
        tab.setTabMode(TabLayout.MODE_SCROLLABLE);
        tab.setupWithViewPager(pager);
        if (MainActivity.start){
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


    public void loadProductList(){

        Log.e("main", "requesting");
        StringRequest request = new StringRequest(Request.Method.POST, response_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            allProducts.clear();
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
                            Toast.makeText(getActivity().getApplicationContext(), "An error has accured", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (getContext() != null){
                    Toast.makeText(getContext().getApplicationContext(), error.getMessage() + "error accured", Toast.LENGTH_SHORT).show();
                }
                // TODO pop up a dialog to check internet connection and try again
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                return map;
            }
        };

        MySingleton.getInstance(getActivity()).addToRequestQueue(request);
    }

    private void makeAndAttachAdapters() {
        MyRecyclerViewAdapter allCategoriesAdapter = new MyRecyclerViewAdapter(getActivity().getApplicationContext(),
                allProducts,
                initListener(getActivity().getApplicationContext(), allProducts));
        MyRecyclerViewAdapter firstCategoryAdapter = new MyRecyclerViewAdapter(getActivity().getApplicationContext(),
                firstCategoryList,
                initListener(getActivity().getApplicationContext(), firstCategoryList));
        MyRecyclerViewAdapter secondCategoryAdapter = new MyRecyclerViewAdapter(getActivity().getApplicationContext(),
                secondCategoryList,
                initListener(getActivity().getApplicationContext(), secondCategoryList));
        MyRecyclerViewAdapter thirdCategoryAdapter = new MyRecyclerViewAdapter(getActivity().getApplicationContext(),
                thirdCategoryList,
                initListener(getActivity().getApplicationContext(), thirdCategoryList));
        allGone();
        allVisable();
        AllFragment.recyclerView.setAdapter(allCategoriesAdapter);
        Category1Fragment.recyclerView.setAdapter(firstCategoryAdapter);
        Category2Fragment.recyclerView.setAdapter(secondCategoryAdapter);
        Category3Fragment.recyclerView.setAdapter(thirdCategoryAdapter);
    }

    private void allVisable() {
        AllFragment.recyclerView.setVisibility(View.VISIBLE);
        Category1Fragment.recyclerView.setVisibility(View.VISIBLE);
        Category2Fragment.recyclerView.setVisibility(View.VISIBLE);
        Category3Fragment.recyclerView.setVisibility(View.VISIBLE);
    }

    private void allGone() {
        AllFragment.progressBar.setVisibility(View.GONE);
        Category1Fragment.progressBar.setVisibility(View.GONE);
        Category2Fragment.progressBar.setVisibility(View.GONE);
        Category3Fragment.progressBar.setVisibility(View.GONE);
    }

    public static MyRecyclerViewAdapter.OnItemClickListener  initListener(Context context, ArrayList<Product> list){
        MyRecyclerViewAdapter.OnItemClickListener mlistener  = (view, position) ->{
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
        return mlistener;
    }

}
