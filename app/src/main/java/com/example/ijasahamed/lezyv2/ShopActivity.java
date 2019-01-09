package com.example.ijasahamed.lezyv2;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
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
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.android.volley.Request.Method.GET;

public class ShopActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private CircleImageView shopPicture;
    private TextView shopName,shopDescription,shopContacts;
    private Button locationBtn,nextBtn;
    private Spinner shopCategorySpinner;

    private String getShopDetailsUrl,updateShopDetailsUrl;
    private int userPhoneNumber = 771013814;
    private RequestQueue requestQueue;
    private RequestOptions options;

    ArrayAdapter<CharSequence> spinnerAdapter;
    LocationManager locationManager;
    Double latitude;
    Double longtitude;
    private Geocoder geocoder;
    private List<Address> addresses;
    private String address;
    private boolean locationButtonClicked = false;
    private static final int REQUEST_LOCATION = 1;

    private Bitmap bitmap;
    String shopCategoryStr;
    private boolean shopCategoryChanged = false;
    private  boolean imageChanged = false;

    private int shopID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        shopName = findViewById(R.id.shop_name_editText);
        shopDescription = findViewById(R.id.shop_description_editText);
        shopContacts = findViewById(R.id.shop_contact_editText);
        locationBtn = findViewById(R.id.shop_location_button);
        nextBtn = findViewById(R.id.shop_edit_next_Button);
        shopCategorySpinner = findViewById(R.id.edit_shop_category);

        shopID = 2;

        getShopDetailsUrl = HomeActivity.BaseUrl+"getShopDetails.php?phone_number="+userPhoneNumber;
        updateShopDetailsUrl = HomeActivity.BaseUrl+"updateShopDetails.php?shop_id="+shopID;


        requestQueue = Volley.newRequestQueue(getApplicationContext());
        options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.loading_shape)
                .error(R.drawable.loading_shape);



        spinnerAdapter = ArrayAdapter.createFromResource(this,R.array.shopCategoryList,android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        shopCategorySpinner.setAdapter(spinnerAdapter);
        shopCategorySpinner.setOnItemSelectedListener(this);


        shopPicture = (CircleImageView)findViewById(R.id.shopPicture_CirImageView);
        shopPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity()
                        .setAspectRatio(1,1)
                        .setCropShape(CropImageView.CropShape.OVAL)
                        .start(ShopActivity.this);
            }
        });

        locationBtn.setOnClickListener(new View.OnClickListener() {
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


        showDetails();

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDetails();
            }
        });






    }

    private String ImageToString(Bitmap bitmap){

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100,byteArrayOutputStream);
        byte[] imageByte = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imageByte,Base64.DEFAULT);
    }

    private void updateDetails(){


        StringRequest stringRequest = new StringRequest(Request.Method.POST, updateShopDetailsUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Toast.makeText(ShopActivity.this, "Details Saved", Toast.LENGTH_SHORT).show();
                //success
//                Intent DetailsNext = new Intent(UserDetails.this,UserDetails2.class);
//                startActivity(DetailsNext);
//                finish();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ShopActivity.this, "There is an error", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("shop_name",shopName.getText().toString());
                params.put("shop_description",shopDescription.getText().toString());
                params.put("shop_contacts",shopContacts.getText().toString());
                params.put("shop_id",String.valueOf(shopID));

                if(locationButtonClicked){
                    params.put("latitude",String.valueOf(latitude));
                    params.put("longitude",String.valueOf(longtitude));
                    params.put("location_details",address);
                }

                if(shopCategoryChanged){
                    params.put("shop_category",shopCategoryStr);
                }

                if (imageChanged){
                    //user image
                    params.put("shop_image",ImageToString(bitmap));
                }

                return params;
            }
        };

        MyUpdateSingleton.getInstance(ShopActivity.this).addToRequestQue(stringRequest);




    }

    private void showDetails() {


        final JsonArrayRequest jsonArrayRequest;

        jsonArrayRequest = new JsonArrayRequest(GET,getShopDetailsUrl,null, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {

                JSONObject jsonObject = null;

                try {

                    jsonObject = response.getJSONObject(0);
                    shopName.setText(jsonObject.getString("shop_name"));
                    shopCategorySpinner.setSelection(spinnerAdapter.getPosition(String.valueOf(jsonObject.getString("shop_category"))));
                    shopDescription.setText(jsonObject.getString("description"));
                    shopContacts.setText(jsonObject.getString("contact_details"));
                    locationBtn.setText(jsonObject.getString("location_details"));
                    String shopImageUrl = HomeActivity.BaseUrl+jsonObject.getInt("shop_picture");

                    Glide.with(getApplicationContext()).load(shopImageUrl).apply(options).into(shopPicture);




                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        },new Response.ErrorListener(){


            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        requestQueue.add(jsonArrayRequest);
    }


    //for image cropping and set the image
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                shopPicture.setImageURI(resultUri);

//                //getting image to save bitmap formate
                try{
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),resultUri);
                    imageChanged = true;
                }catch (IOException e){
                    e.printStackTrace();
                }

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(this, ""+error, Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        shopCategoryStr = parent.getItemAtPosition(position).toString();
        shopCategoryChanged = true;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    // To get Location Details

    private void getAddress(){
        //to get addresss
        geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        try{
            addresses = geocoder.getFromLocation(latitude,longtitude,1);

            address = addresses.get(0).getAddressLine(0);
            locationBtn.setText(address);
            Toast.makeText(this, "Location Has Set", Toast.LENGTH_SHORT).show();
            locationButtonClicked = true;

        }catch (IOException e){
            e.printStackTrace();
        }
    }


    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(ShopActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (ShopActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(ShopActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

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

            if( latitude != null && longtitude != null ){
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
