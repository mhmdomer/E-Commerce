package com.moahammedomer.networkingliberaries;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;

public class ProductDetailActivity extends AppCompatActivity {

    TextView price, description, country, category;
    ImageView image;
    Button order;
    Toolbar toolbar;
    TextView title;
    Activity activity;
    Context context;
    Bitmap bitmap;
    public static int width = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        activity = this;
        if (width == -1){
            width = findViewById(R.id.detail_container).getWidth();
        }
        context = this;
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
        description.setText(intent.getExtras().getString(MainFragment.DESCRIPTION_EXTRA));
        country.setText(intent.getExtras().getString(MainFragment.COUNTRY_EXTRA));
        category.setText(intent.getExtras().getString(MainFragment.CATEGORY_EXTRA));
        Glide.with(this)
                .load(intent.getExtras().getString(MainFragment.IMAGE_EXTRA))
                .into(image);
        image.setClickable(true);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (image.getDrawable() != null) {
                    bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
                    final Dialog nagDialog = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
                    nagDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    nagDialog.setCancelable(true);
                    nagDialog.setContentView(R.layout.image_layout);
                    PhotoView view = nagDialog.findViewById(R.id.image_preview);
                    view.setImageBitmap(bitmap);
                    nagDialog.show();
                }
            }
        });

    }

//    @Override
//    public void onBackPressed() {
//        startActivity(new Intent(this, MainActivity.class));
//    }
}
