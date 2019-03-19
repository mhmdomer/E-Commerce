package com.moahammedomer.networkingliberaries;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
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
import java.util.Map;

public class AllFragment extends Fragment {

    public static RecyclerView recyclerView;
    public static MyRecyclerViewAdapter adapter;
    public static ArrayList<Product> allProducts;
    ProgressBar progressBar;
    SearchView sv;
    private String response_url = MainActivity.SERVER_URL + "request_data.php";
    public static final String NAME_EXTRA = "name";
    public static final String IMAGE_EXTRA = "image";
    public static final String DESCRIPTION_EXTRA = "description";
    public static final String PRICE_EXTRA = "price";
    public static final String COUNTRY_EXTRA = "country";
    public static final String CATEGORY_EXTRA = "category";
    public static MyRecyclerViewAdapter.OnItemClickListener listener;
    View fragment;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if(fragment == null){
            fragment = inflater.inflate(R.layout.fragment_all, container, false);
        }
        recyclerView = fragment.findViewById(R.id.recycler);
        progressBar = fragment.findViewById(R.id.progress);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        // for caching images
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        allProducts = new ArrayList<>();
        initListener(getActivity(),allProducts);
        adapter = new MyRecyclerViewAdapter(getActivity(), allProducts, listener);
        Log.e("main", "loading all fragment..");
        loadProductList();
        return fragment;
    }

    public static void initListener(Context context, ArrayList<Product> list){
        listener = listener = (view, position) ->{
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

    public void loadProductList(){

        Log.e("main", "requesting");
        StringRequest request = new StringRequest(Request.Method.POST, response_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("mian", response);
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
                            progressBar.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                            recyclerView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), error.getMessage() + "error accured", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
//                Log.e("main", "search string " + searchString);
//                map.put("search", searchString);
                return map;
            }
        };

        MySingleton.getInstance(getActivity()).addToRequestQueue(request);
    }
}
