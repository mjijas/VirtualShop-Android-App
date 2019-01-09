package com.example.ijasahamed.lezyv2;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class Scanner extends AppCompatActivity implements  ZXingScannerView.ResultHandler {

    private ZXingScannerView scannerView;
    private static final int REQUST_CAMERS_SCANNER = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);

        scannerView = new ZXingScannerView(this);
        setContentView(scannerView);


        if (ContextCompat.checkSelfPermission(Scanner.this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted

            ActivityCompat.requestPermissions(Scanner.this,
                    new String[]{Manifest.permission.CAMERA},
                    REQUST_CAMERS_SCANNER);


        }else {
//            Toast.makeText(this, "Permision Granted ", Toast.LENGTH_SHORT).show();
            scannerView.setResultHandler(this);
            scannerView.startCamera();
        }



//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
//            if(true){  //checkPermission()
//                Toast.makeText(this, "Permision Granted ", Toast.LENGTH_SHORT).show();
//                // here scanning starts
//                scannerView.setResultHandler(this);
//                scannerView.startCamera();
//
//
//            }else {
////                requestPermission();
//                requestPermissions(new String[]{CAMERA},REQUST_CAMERS_SCANNER);
////                ActivityCompat.requestPermissions(this,new String[]{CAMERA},REQUST_CAMERS_SCANNER);
//
//            }
//        }

    }

    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUST_CAMERS_SCANNER: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }


    // this is the QR scanner

    @Override
    public void handleResult(final Result result) {
        final String scanResult = result.getText();


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Scan Result");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                scannerView.resumeCameraPreview(Scanner.this);
            }
        });
        builder.setNeutralButton("Visit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse(scanResult));
                startActivity(intent);
            }
        });
        builder.setMessage(scanResult);
        AlertDialog alert = builder.create();
        alert.show();

    }

//    private boolean checkPermission(){
//        return (ContextCompat.checkSelfPermission(Scanner.this,Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED);
//    }
//    private void requestPermission(){
//        ActivityCompat.requestPermissions(this,new String[]{CAMERA},REQUST_CAMERS_SCANNER);
//    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
////        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        switch (requestCode){
//
//            case REQUST_CAMERS_SCANNER:
//                if(grantResults.length > 0){
//                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
//                    if(cameraAccepted){
//                        Toast.makeText(this, "permision Granted", Toast.LENGTH_SHORT).show();
//                    }else {
//                        Toast.makeText(this, "permision Denied", Toast.LENGTH_SHORT).show();
//                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
//                            if(shouldShowRequestPermissionRationale(CAMERA)){
//                                displayAlertMessage("You Need To Allow Permissions",
//                                        new DialogInterface.OnClickListener() {
//                                            @Override
//                                            public void onClick(DialogInterface dialog, int which) {
//                                                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
//                                                    requestPermissions(new String[]{CAMERA},REQUST_CAMERS_SCANNER);
//                                                }
//                                            }
//                                        });
//                                return;
//                            }
//                        }
//                    }
//                }
//                break;
//
//
//
//        }
//    }

    @Override
    public void onResume() {

        super.onResume();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(ContextCompat.checkSelfPermission(Scanner.this, Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED){ //checkPermission()
                if(scannerView == null){
                    scannerView = new ZXingScannerView(this);
                    setContentView(scannerView);
                }
                scannerView.setResultHandler(this);
                scannerView.startCamera();
            }else {

            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        scannerView.stopCamera();
    }

    public void displayAlertMessage(String Message, DialogInterface.OnClickListener Listener){
        new AlertDialog.Builder(Scanner.this)
                .setMessage(Message)
                .setPositiveButton("OK",Listener)
                .setNegativeButton("Cancel",null)
                .create()
                .show();
    }

}
