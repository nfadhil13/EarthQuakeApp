package com.fdev.earthquakeapp.service.remote;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class EarthQuakeRemoteSingleton {

    private static EarthQuakeRemoteSingleton instance;
    private RequestQueue requestQueue;
    private static Context mContext;

    private EarthQuakeRemoteSingleton(Context context){
        mContext = context;
        requestQueue = getRequestQueue();
    }

    public static synchronized EarthQuakeRemoteSingleton getInstance(Context context) {
        if (instance == null) {
            instance = new EarthQuakeRemoteSingleton(context);
        }
        return instance;
    }

    public RequestQueue getRequestQueue() {
        if(requestQueue == null){
            requestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        }
        return  requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

}
