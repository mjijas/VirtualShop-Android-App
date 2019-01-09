package com.example.ijasahamed.lezyv2;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditItemDetails extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener,AdapterView.OnItemSelectedListener{

    Spinner itemCategorySpinner;
    ArrayAdapter<CharSequence> spinnerAdapter;
    ImageButton itemTypeBtn,availabilityBtn;
    TextView availabilityText;
    Button saveBtn,deleteBtn;

    TextView editTextItemName;
    TextView editTextItemPrice;
    TextView editTextItemDescription;
    CircleImageView itemImageView;


    boolean itemType  = true;
    int availability = 1;
    private int itemId = 0;
    private String updateItemDetailsUrl,DeleteItemUrl;
    String itemCategory;
    int shopId = 0;


    Bitmap bitmap;
    boolean imageChanged = false;
    boolean itemCategoryChanged = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item_details);
        itemTypeBtn = findViewById(R.id.item_type_imgbtn);
        availabilityBtn = findViewById(R.id.availability_imgbtn);
        availabilityText = findViewById(R.id.availability_textview);

        editTextItemName = findViewById(R.id.item_name);
        editTextItemPrice = findViewById(R.id.item_price);
        editTextItemDescription = findViewById(R.id.item_description_editText);
        itemImageView = findViewById(R.id.itemPicture_CirImageView);

        saveBtn = findViewById(R.id.item_save_button);
        deleteBtn = findViewById(R.id.item_delete_button);
        itemCategorySpinner = findViewById(R.id.item_category);


        itemImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity()
                        .setAspectRatio(1,1)
                        .setCropShape(CropImageView.CropShape.OVAL)
                        .start(EditItemDetails.this);
            }
        });


        itemTypeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeItemType();
            }
        });

        availabilityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeItemAvailability();
            }
        });


        setSpinnerResource();
        settingData();
        shopId = 2;

//        updateItemDetailsUrl = HomeActivity.BaseUrl+"updateItemDetails.php";
//        DeleteItemUrl = HomeActivity.BaseUrl+"deleteItem.php" ;

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(itemId == 0){

                    TextView nameLabel = findViewById(R.id.item_name_label);
                    TextView priceLabel = findViewById(R.id.item_price_label);

                    //add items
                        if(editTextItemName.getText().toString().equals("")){
                                Toast.makeText(EditItemDetails.this, "Please Give a Name for Your Product", Toast.LENGTH_SHORT).show();
                                nameLabel.setTextColor(Color.RED);
                                editTextItemName.requestFocus();

                            }else {

                                nameLabel.setTextColor(Color.WHITE);

                                if (editTextItemPrice.getText().toString().equals("")){
                                    Toast.makeText(EditItemDetails.this, "Please Give a Price for Your Product", Toast.LENGTH_SHORT).show();
                                    priceLabel.setTextColor(Color.RED);
                                    editTextItemPrice.requestFocus();
                                }else {
                                      // name and price are given
                                       priceLabel.setTextColor(Color.WHITE);
                                       updateItemDetails();
                                }
                            }
                }else {
                    //Toast.makeText(EditItemDetails.this, "item id - "+itemId, Toast.LENGTH_SHORT).show();
                    updateItemDetails();

                }


//                updateItemDetails();


            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


//                AlertDialog alertDialog = new AlertDialog.Builder(getApplicationContext())
//                        //set icon
//                        .setIcon(android.R.drawable.ic_dialog_alert)
//                        //set title
//                        .setTitle("Are you sure to Exit")
//                        //set message
//                        .setMessage("Exiting will call finish() method")
//                        //set positive button
//                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                //set what would happen when positive button is clicked
//                                finish();
//                            }
//                        })
//                        //set negative button
//                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                //set what should happen when negative button is clicked
//                                Toast.makeText(getApplicationContext(),"Nothing Happened",Toast.LENGTH_LONG).show();
//                            }
//                        })
//                        .show();
//



//                alert box for asking conformation
                AlertDialog.Builder builder = new AlertDialog.Builder(EditItemDetails.this);
