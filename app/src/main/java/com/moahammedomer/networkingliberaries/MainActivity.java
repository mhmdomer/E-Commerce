package com.moahammedomer.networkingliberaries;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    ProgressBar progressBar;
    Button b, select;
    EditText name;
    private String response_url = "http://095c6044.ngrok.io/test.php";
    ImageView imageView;
    final static int IMAGE_REQUEST = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        b = findViewById(R.id.button);
        select = findViewById(R.id.img_select);
        name = findViewById(R.id.name);
        imageView = findViewById(R.id.img);
        progressBar = new ProgressBar(this, null, android.R.attr.progressBarStyleSmall);
        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), IMAGE_REQUEST);
            }
        });
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(imageView.getDrawable() == null || name.getText().toString().isEmpty()){
                    Toast.makeText(MainActivity.this, "Please select an Image and a name", Toast.LENGTH_SHORT).show();
                }
                else{
                    b.setClickable(false);
                    StringRequest request = new StringRequest(Request.Method.POST, response_url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                    Toast.makeText(MainActivity.this, response, Toast.LENGTH_LONG).show();
                                    name.setText("");
                                    imageView.setImageBitmap(null);
                                    startActivity(new Intent(MainActivity.this, Main2Activity.class));

                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(MainActivity.this, "Something went wrong!", Toast.LENGTH_LONG).show();
                        }
                    }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String > map = new HashMap<>();
                            map.put("name", name.getText().toString());
                            imageView.buildDrawingCache();
                            BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
                            Bitmap bitmap = drawable.getBitmap();
                            map.put("image", imageToString(bitmap));

                            return map;
                        }
                    };
                    request.setRetryPolicy(new DefaultRetryPolicy(
                            0,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    MySingleton.getInstance(MainActivity.this).addToRequestQueue(request);
                }

            }

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == IMAGE_REQUEST && resultCode == RESULT_OK){
            Uri imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String imageToString(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayOutputStream);
        byte[] imgBytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgBytes, Base64.DEFAULT);
    }
}
