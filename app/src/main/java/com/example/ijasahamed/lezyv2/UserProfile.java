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
import com.example.ijasahamed.lezyv2.adapters.RcRecyclerViewAdapter;
import com.example.ijasahamed.lezyv2.models.ratingsComments;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.android.volley.Request.Method.GET;

public class UserProfile extends AppCompatActivity {

    private int userPhoneNo,requestUserPhoneNumber;

    private Button editeBtn;
    private TextView userName,userVisit,userDetails;
    private ImageView userImage;
    private String userImageUrl;

    private RequestQueue requestQueue;
    private String userRequestUrl,commentsRequestUrl;
    private RecyclerView commentsRecyclerView;
    private List<ratingsComments> CommentsList;
    private RequestOptions options;
    CollapsingToolbarLayout collapsingToolbarLayout;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        editeBtn = findViewById(R.id.edit_button);
        userName = findViewById(R.id.info_user_name);
        userVisit = findViewById(R.id.info_user_visit);
        userDetails = findViewById(R.id.info_user_details);
        userImage = findViewById(R.id.info_user_thumbnail);
        commentsRecyclerView = findViewById(R.id.comments_recycler_view);

        CommentsList = new ArrayList<>();
        collapsingToolbarLayout = findViewById(R.id.collapsinglayout);
        collapsingToolbarLayout.setTitleEnabled(true);



        userPhoneNo = 771013814;
        requestUserPhoneNumber = 771013814;

        userRequestUrl = HomeActivity.BaseUrl+"getUser.php?userPhoneNo=" + requestUserPhoneNumber;

        commentsRequestUrl = HomeActivity.BaseUrl+"userComments.php?userPhoneNo="+ requestUserPhoneNumber;

        requestQueue = Volley.newRequestQueue(getApplicationContext());

        if(userPhoneNo == requestUserPhoneNumber){
            editeBtn.setVisibility(View.VISIBLE);
        }else {
            editeBtn.setVisibility(View.GONE);
        }

        options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.loading_shape)
                .error(R.drawable.loading_shape);



           requestUser();
           requestComments();


           editeBtn.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   Intent editUserIntent = new Intent(UserProfile.this,UserDetails.class);
                   startActivity(editUserIntent);
                   finish();
               }
           });


    }

    private void requestUser() {
        JsonArrayRequest request;
         request = new JsonArrayRequest(GET, userRequestUrl, null, new Response.Listener<JSONArray>() {
                 @Override
             public void onResponse(JSONArray response) {
                        try {
                            JSONObject jsonObject = response.getJSONObject(0);

                            String Name =  jsonObject.getString("user_name");
                            collapsingToolbarLayout.setTitle(Name);
                            userName.setText(Name);
                            userVisit.setText(String.valueOf(jsonObject.getInt("number_of_visits")));
                            userDetails.setText(jsonObject.getString("user_details"));
                            userImageUrl = HomeActivity.BaseUrl+jsonObject.getString("user_image");

                            Glide.with(getApplicationContext()).load(userImageUrl).apply(options).into(userImage);



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



    private void requestComments() {

        JsonArrayRequest request;
        request = new JsonArrayRequest(GET,commentsRequestUrl,null ,new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                JSONObject jsonObject = null;
                for(int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        ratingsComments rc = new ratingsComments();


                        rc.setRatings(jsonObject.getInt("ratings"));
                        rc.setComments(jsonObject.getString("comments"));
                        rc.setDate_time(jsonObject.getString("date_time"));
                        rc.setUser_name(jsonObject.getString("item_name"));

                        CommentsList.add(rc);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                setuprecyclerview(CommentsList);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });


        requestQueue.add(request);



    }

    public void setuprecyclerview(List<ratingsComments> RcInstance) {

        RcRecyclerViewAdapter myAdapter = new RcRecyclerViewAdapter(getApplicationContext(),RcInstance);
        commentsRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        commentsRecyclerView.setAdapter(myAdapter);

    }


}
