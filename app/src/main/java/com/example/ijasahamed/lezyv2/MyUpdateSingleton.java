package com.example.ijasahamed.lezyv2;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class MyUpdateSingleton {

    private static MyUpdateSingleton myUpdateSingleton;
    private RequestQueue requestQueue;
    private Context mContext;


    private MyUpdateSingleton(Context context){
        mContext = context;
        requestQueue =getRequestQueue();

    }

    public static synchronized MyUpdateSingleton getInstance(Context context){

        if (myUpdateSingleton == null){
            myUpdateSingleton = new MyUpdateSingleton(context);
        }
        return myUpdateSingleton;
    }

    public RequestQueue getRequestQueue(){
        if(requestQueue == null){
            requestQueue = Volley.newRequestQueue(mContext);

        }
        return requestQueue;
    }

    public <T>void addToRequestQue(Request<T> request){
        requestQueue.add(request);
    }


}
