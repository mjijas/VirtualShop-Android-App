package com.example.ijasahamed.lezyv2;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private Button logoutBtn,mapBtn,infoBtn;
    private ImageButton userBtn,sellerBtn;


    private static final String TAG = "MainActivity";
    private static final int ERROR_DIALOG_REQUEST = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // to change status bar colour
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.BLACK);
        }

        mAuth = FirebaseAuth.getInstance();


            logoutBtn = (Button)findViewById(R.id.login_button);
            userBtn = (ImageButton) findViewById(R.id.user_Button);
            sellerBtn = findViewById(R.id.seller_Button);
            mapBtn = (Button)findViewById(R.id.search_Button);
            infoBtn = (Button)findViewById(R.id.info_button);


        logoutBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mAuth.signOut();

                    Intent signIn = new Intent(MainActivity.this,signup.class);
                    startActivity(signIn);
                    finish();
                }
            });

            userBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent userDetails = new Intent(MainActivity.this,UserDetails.class);
                    startActivity(userDetails);

                }
            });

            sellerBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent shopIntent = new Intent(MainActivity.this,ShopActivity.class);
                    startActivity(shopIntent);
                }
            });

            infoBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent homeIntent = new Intent(MainActivity.this,HomeActivity.class);
                    startActivity(homeIntent);
                }
            });







// for map service 1- check all permisions 2.button click event to open a map activity
        if(isServicesOk()){
            init();
        }
    }

    private void init(){

        mapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mapIntent = new Intent(MainActivity.this,MapActivity.class);
                startActivity(mapIntent);
            }
        });
    }

    //to check whether play services inable or not
    public boolean isServicesOk(){
        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MainActivity.this);

        if(available == ConnectionResult.SUCCESS){
            //everything is fine now user can make mape request
            return true;

        }else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            // error occured but we can resolve it
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(MainActivity.this,available,ERROR_DIALOG_REQUEST);
            dialog.show();
        }else {
            Toast.makeText(this, "we can make map request", Toast.LENGTH_SHORT).show();
        }
        return false;
    }


    public void onStart() {

        super.onStart();

        FirebaseUser  currentUser = mAuth.getCurrentUser();

        if(currentUser == null){
            Intent authIntent = new Intent(MainActivity.this,singin.class);
            startActivity(authIntent);
            finish();
        }
    }


}
