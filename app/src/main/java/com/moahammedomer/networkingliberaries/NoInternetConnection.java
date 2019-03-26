package com.moahammedomer.networkingliberaries;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.Toast;

public class NoInternetConnection extends AppCompatActivity {

    Button b;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_internet_connection);
        b = findViewById(R.id.refresh);
        b.setOnClickListener(v -> {
            NetworkInfo networkInfo =  ((ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()){
                MainActivity.start = true;
                Intent intent = new Intent(NoInternetConnection.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
            else {
                Toast.makeText(this, R.string.not_connected_to_a_network_message, Toast.LENGTH_SHORT).show();
            }
        });

    }


}