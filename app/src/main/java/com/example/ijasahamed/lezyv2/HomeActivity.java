package com.example.ijasahamed.lezyv2;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.ijasahamed.lezyv2.models.items;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class HomeActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener , NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout drawer;
    private Spinner spinner;
    private final String Urlgetitems = "";
    private JsonArrayRequest request;
    private RequestQueue requestQueue;
    private List<items> item;
    private RecyclerView recyclerView;

    private static final int ERROR_DIALOG_REQUEST = 9001;

//    public static final String BaseUrl = "http://10.0.2.2:8080/Virtual_shop/";
//    public static final String BaseUrl = "http://192.168.1.100:8080/Virtual_shop/";
      public static final String BaseUrl = "http://35.184.42.40/";

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();


        drawer = findViewById(R.id.nav_drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        spinner = findViewById(R.id.typeSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.productType,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);



        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,toolbar,
                R.string.navigation_drawer_open,R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        openFragment(1);



//        android.support.v4.app.FragmentManager fragmentManager=getSupportFragmentManager();
//        android.support.v4.app.Fragment fragment=fragmentManager.findFragmentById(R.id.fragment_container);
//        if(fragment==null){
//            fragment=new FragmentItem();
//            android.support.v4.app.FragmentTransaction transaction=fragmentManager.beginTransaction();
//            transaction.add(R.id.fragment_container,fragment).commit();
//        }

    }

    public void openFragment(int type){
        Log.i("test", "openFragment: "+type);
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        FragmentItem fragmentObj = new FragmentItem();
        fragmentObj.setArguments(bundle);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                fragmentObj).commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_profile:
                Intent userProfileIntent = new Intent(HomeActivity.this,UserProfile.class);
                startActivity(userProfileIntent);
                break;
            case R.id.nav_shop:
                Intent shopProfile = new Intent(HomeActivity.this,ShopProfile.class);
                startActivity(shopProfile);
                break;
            case R.id.nav_shop_items:
                Intent shopItems = new Intent(HomeActivity.this,EditShopItems.class);
                startActivity(shopItems);
                break;

            case R.id.nav_search:
                if(isServicesOk()){
                    Intent mapIntent = new Intent(HomeActivity.this,MapActivity.class);
                    startActivity(mapIntent);
                }
                break;
            case R.id.nav_scanner:
                Intent scanner = new Intent(HomeActivity.this,Scanner.class);
                startActivity(scanner);
                break;

            case R.id.nav_sign_out:
                mAuth.signOut();
                Intent signIn = new Intent(HomeActivity.this,signup.class);
                startActivity(signIn);
                finish();


        }





            return false;
    }

    // scanner button clicked
    private void qrAndBarcodeScanner() {
//        IntentIntegrator integrator =  new IntentIntegrator(HomeActivity.this);
//        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
//        integrator.setPrompt("scan");
//        integrator.setCameraId(0);
//        integrator.setBeepEnabled(true);
//        integrator.setBarcodeImageEnabled(false);
//        integrator.initiateScan();






    }




//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//
//        IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
//        if(result != null){
//            if(result.getContents() == null){
//                Toast.makeText(this, "You Cancelled the Scanning", Toast.LENGTH_SHORT).show();
//            }else {
//                Toast.makeText(this, result.getContents(), Toast.LENGTH_SHORT).show();
//            }
//        }else {
//            super.onActivityResult(requestCode, resultCode, data);
//
//        }
//    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String typeString = parent.getItemAtPosition(position).toString();
        Log.i("string", "onItemSelected: "+typeString);
        if(typeString.equals("Goods")){
            openFragment(1);

        }else if(typeString.equals("Services")){
            openFragment(2);
        }
        Toast.makeText(parent.getContext(), typeString, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public boolean isServicesOk(){
        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(HomeActivity.this);

        if(available == ConnectionResult.SUCCESS){
            //everything is fine now user can make mape request
            return true;

        }else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            // error occured but we can resolve it
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(HomeActivity.this,available,ERROR_DIALOG_REQUEST);
            dialog.show();
        }else {
            Toast.makeText(this, "we can make map request", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    public void onStart() {

        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser == null){
            Intent authIntent = new Intent(HomeActivity.this,signup.class);
            startActivity(authIntent);
            finish();
        }
    }




}
