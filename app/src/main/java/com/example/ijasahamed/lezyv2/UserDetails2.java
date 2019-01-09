package com.example.ijasahamed.lezyv2;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.android.volley.Request.Method.GET;

public class UserDetails2 extends AppCompatActivity {

    private Button getLocationBtn;
    private Button finishButton;
    private EditText detailsEditText;

    Geocoder geocoder;
    List<Address> addresses;
    Double latitude;
    Double longtitude;
    boolean locationButtonClicked = false;private
    RequestQueue requestQueue;
    String address;


    private static final int REQUEST_LOCATION = 2;
    LocationManager locationManager;

    int user_phone = 771013814;
    String UpdateUrl = HomeActivity.BaseUrl+"updateUserDetails2.php";
    String getUrl = HomeActivity.BaseUrl+"getUser2.php?user_phone="+user_phone;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details2);

        finishButton = findViewById(R.id.Finish_Button);
        getLocationBtn = findViewById(R.id.get_location_button);
        detailsEditText = findViewById(R.id.user_details_edittext);

        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        showDetails();


        getLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    buildAlertMessageNoGps();

                } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    getLocation();
                }
            }


        });

        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDetails();
            }
        });

    }

    private void showDetails(){


        requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(GET, getUrl, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                JSONObject jsonObject = null;

                try {
                    jsonObject = response.getJSONObject(0);
                    detailsEditText.setText(jsonObject.getString("user_details"));
                    latitude = jsonObject.getDouble("latitude");
                    longtitude = jsonObject.getDouble("longitude");
                    getLocationBtn.setText(jsonObject.getString("location_details"));

                }catch (JSONException e){
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        requestQueue.add(jsonArrayRequest);


    }

    private void updateDetails() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, UpdateUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Toast.makeText(UserDetails2.this, "Details Saved Successfully", Toast.LENGTH_SHORT).show();
                //success
                Intent HomeIntent = new Intent(UserDetails2.this,HomeActivity.class);
                startActivity(HomeIntent);
                finish();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(UserDetails2.this, "There is an error", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("user_details",detailsEditText.getText().toString());

                if(locationButtonClicked){
                    params.put("latitude",String.valueOf(latitude));
                    params.put("longitude",String.valueOf(longtitude));
                    params.put("location_details",address);
                }

                params.put("user_phone",String.valueOf(user_phone));
                return params;
            }
        };

        MyUpdateSingleton.getInstance(UserDetails2.this).addToRequestQue(stringRequest);
    }


    private void getAddress(){
        //to get addresss
        geocoder = new Geocoder(this, Locale.getDefault());
        try{
            addresses = geocoder.getFromLocation(latitude,longtitude,1);

            address = addresses.get(0).getAddressLine(0);
            getLocationBtn.setText(address);
            Toast.makeText(this, "Location Has Set", Toast.LENGTH_SHORT).show();
            locationButtonClicked = true;

        }catch (IOException e){
            e.printStackTrace();
        }
    }


    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(UserDetails2.this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(UserDetails2.this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(UserDetails2.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        } else {
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            Location location1 = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            Location location2 = locationManager.getLastKnownLocation(LocationManager. PASSIVE_PROVIDER);

            if (location != null) {
                latitude = location.getLatitude();
                longtitude = location.getLongitude();



            } else  if (location1 != null) {
                latitude = location1.getLatitude();
                longtitude = location1.getLongitude();



            } else  if (location2 != null) {
                latitude= location2.getLatitude();
                longtitude = location2.getLongitude();


            }else{

                Toast.makeText(this,"Unble to Trace your location",Toast.LENGTH_SHORT).show();

            }

                if(latitude != null && longtitude != null ){
                    getAddress();
                }
        }
    }


    protected void buildAlertMessageNoGps() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Please Turn ON your GPS Connection")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }




}
