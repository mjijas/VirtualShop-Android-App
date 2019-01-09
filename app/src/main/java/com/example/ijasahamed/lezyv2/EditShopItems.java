package com.example.ijasahamed.lezyv2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.ijasahamed.lezyv2.adapters.EditItemRecyclerAdapter;
import com.example.ijasahamed.lezyv2.models.items;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.android.volley.Request.Method.GET;

public class EditShopItems extends AppCompatActivity {

    private String getItemsListUrl;
    List<items> isItem;
    RequestQueue requestQueue;
    RecyclerView recyclerView;
    int shopID;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_shop_items);

        recyclerView = findViewById(R.id.shop_items_recycler_view);

        shopID = 2;
        getItemsListUrl = HomeActivity.BaseUrl+"getShopItems.php?shop_id="+shopID;
        isItem = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(getApplicationContext());

        toolbar = findViewById(R.id.item_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Goods & Services");


        requestItem();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.shop_item_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // tool bar menu item clicked listner

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.finished_btn) {
            Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
            Intent homeIntent = new Intent(EditShopItems.this,HomeActivity.class);
            startActivity(homeIntent);
            finish();
            return true;
        }

        if (id == R.id.add_item_btn) {
//here we have to send shop id
            Intent EditItem = new Intent(EditShopItems.this,EditItemDetails.class);
            startActivity(EditItem);

            return true;
        }

        return super.onOptionsItemSelected(item);
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
                        itemsobj.setItem_type(jsonObject.getInt("item_type"));
                        itemsobj.setItem_category(jsonObject.getString("item_category"));
                        itemsobj.setItem_availability(jsonObject.getInt("item_availability"));
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

        EditItemRecyclerAdapter myAdapter = new EditItemRecyclerAdapter(getApplicationContext(),itemsInstance);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(myAdapter);

    }
}
