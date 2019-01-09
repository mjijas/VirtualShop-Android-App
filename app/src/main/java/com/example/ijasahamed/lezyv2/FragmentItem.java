package com.example.ijasahamed.lezyv2;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.ijasahamed.lezyv2.adapters.RecyclerViewAdapter;
import com.example.ijasahamed.lezyv2.models.items;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.android.volley.Request.Method.GET;


public class FragmentItem extends Fragment {

    View v;

//    private final String Urlgetitems = "http://192.168.8.103:8080/Virtual_shop/getItems.php";
    private final String Urlgetitems = HomeActivity.BaseUrl+"getItems.php";

    private JsonArrayRequest request;
    private RequestQueue requestQueue;
    private List<items> isItem;
    private RecyclerView recyclerView;
    private int type;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       // return super.onCreateView(inflater, container, savedInstanceState);

// getting data from fragment




        isItem = new ArrayList<>();

        v = inflater.inflate(R.layout.items_fragment,container,false);
        recyclerView = v.findViewById(R.id.recyclerview);
        jsonreqest();
        return v;


//        View v = inflater.inflate(R.layout.fragment_genres, container, false);
//        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);
//        recyclerView.setHasFixedSize(true);
//        layoutManager = new LinearLayoutManager(getActivity());
//        recyclerView.setLayoutManager(layoutManager);
//        return v;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {

            type = getArguments().getInt("type");
            Log.i("type test", "onCreate: "+type);
        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        jsonreqest();
    }



    public void setuprecyclerview(List<items> itemsInstance) {
//        Toast.makeText(getContext(), "inside the function", Toast.LENGTH_SHORT).show();

        RecyclerViewAdapter myAdapter = new RecyclerViewAdapter(getContext(),itemsInstance);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(myAdapter);

    }


    private void jsonreqest(){
        requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        request = new JsonArrayRequest(GET,Urlgetitems,null ,new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                JSONObject jsonObject = null;
                for(int i = 0; i < response.length(); i++){
                    try {
                        jsonObject = response.getJSONObject(i);
                        items itemsobj = new items();
                        itemsobj.setItem_id(jsonObject.getInt("item_id"));
                        itemsobj.setItem_name(jsonObject.getString("item_name"));
                        itemsobj.setShop_id(jsonObject.getInt("shop_id"));
                        itemsobj.setShop_name(jsonObject.getString("shop_name"));
                        itemsobj.setAvg_rating((float) jsonObject.getDouble("Avg_rating")); // should be ratting
                        itemsobj.setItem_price((float) jsonObject.getDouble("item_price"));
                        itemsobj.setItem_descriptions(jsonObject.getString("item_descriptions"));
                        itemsobj.setItem_img(HomeActivity.BaseUrl+jsonObject.getString("item_img"));

//                        isItem.add(itemsobj);
                        if(type == 1){
                            if(jsonObject.getInt("item_type" )== 1){
                                isItem.add(itemsobj);
                            }
                        }
                        else if(type == 2){
                            if(jsonObject.getInt("item_type" )== 2){
                                isItem.add(itemsobj);
                            }
                        }



                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }

                setuprecyclerview(isItem);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });


        requestQueue.add(request);

    }
}

