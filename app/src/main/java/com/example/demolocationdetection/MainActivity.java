package com.example.demolocationdetection;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;

import android.Manifest;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class MainActivity extends AppCompatActivity {

    // What should be here?
    Button btnGetLastLocation, btnGetLocationUpdate, btnRemoveLocationUpdate;
    LocationRequest mLocationRequest;
    LocationCallback mLocationCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(this);

        //==================UI components handlers===================
        btnGetLastLocation = findViewById(R.id.btnGetLastLocation);
        btnGetLocationUpdate = findViewById(R.id.btnGetLocationUpdate);
        btnRemoveLocationUpdate = findViewById(R.id.btnRemoveLocationUpdate);
        //==================UI components handlers===================


        //====================GetLastLocation===================
        btnGetLastLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermission();
                Task<Location> task = client.getLastLocation();

                task.addOnSuccessListener(MainActivity.this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        String msg;
                        if(location != null){
                            msg = "Lat:" + location.getLatitude() + " Lng:" + location.getLongitude();
                        }
                        else{
                            msg = "No Last Known Location found";
                        }
                        Toast.makeText(MainActivity.this,msg, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        //====================GetLastLocation===================


        //====================LocationUpdate===================
        btnGetLocationUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermission();

                mLocationRequest = LocationRequest.create();
                mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                mLocationRequest.setInterval(10000);
                mLocationRequest.setFastestInterval(5000);
                mLocationRequest.setSmallestDisplacement(100);

                mLocationCallback = new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        if (locationResult != null) {
                            Location data = locationResult.getLastLocation();
                            double lat = data.getLatitude();
                            double lng = data.getLongitude();

                            String msg = "Lat:" + lat + " Lng:" + lng;
                            Toast.makeText(MainActivity.this,msg, Toast.LENGTH_SHORT).show();
                        }
                    };
                };
                client.requestLocationUpdates(mLocationRequest, mLocationCallback, null);


            }
        });
        //====================LocationUpdate===================


        //====================RemoveLocation===================
        btnRemoveLocationUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermission();
                client.removeLocationUpdates(mLocationCallback);

            }
        });
        //====================RemoveLocation===================
    }

    //===============-------FuncPermissionChecker------==================
    private boolean checkPermission(){
        int permissionCheck_Coarse = ContextCompat.checkSelfPermission(
                MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION);
        int permissionCheck_Fine = ContextCompat.checkSelfPermission(
                MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);

        if (permissionCheck_Coarse == PermissionChecker.PERMISSION_GRANTED
                || permissionCheck_Fine == PermissionChecker.PERMISSION_GRANTED) {
            return true;
        }
        else {
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
            return false;
        }
    }
    //===============-------FuncPermissionChecker------==================

}
