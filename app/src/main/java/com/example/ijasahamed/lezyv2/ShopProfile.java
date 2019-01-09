package com.example.ijasahamed.lezyv2;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.ijasahamed.lezyv2.adapters.RecyclerViewAdapter;
import com.example.ijasahamed.lezyv2.models.items;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.android.volley.Request.Method.GET;

public class ShopProfile extends AppCompatActivity {

    private int userPhoneNumber,requestPhoneNumber ;

    private Button editBtn,userVisit,shopItems;
    private TextView shopeName,shopCatagory,shopContect,shopDetails;
    String getShopDetailsUrl,getItemsListUrl;
    int shopId;
    private ImageView shopImage;
    private RecyclerView recyclerView;
    private List<items> isItem;

    private RequestQueue requestQueue;
    private RequestOptions options;
    CollapsingToolbarLayout collapsingToolbarLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_profile);

        editBtn = findViewById(R.id.shop_edit_button);
        userVisit = findViewById(R.id.info_shop_visit);
        shopItems = findViewById(R.id.info_item_count);
        shopeName = findViewById(R.id.info_shop_name);
        shopCatagory = findViewById(R.id.info_shop_category);
        shopContect = findViewById(R.id.info_shop_contact_details);
        shopDetails = findViewById(R.id.info_shop_details);
        shopImage = findViewById(R.id.info_shop_thumbnail);
        recyclerView = findViewById(R.id.shop_items_recycler_view);

        isItem = new ArrayList<>();

        collapsingToolbarLayout = findViewById(R.id.collapsinglayout);
        collapsingToolbarLayout.setTitleEnabled(true);

        options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.loading_shape)
                .error(R.drawable.loading_shape);
        requestQueue = Volley.newRequestQueue(getApplicationContext());

        userPhoneNumber = 771013814;
        requestPhoneNumber = 771013814;
        shopId = 2;

        getShopDetailsUrl = HomeActivity.BaseUrl+"getShop.php?user_phone_number="+requestPhoneNumber;
//        getItemsListUrl = HomeActivity.BaseUrl+"getShopItems.php?user_phone_number="+requestPhoneNumber;
        getItemsListUrl = HomeActivity.BaseUrl+"getShopItems.php?shop_id="+shopId;


        requestShop();
        requestItem();

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shopIntent = new Intent(ShopProfile.this,ShopActivity.class);
                startActivity(shopIntent);
                finish();
            }
        });


    }

    private void requestShop() {

        JsonArrayRequest request;
        request = new JsonArrayRequest(GET, getShopDetailsUrl, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    JSONObject jsonObject = response.getJSONObject(0);

                    String shopNameStr =  jsonObject.getString("shop_name");
                    collapsingToolbarLayout.setTitle(shopNameStr);
                    shopeName.setText(shopNameStr);
                    shopCatagory.setText(jsonObject.getString("shop_category"));
                    shopContect.setText(jsonObject.getString("contact_details"));
                    userVisit.setText(String.valueOf(jsonObject.getInt("number_of_visits")));
                    shopItems.setText(String.valueOf(jsonObject.getInt("item_count")));
                    shopDetails.setText(jsonObject.getString("description"));
                    String userImageUrl = HomeActivity.BaseUrl+jsonObject.getString("shop_picture");

                    Glide.with(getApplicationContext()).load(userImageUrl).apply(options).into(shopImage);



                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(request);


    }

    private void requestItem(){

        JsonArrayRequest request2 = new JsonArrayRequest(GET,getItemsListUrl,null ,new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {

                JSONObject jsonObject = null;
                for(int i = 0; i < response.length(); i++){
                    try {
                        jsonObject = response.getJSONObject(i);
                        items itemsobj = new items();
                        itemsobj.setItem_id(jsonObject.getInt("item_id"));
                        itemsobj.setItem_name(jsonObject.getString("item_name"));
                        itemsobj.setShop_name(jsonObject.getString("shop_name")); // should be shop name
                        itemsobj.setAvg_rating((float) jsonObject.getDouble("Avg_rating")); // should be ratting
                        itemsobj.setItem_price((float) jsonObject.getDouble("item_price"));
                        itemsobj.setItem_descriptions(jsonObject.getString("item_descriptions"));
                        itemsobj.setItem_img(HomeActivity.BaseUrl+jsonObject.getString("item_img"));
                        isItem.add(itemsobj);


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


        requestQueue.add(request2);



    }

    public void setuprecyclerview(List<items> itemsInstance) {

        RecyclerViewAdapter myAdapter = new RecyclerViewAdapter(getApplicationContext(),itemsInstance);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(myAdapter);

    }

}
