package com.example.ijasahamed.lezyv2;

import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class Auth extends AppCompatActivity implements login.OnFragmentInteractionListener, register.OnFragmentInteractionListener{

    TabLayout tabLayout;
    ViewPager viewPager;
    PagerAdopter pagerAdopter;
    Button registerBtn,loginBtn;
    EditText phoneNumber;

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks tCallBacks;

    Button register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        tabLayout = (TabLayout)findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText("LOGIN"));
        tabLayout.addTab(tabLayout.newTab().setText("REGISTER"));

        tabLayout.setTabGravity(tabLayout.GRAVITY_FILL);

        viewPager = (ViewPager)findViewById(R.id.pager);
        pagerAdopter = new PagerAdopter(getSupportFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdopter);
        viewPager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        register = (Button)findViewById(R.id.registerButton);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        register = (Button)findViewById(R.id.registerButton);
        loginBtn = (Button)findViewById(R.id.login_button);
        phoneNumber = (EditText)findViewById(R.id.register_phoneNo_edittext);

        register.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                phoneNumber.setEnabled(false);
                register.setEnabled(false);
                String pNumber = phoneNumber.getText().toString();

                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        pNumber,
                        60,
                        TimeUnit.SECONDS,
                        Auth.this,
                        tCallBacks

                );
            }
        });

        tCallBacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {

            }
        };

    }



    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
