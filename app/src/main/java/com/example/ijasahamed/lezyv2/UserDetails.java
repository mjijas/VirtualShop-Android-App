package com.example.ijasahamed.lezyv2;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.android.volley.Request.Method.GET;

public class UserDetails extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    //for date picker
    private Button datePicker,nextBtn;
    private TextView showDate;
    private CircleImageView profilePic;
    private EditText name,password,confirmPassword,email;
    private TextView dob;

    private int requestUserPhoneNumber = 771013814;
    RequestQueue requestQueue;
    private RequestOptions options;

    String getUserUrl = HomeActivity.BaseUrl+"getUser.php?userPhoneNo=" + requestUserPhoneNumber;
    String UpdateUrl = HomeActivity.BaseUrl+"updateUserDetails.php";
    int user_phone = 771013814;
    Bitmap bitmap;
    boolean imageChanged = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);


        name = findViewById(R.id.name_edittext);
        password = findViewById(R.id.pass_edittext);
        confirmPassword = findViewById(R.id.confPass_edittext);
        email = findViewById(R.id.user_email_edittext);
        nextBtn = findViewById(R.id.Next_Button);
        dob = findViewById(R.id.dateShowTextView);


        //for date picker
        datePicker = (Button)findViewById(R.id.datePickerButton);
        showDate = (TextView)findViewById(R.id.dateShowTextView);

        options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.loading_shape)
                .error(R.drawable.loading_shape);

        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.support.v4.app.DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(),"Date Picker");
            }
        });

        //for profile picture
        profilePic = (CircleImageView)findViewById(R.id.profilePic_CirImageView);

        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity()
                        .setAspectRatio(1,1)
                        .setCropShape(CropImageView.CropShape.OVAL)
                        .start(UserDetails.this);
            }
        });

        //for volly request
        requestQueue = Volley.newRequestQueue(getApplicationContext());

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

    public void updateDetails(){

        if(!password.getText().toString().equals(confirmPassword.getText().toString()) ){
            Toast.makeText(this, "Passwords are not matching" , Toast.LENGTH_SHORT).show();


            // Create a border programmatically
            ShapeDrawable shape = new ShapeDrawable(new RectShape());
            shape.getPaint().setColor(Color.YELLOW);
            shape.getPaint().setStrokeWidth(2);

            // Assign the created border to EditText widget
            password.setBackground(shape);
            confirmPassword.setBackground(shape);
            password.requestFocus();

        }else {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, UpdateUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    //success
                    Intent DetailsNext = new Intent(UserDetails.this,UserDetails2.class);
                    startActivity(DetailsNext);
                    finish();

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(UserDetails.this, "There is an error", Toast.LENGTH_SHORT).show();
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> params = new HashMap<String, String>();
                    params.put("user_name",name.getText().toString());
                    params.put("user_dob",showDate.getText().toString());
                    params.put("user_pwd",password.getText().toString());
                    params.put("user_email",email.getText().toString());

                    params.put("user_phone",String.valueOf(user_phone));
                    if (imageChanged){
                        //user image
                        params.put("user_image",ImageToString(bitmap));
                    }

                    return params;
                }
            };

            MyUpdateSingleton.getInstance(UserDetails.this).addToRequestQue(stringRequest);
        }

    }


    public void showDetails(){
            JsonArrayRequest jsonArrayRequest;

            jsonArrayRequest = new JsonArrayRequest(GET,getUserUrl,null, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {

                JSONObject jsonObject = null;

                    try {

                        jsonObject = response.getJSONObject(0);
                        String nameStr = jsonObject.getString("user_name");
                        String dobStr = jsonObject.getString("dob");
                        String passwordStr = jsonObject.getString("password");
                        String emailStr = jsonObject.getString("user_email");
                        String userImageUrl = HomeActivity.BaseUrl +jsonObject.getString("user_image");

                        name.setText(nameStr);
                        dob.setText(dobStr);
                        password.setText(passwordStr);
                        confirmPassword.setText(passwordStr);
                        email.setText(emailStr);

                        Glide.with(getApplicationContext()).load(userImageUrl).apply(options).into(profilePic);


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

    //for set the calender date
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR,year);
        c.set(Calendar.MONTH,month);
        c.set(Calendar.DAY_OF_MONTH,dayOfMonth);

        String dateString = new SimpleDateFormat("YYYY-MM-d").format(c.getTime());
//        String dateString = DateFormat.getDateInstance(DateFormat.LONG).format(c.getTime());
        showDate.setText(dateString);

//        Toast.makeText(this, , Toast.LENGTH_SHORT).show();
    }


    //for image cropping and set the image
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                //getting Image to show
                Uri resultUri = result.getUri();
                profilePic.setImageURI(resultUri);

                //getting image to save bitmap formate
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


















}
