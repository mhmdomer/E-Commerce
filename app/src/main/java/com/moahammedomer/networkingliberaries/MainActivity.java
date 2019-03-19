package com.moahammedomer.networkingliberaries;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{


    public static final String SERVER_URL = "https://f3d3b99b.ngrok.io/";
    Toolbar toolbar;
    private DrawerLayout drawer;
    MenuItem search;
    public static MainFragment mainFragment = null;
    public static final String MAIN_FRAGMENT_TAG = "main tag";
    NavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        toggle.syncState();
        // don't created a new fragment if there is already exist one
        if (mainFragment == null) {
            mainFragment = new MainFragment();
        }
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.fragment_container, mainFragment, MAIN_FRAGMENT_TAG).commit();

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.main_page:
                search.setVisible(true);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MainFragment(),MAIN_FRAGMENT_TAG).commit();
                drawer.closeDrawer(GravityCompat.START);
                return true;
            case R.id.my_info:
                search.setVisible(false);
                //TODO launch users info fragment
                drawer.closeDrawer(GravityCompat.START);
                return true;
            case R.id.about:
                search.setVisible(false);
                //TODO launch about fragment
                drawer.closeDrawer(GravityCompat.START);
                return true;
            case R.id.contact_us:
                search.setVisible(false);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ContactUsFragment()).commit();
                drawer.closeDrawer(GravityCompat.START);
                return true;
            case R.id.share_app:
                //TODO fire share app intent
                drawer.closeDrawer(GravityCompat.START);
                return true;
            case R.id.rate_app:
                //TODO fire rate app intent
                drawer.closeDrawer(GravityCompat.START);
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        MainFragment f = (MainFragment) getSupportFragmentManager().findFragmentByTag(MAIN_FRAGMENT_TAG);
        if (drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }
        else if (!(getSupportFragmentManager().findFragmentById(R.id.fragment_container) instanceof MainFragment)){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MainFragment()).commit();
            navigationView.getMenu().getItem(0).setChecked(true);
            search.setVisible(true);
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        loadProductList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        search = menu.findItem(R.id.search_products);
        SearchView searchView=(SearchView)search.getActionView();
        search(searchView);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add:
                Intent intent = new Intent(MainActivity.this, AddItemActivity.class);
                startActivity(intent);
                return true;
        }
        return true;
    }

    private void search(SearchView searchView) {
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainFragment.tab.getTabAt(0).select();
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                getMatchProducts(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                getMatchProducts(newText);
                return true;
            }
        });
    }

    public void getMatchProducts(String search){
        ArrayList<Product> arrayList = new ArrayList<>();
        int length = AllFragment.allProducts.size();
        for(int i = 0; i < length; i++){
            if(AllFragment.allProducts.get(i).getName().contains(search)){
                arrayList.add(AllFragment.allProducts.get(i));
            }
        }
        AllFragment.listener = AllFragment.initListener(this, arrayList);
        AllFragment.adapter = new MyRecyclerViewAdapter(this, arrayList, AllFragment.listener);
        AllFragment.recyclerView.setAdapter(AllFragment.adapter);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

}