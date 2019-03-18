package com.moahammedomer.networkingliberaries;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

public class ProductDetailActivity extends AppCompatActivity {

    TextView price, description, country, category;
    ImageView image;
    Button order;
    Dialog imageDialog;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        toolbar = findViewById(R.id.toolbar3);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        Intent intent = getIntent();
        price = findViewById(R.id.detail_price);
        description = findViewById(R.id.detail_description);
        country = findViewById(R.id.detail_country);
        category = findViewById(R.id.detial_category);
        image = findViewById(R.id.detail_image);
        order = findViewById(R.id.detail_order);
        description.setText(intent.getExtras().getString(Main2Activity.DESCRIPTION_EXTRA));
        country.setText(intent.getExtras().getString(Main2Activity.COUNTRY_EXTRA));
        category.setText(intent.getExtras().getString(Main2Activity.CATEGORY_EXTRA));
        Glide.with(this)
                .load(intent.getExtras().getString(Main2Activity.IMAGE_EXTRA))
                .into(image);
        image.setClickable(true);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog d = new Dialog(ProductDetailActivity.this);
                d.setContentView(R.layout.image_layout);
                ImageView imageView = d.findViewById(R.id.image_preview);
                Glide.with(ProductDetailActivity.this)
                        .load(intent.getExtras().getString(Main2Activity.IMAGE_EXTRA))
                        .into(imageView);
                d.show();
            }
        });

    }
}
