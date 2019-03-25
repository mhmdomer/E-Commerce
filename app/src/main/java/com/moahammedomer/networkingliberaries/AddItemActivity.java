package com.moahammedomer.networkingliberaries;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
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
import java.util.Objects;

public class AddItemActivity extends AppCompatActivity {

    ProgressBar progressBar;
    Button b, select;
    EditText name;
    private String response_url = MainActivity.SERVER_URL + "/upload_product.php";
    ImageView imageView;
    final static int IMAGE_REQUEST = 100;
    Uri imageUri;
    byte [] BYTE;
    Toolbar toolbar;
    TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        title = findViewById(R.id.toolbar_title);
        title.setText(R.string.add_item_title);
        b = findViewById(R.id.button);
        select = findViewById(R.id.img_select);
        name = findViewById(R.id.name);
        imageView = findViewById(R.id.img);
        progressBar = new ProgressBar(this, null, android.R.attr.progressBarStyleSmall);
        select.setOnClickListener( v -> {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), IMAGE_REQUEST);
        });
        b.setOnClickListener( v ->{
                if(imageView.getDrawable() == null || name.getText().toString().isEmpty()){
                    Toast.makeText(AddItemActivity.this, "Please select an Image and a name", Toast.LENGTH_SHORT).show();
                }
                else{
                    StringRequest request = new StringRequest(Request.Method.POST, response_url,
                            response ->{

                                    Toast.makeText(AddItemActivity.this, response, Toast.LENGTH_LONG).show();
                                    name.setText("");
                                    imageView.setImageBitmap(null);
                                    startActivity(new Intent(AddItemActivity.this, MainActivity.class));

                            }, error ->{
                            Toast.makeText(AddItemActivity.this, "Something went wrong!", Toast.LENGTH_LONG).show();
                    }){
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String > map = new HashMap<>();
                            map.put("name", name.getText().toString());
                            imageView.buildDrawingCache();
                            BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
                            Bitmap bitmap = drawable.getBitmap();
                            ByteArrayOutputStream bos = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, bos);
                            BYTE = bos.toByteArray();
                            Bitmap bt2 = BitmapFactory.decodeByteArray(BYTE, 0, BYTE.length);
                            map.put("image", imageToString(bt2));

                            return map;
                        }
                    };
                    request.setRetryPolicy(new DefaultRetryPolicy(
                            0,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    MySingleton.getInstance(AddItemActivity.this).addToRequestQueue(request);
                }

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == IMAGE_REQUEST && resultCode == RESULT_OK){
            if (data != null){
                imageUri = data.getData();
                try {
                    Bitmap bitmap = getResizedBitmap(MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri),
                            320,
                            320);
                    imageView.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private String imageToString(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imgBytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgBytes, Base64.DEFAULT);
    }

    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }
}
