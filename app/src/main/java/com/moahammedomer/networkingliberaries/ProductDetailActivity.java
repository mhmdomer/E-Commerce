package com.moahammedomer.networkingliberaries;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class ProductDetailActivity extends AppCompatActivity {

    TextView price, description, country, category;
    ImageView image;
    Button order;
    Dialog imageDialog;
    Toolbar toolbar;
    TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        title = findViewById(R.id.toolbar_title);
        title.setText(R.string.view_item_title);
        Intent intent = getIntent();
        price = findViewById(R.id.detail_price);
        description = findViewById(R.id.detail_description);
        country = findViewById(R.id.detail_country);
        category = findViewById(R.id.detial_category);
        image = findViewById(R.id.detail_image);
        order = findViewById(R.id.detail_order);
        description.setText(intent.getExtras().getString(AllFragment.DESCRIPTION_EXTRA));
        country.setText(intent.getExtras().getString(AllFragment.COUNTRY_EXTRA));
        category.setText(intent.getExtras().getString(AllFragment.CATEGORY_EXTRA));
        Glide.with(this)
                .load(intent.getExtras().getString(AllFragment.IMAGE_EXTRA))
                .into(image);
        image.setClickable(true);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog d = new Dialog(ProductDetailActivity.this);
                d.setContentView(R.layout.image_layout);
                ImageView imageView = d.findViewById(R.id.image_preview);
                Glide.with(ProductDetailActivity.this)
                        .load(intent.getExtras().getString(AllFragment.IMAGE_EXTRA))
                        .into(imageView);
                d.show();
            }
        });

    }

//    @Override
//    public void onBackPressed() {
//        startActivity(new Intent(this, MainActivity.class));
//    }
}
