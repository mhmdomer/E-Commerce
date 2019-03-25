package com.moahammedomer.networkingliberaries;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

public class NoInternetConnection extends AppCompatActivity {

    Button b;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_internet_connection);
        b = findViewById(R.id.refresh);
        b.setOnClickListener(v -> {
                Intent intent = new Intent(NoInternetConnection.this,MainActivity.class);
                startActivity(intent);
            });

    }


}