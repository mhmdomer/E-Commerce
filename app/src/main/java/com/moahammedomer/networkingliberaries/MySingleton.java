package com.moahammedomer.networkingliberaries;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class MySingleton {

    private static MySingleton instance;
    private RequestQueue requestQueue;
    private Context ctx;

    private MySingleton(Context context){
        ctx = context;
        requestQueue = getRequestQueue();
    }

    public RequestQueue getRequestQueue() {
        if(requestQueue == null){
            requestQueue = Volley.newRequestQueue(ctx.getApplicationContext());
        }
        return requestQueue;
    }

    public static synchronized MySingleton getInstance(Context context){

        if(instance == null){
            instance = new MySingleton(context);
        }
        return instance;
    }

    public <T> void  addToRequestQueue(Request<T> request){
        getRequestQueue().add(request);
    }
}
