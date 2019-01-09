package com.example.ijasahamed.lezyv2;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.ijasahamed.lezyv2.adapters.RcRecyclerViewAdapter;
import com.example.ijasahamed.lezyv2.models.ratingsComments;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.android.volley.Request.Method.GET;

public class ItemInfoActivity extends AppCompatActivity implements DialogRateComments.RateCommentsListener {

    private RequestQueue requestQueue;
    private List<ratingsComments> RcList;
    private RecyclerView recyclerView;
    private JsonArrayRequest request;
    private String Url;


    private int item_id;
    private String item_name;
    private String item_img;
    private float item_price;
    private int item_availability;
    private int  item_type;
    private String  item_category;
    private int shop_id;
    private String shop_name;
    private String item_descriptions;
    private float Avg_rating;

    private int user_id;
    private String addRatingAndCommentsUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_info);

        RcList = new ArrayList<>();
        recyclerView = findViewById(R.id.comments_recycler_view);

        user_id = 1;
        addRatingAndCommentsUrl = HomeActivity.BaseUrl+"addCommentsItem.php";


        item_id = getIntent().getExtras().getInt("item_id");
        item_name = getIntent().getExtras().getString("item_name");
        item_img = getIntent().getExtras().getString("item_img");
        item_price = getIntent().getExtras().getFloat("item_price");
        item_availability = getIntent().getExtras().getInt("item_availability");
        item_type = getIntent().getExtras().getInt("item_type");
        item_category= getIntent().getExtras().getString("item_category");
        shop_id = getIntent().getExtras().getInt("shop_id");
        shop_name = getIntent().getExtras().getString("shop_name");
        item_descriptions = getIntent().getExtras().getString("item_descriptions");
        Avg_rating = getIntent().getExtras().getFloat("Avg_rating");

        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsinglayout);
        collapsingToolbarLayout.setTitleEnabled(true);


        TextView tv_itemName = findViewById(R.id.info_item_name);;
        TextView tv_shopName = findViewById(R.id.info_shop_name);
        TextView tv_price = findViewById(R.id.info_price);
        TextView tv_rating = findViewById(R.id.info_rating);
        ImageView iv_thumbnail =findViewById(R.id.info_thumbnail);
        TextView tv_itemDescription = findViewById(R.id.info_description);


        tv_itemName.setText(item_name);
        tv_shopName.setText(shop_name);
        tv_price.setText(Float.toString(item_price));
        tv_rating.setText(Float.toString(Avg_rating));
        tv_itemDescription.setText(item_descriptions);

        collapsingToolbarLayout.setTitle(item_name);

        RequestOptions requestOptions = new RequestOptions().centerCrop().placeholder(R.drawable.loading_shape).error(R.drawable.loading_shape);

        Glide.with(this).load(item_img).apply(requestOptions).into(iv_thumbnail);

        Url = HomeActivity.BaseUrl+"getComments.php?item_id=" + item_id;


        jsonObjectRequest();

        FloatingActionButton fab = findViewById(R.id.flottingbutton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               openRateCommentDialog();
            }
        });


    }

    public void openRateCommentDialog(){
        Bundle bundle = new Bundle();
        bundle.putString("itemName", item_name);
        bundle.putString("shopName", shop_name );
        bundle.putString("itemImage", item_img);

        DialogRateComments dialogRateComments = new DialogRateComments();
        dialogRateComments.setArguments(bundle);
        dialogRateComments.show(getSupportFragmentManager(),"rate comment dialog");
    }



    public void setuprecyclerview(List<ratingsComments> RcInstance) {
//        Toast.makeText(getContext(), "inside the function", Toast.LENGTH_SHORT).show();

        RcRecyclerViewAdapter myAdapter = new RcRecyclerViewAdapter(getApplicationContext(),RcInstance);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(myAdapter);

    }



    private void jsonObjectRequest(){

            requestQueue = Volley.newRequestQueue(getApplicationContext());
            request = new JsonArrayRequest(GET,Url,null ,new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

//                Toast.makeText(getContext(), response.toString(),Toast.LENGTH_SHORT ).show();
                JSONObject jsonObject = null;
                for(int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        ratingsComments rc = new ratingsComments();


                        rc.setRatings(jsonObject.getInt("ratings"));
                        rc.setComments(jsonObject.getString("comments"));
                        rc.setDate_time(jsonObject.getString("date_time"));
                        rc.setUser_name(jsonObject.getString("user_name"));

                        RcList.add(rc);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                setuprecyclerview(RcList);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });


        requestQueue.add(request);

    }

    @Override
    public void saveRatingsComments(final String rating, final String comment) {



        if(rating.equals("0") || comment.equals("")){
            Toast.makeText(this, "You Should Give Both Ratings and Comments", Toast.LENGTH_SHORT).show();

        }else {


            StringRequest stringRequest = new StringRequest(Request.Method.POST, addRatingAndCommentsUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    //success
                    jsonObjectRequest();

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(ItemInfoActivity.this, "There is an error", Toast.LENGTH_SHORT).show();
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> params = new HashMap<String, String>();
                    params.put("rating",rating);
                    params.put("comment",comment);
                    params.put("item_id",String.valueOf(item_id));
                    params.put("shop_id",String.valueOf(shop_id));
                    params.put("user_id",String.valueOf(user_id));

                    Calendar calendar;
                    SimpleDateFormat simpleDateFormat;
                    String DateAndTime;

                    calendar = Calendar.getInstance();
                    simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    DateAndTime = simpleDateFormat.format(calendar.getTime());

                    params.put("date_time",DateAndTime);

                    return params;
                }
            };

            MyUpdateSingleton.getInstance(ItemInfoActivity.this).addToRequestQue(stringRequest);


        }
    }
}
