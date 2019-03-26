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
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{


    public static final String SERVER_URL = "http://mohammedomerali.000webhostapp.com/";
    Toolbar toolbar;
    private DrawerLayout drawer;
    public static final String MAIN_FRAGMENT_TAG = "main tag";
    MenuItem search;
    NavigationView navigationView;
    SearchView searchView;
    public static boolean start = true;
    public MainFragment mainFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        toggle.syncState();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (mainFragment == null){
            mainFragment = new MainFragment();
        }
        ft.add(R.id.fragment_container, mainFragment, MAIN_FRAGMENT_TAG).commit();
        navigationView.getMenu().getItem(0).setChecked(true);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.main_page:
                search.setVisible(true);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, mainFragment,MAIN_FRAGMENT_TAG).commit();
                drawer.closeDrawer(GravityCompat.START);
                searchView.clearFocus();
                return true;
            case R.id.my_info:
                search.setVisible(false);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MyInfoFragment(),MAIN_FRAGMENT_TAG).commit();
                drawer.closeDrawer(GravityCompat.START);
                searchView.clearFocus();
                return true;
            case R.id.about:
                search.setVisible(false);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AboutTheAppFragment(),MAIN_FRAGMENT_TAG).commit();
                drawer.closeDrawer(GravityCompat.START);
                searchView.clearFocus();
                return true;
            case R.id.contact_us:
                search.setVisible(false);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ContactUsFragment()).commit();
                drawer.closeDrawer(GravityCompat.START);
                searchView.clearFocus();
                return true;
            case R.id.refresh_products:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, mainFragment).commit();
                drawer.closeDrawer(GravityCompat.START);
                mainFragment.loadProductList();
                navigationView.getMenu().getItem(0).setChecked(true);
                search.setVisible(true);
                return true;
            case R.id.share_app:
                Toast.makeText(this, "start share app intent", Toast.LENGTH_SHORT).show();
                drawer.closeDrawer(GravityCompat.START);
                searchView.clearFocus();
                return true;
            case R.id.rate_app:
                Toast.makeText(this, "start rate app intent", Toast.LENGTH_SHORT).show();
                drawer.closeDrawer(GravityCompat.START);
                searchView.clearFocus();
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }
        else if (!(getSupportFragmentManager().findFragmentById(R.id.fragment_container) instanceof MainFragment)){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, mainFragment).commit();
            navigationView.getMenu().getItem(0).setChecked(true);
            search.setVisible(true);
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        search = menu.findItem(R.id.search_products);
        searchView=(SearchView)search.getActionView();
        searchView.setQueryHint(getString(R.string.search_hint));
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
        searchView.setOnSearchClickListener(v -> Objects.requireNonNull(mainFragment.tab.getTabAt(0)).select());
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!Objects.requireNonNull(mainFragment.tab.getTabAt(0)).isSelected()){
                    Objects.requireNonNull(mainFragment.tab.getTabAt(0)).select();
                }
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (!Objects.requireNonNull(mainFragment.tab.getTabAt(0)).isSelected()){
                    Objects.requireNonNull(mainFragment.tab.getTabAt(0)).select();
                }
                getMatchProducts(newText);
                return true;
            }
        });
    }

    public void getMatchProducts(String search){
        ArrayList<Product> arrayList = new ArrayList<>();
        int length = mainFragment.getAllProducts().size();
        for(int i = 0; i < length; i++){
            if(mainFragment.getAllProducts().get(i).getName().toLowerCase().contains(search.toLowerCase())){
                arrayList.add(mainFragment.getAllProducts().get(i));
            }
        }
        mainFragment.setAllFragmentListener(MainFragment.initListener(this, arrayList));
        mainFragment.setAllFragmentAdapter(new MyRecyclerViewAdapter(
                Glide.with(this),
                this,
                arrayList, MainFragment.initListener(getApplicationContext(), arrayList)));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        start = true;
    }
}