package com.example.ijasahamed.lezyv2;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class signup extends AppCompatActivity {

    private Button goLogin;

    private Button registerBtn;
    private EditText phoneNumber,verificationCode;

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBack;
    private FirebaseAuth mAuth;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private boolean buttonType = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //all decelerations come here
        goLogin = (Button)findViewById(R.id.goLoginButton);

        registerBtn = (Button)findViewById(R.id.registerButton);
        phoneNumber = (EditText)findViewById(R.id.register_phoneNo_edittext);
        verificationCode = (EditText)findViewById(R.id.verification_code_edittext);

        mAuth = FirebaseAuth.getInstance();

        // to change status bar colour
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.BLACK);
        }

        // on click listener for direct to signin activity
        goLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent login = new Intent(signup.this,singin.class);
                startActivity(login);
                finish();
            }
        });


        registerBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (!buttonType){
                    registerBtn.setEnabled(false);
                    phoneNumber.setEnabled(false);
                    String number =  phoneNumber.getText().toString();

                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
                            number,
                            60,
                            TimeUnit.SECONDS,
                            signup.this,
                            mCallBack
                    );
                }else {

                    registerBtn.setEnabled(false);
                    String vCode = verificationCode.getText().toString();
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, vCode);
                    signInWithPhoneAuthCredential(credential);
                }

            }
        });

        mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {

                Toast.makeText(signup.this, "There is an error", Toast.LENGTH_SHORT).show();

            }

            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
//                Log.d(TAG, "onCodeSent:" + verificationId);

                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;

                buttonType = true;

                verificationCode.setVisibility(View.VISIBLE);
                registerBtn.setText("Verify Me");
                registerBtn.setEnabled(true);
                // ...
            }

        };



    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
//                            Log.d(TAG, "signInWithCredential:success");

                            FirebaseUser user = task.getResult().getUser();

                            Intent mainActivity = new Intent(signup.this,HomeActivity.class);
                            startActivity(mainActivity);
                            finish();
                            // ...
                        } else {
                            // Sign in failed, display a message and update the UI
//                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(signup.this, "There is an error", Toast.LENGTH_SHORT).show();

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                            }
                        }
                    }
                });
    }
}