//                builder.setTitle("Delete");
                builder.setMessage("Are You Sure, Do You Want To delete?");

                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteItem();
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.setTitle("Delete");
                dialog.setIcon(android.R.drawable.ic_dialog_alert);
                dialog.show();
            }
        });


    }

    public void setSpinnerResource(){

        if(itemType){
            spinnerAdapter = ArrayAdapter.createFromResource(this,R.array.itemsCategoryList,android.R.layout.simple_spinner_item);

        }else {
            spinnerAdapter = ArrayAdapter.createFromResource(this,R.array.serviceCategoryList,android.R.layout.simple_spinner_item);

        }
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        itemCategorySpinner.setAdapter(spinnerAdapter);
        itemCategorySpinner.setOnItemSelectedListener(this);
    }


    public void updateItemDetails(){


        StringRequest stringRequest = new StringRequest(Request.Method.POST, updateItemDetailsUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                //success
                Intent DetailsNext = new Intent(EditItemDetails.this,EditShopItems.class);
                startActivity(DetailsNext);
                finish();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(EditItemDetails.this, "There is an error", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("item_name",editTextItemName.getText().toString());
                params.put("item_price",editTextItemPrice.getText().toString());
                params.put("item_description",editTextItemDescription.getText().toString());

                params.put("item_id",String.valueOf(itemId));

                if(itemId == 0){
                    params.put("forg_shop_id",String.valueOf(shopId));
                }else

                //item category

                if (itemCategoryChanged){
                    if(!itemCategory.equals("Select Your Service Category")){
                        params.put("item_category",itemCategory);
                    }
                }

                //item type
                int itemTypeInt;
                if(itemType){
                    itemTypeInt = 1;
                }else {
                    itemTypeInt = 2;
                }

                params.put("item_type",String.valueOf(itemTypeInt));

                //item availability
                params.put("item_availability",String.valueOf(availability));

                if (imageChanged){
                    //item image
                    params.put("item_image",ImageToString(bitmap));
                }
                return params;
            }
        };

        MyUpdateSingleton.getInstance(EditItemDetails.this).addToRequestQue(stringRequest);


    }

    public void deleteItem(){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, DeleteItemUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                //success
                Intent DetailsNext = new Intent(EditItemDetails.this,EditShopItems.class);
                startActivity(DetailsNext);
                finish();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(EditItemDetails.this, "There is an error", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();

                params.put("item_id",String.valueOf(itemId));
                return params;
            }
        };

        MyUpdateSingleton.getInstance(EditItemDetails.this).addToRequestQue(stringRequest);


    }





    public void changeItemType(){

        Bundle extras = getIntent().getExtras();

        if (itemType) {
                itemTypeBtn.setImageResource(R.drawable.services_switch);

                if(extras == null){
                    itemImageView.setImageResource(R.drawable.service_icon);

                }else {

                    if(getIntent().getExtras().getString("item_img").equals("")){
                        itemImageView.setImageResource(R.drawable.service_icon);
                    }
                }


                itemType = false;
                setSpinnerResource();

            } else {
                itemTypeBtn.setImageResource(R.drawable.goods_switch);
                    if(extras == null){
                        itemImageView.setImageResource(R.drawable.item_icon);


                    }else {

                        if(getIntent().getExtras().getString("item_img").equals("")){
                            itemImageView.setImageResource(R.drawable.item_icon);

                        }
                    }


                itemType = true;
                setSpinnerResource();

            }

    }

    public void changeItemAvailability(){

        if(availability == 1){

            availabilityBtn.setImageResource(R.drawable.not_sure);
            availabilityText.setText("Not Sure");
            availability = 2;

        }else if(availability == 2){

            availabilityBtn.setImageResource(R.drawable.not_available);
            availabilityText.setText("Not Available");
            availability = 3;
        }else if(availability == 3){

            availabilityBtn.setImageResource(R.drawable.available);
            availabilityText.setText("Available");
            availability = 1;
        }
    }

    public void settingData(){

        Bundle extras = getIntent().getExtras();

        if(extras != null){

            itemId = getIntent().getExtras().getInt("item_id");
            String item_name = getIntent().getExtras().getString("item_name");
            String item_img = getIntent().getExtras().getString("item_img");
            float item_price = getIntent().getExtras().getFloat("item_price");
            int item_availability = getIntent().getExtras().getInt("item_availability");
            int item_type = getIntent().getExtras().getInt("item_type");
            String item_category =  getIntent().getExtras().getString("item_category");
//        String shop_name = getIntent().getExtras().getString("shop_name");
            String item_descriptions = getIntent().getExtras().getString("item_descriptions");
//        float Avg_rating = getIntent().getExtras().getFloat("Avg_rating");


            editTextItemName.setText(item_name);
            editTextItemPrice.setText(String.valueOf(item_price));
            editTextItemDescription.setText(item_descriptions);

            RequestOptions requestOptions = new RequestOptions().centerCrop().placeholder(R.drawable.loading_shape).error(R.drawable.loading_shape);


            if((item_img.equals(HomeActivity.BaseUrl))){

                if(item_type == 1){
                    itemImageView.setImageResource(R.drawable.item_icon);
                }else if(item_type == 2){
                    itemImageView.setImageResource(R.drawable.service_icon);
                }

            }else {
                Glide.with(this).load(item_img).apply(requestOptions).into(itemImageView);


            }


            if (item_type == 1) {
                itemType = false;
            } else if (item_type == 2) {
                itemType = true;
            }

            changeItemType();

            if (item_availability == 1) {
                availability = 3;
            } else if (item_availability == 2) {
                availability = 1;
            } else if (item_availability == 3) {
                availability = 2;
            }
            changeItemAvailability();


            itemCategorySpinner.setSelection(spinnerAdapter.getPosition(String.valueOf(item_category)));



            // to determoine what are actions can perform lile update and delete or insert only
            setActions("update");

        }else {

            // to determoine what are actions can perform lile update and delete or insert only
            setActions("insert");

        }






    }

    public void setActions(String actions){

        if(actions.equals("insert")){
            // insert only
            deleteBtn.setVisibility(View.GONE);
            saveBtn.setText("Add Item ");
            updateItemDetailsUrl = HomeActivity.BaseUrl+"addItem.php";


        }else if(actions.equals("update")){
            //update and delete
            updateItemDetailsUrl = HomeActivity.BaseUrl+"updateItemDetails.php";
            DeleteItemUrl = HomeActivity.BaseUrl+"deleteItem.php" ;

        }


    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        itemCategory = parent.getItemAtPosition(position).toString();
        itemCategoryChanged = true;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    //for image cropping and set the image
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                //getting Image to show
                Uri resultUri = result.getUri();
                itemImageView.setImageURI(resultUri);

                //here we convert image into bitmap format
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

    private String ImageToString(Bitmap bitmap){

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100,byteArrayOutputStream);
        byte[] imageByte = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imageByte,Base64.DEFAULT);
    }



}
