package com.moahammedomer.networkingliberaries;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class ProductDetailActivity extends AppCompatActivity {

    TextView price, description, country, category;
    ImageView image;
    Button order;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        Intent intent = getIntent();
        price = findViewById(R.id.detail_price);
        description = findViewById(R.id.detail_description);
        country = findViewById(R.id.detail_country);
        category = findViewById(R.id.detial_category);
        image = findViewById(R.id.detail_image);
        order = findViewById(R.id.detail_order);
        price.setText(String.valueOf(intent.getExtras().getDouble(Main2Activity.PRICE_EXTRA) + getString(R.string.SDG_unit)));
        Log.e("detail", "price " + price.getText().toString());
        description.setText(intent.getExtras().getString(Main2Activity.DESCRIPTION_EXTRA));
        country.setText(intent.getExtras().getString(Main2Activity.COUNTRY_EXTRA));
        category.setText(intent.getExtras().getString(Main2Activity.CATEGORY_EXTRA));
        Glide.with(this)
                .load(intent.getExtras().getString(Main2Activity.IMAGE_EXTRA))
                .into(image);

    }
}
