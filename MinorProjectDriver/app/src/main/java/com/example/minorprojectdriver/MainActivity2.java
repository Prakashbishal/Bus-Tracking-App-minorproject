package com.example.minorprojectdriver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Switch;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity2 extends AppCompatActivity {
    private Switch mySwitch;
    private static final int PERMISSION_REQUEST_CODE = 123; // You can use any integer value
    private boolean locationUpdatesEnabled = false;
    private DatabaseReference databaseReference;
    private LocationManager locationManager;
    private LocationListener locationListener;

    private final Handler handler = new Handler();
    private static final int LOCATION_UPDATE_INTERVAL = 300; // Update every 0.3 second

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(this);
        final LinearLayout bg=findViewById(R.id.background);
        // Initialize the Switch inside the onCreate method
        mySwitch = findViewById(R.id.switch1);
        databaseReference = FirebaseDatabase.getInstance().getReference("driver");
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        mySwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locationUpdatesEnabled = mySwitch.isChecked();
                if (locationUpdatesEnabled) {
                    startLocationUpdates();
                    bg.setBackgroundResource(R.drawable.round_back_green);
                    mySwitch.setTextColor(Color.WHITE);

                } else {
                    stopLocationUpdates();
                    // Change the background color back to its original state
                    bg.setBackgroundResource(R.drawable.round_back_red);
                    mySwitch.setTextColor(Color.BLACK);

                }
            }
        });

    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_CODE);
            return;
        }

        Intent intent=getIntent();
        String idNo = intent.getStringExtra("ID_NO");
        if (locationListener == null) {

            locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(@NonNull Location location) {

                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();

                    // Update driver's location in the database
                    assert idNo != null;
                    databaseReference.child(idNo).child("location").child("latitude").setValue(latitude);
                    databaseReference.child(idNo).child("location").child("longitude").setValue(longitude);
                }

                // Implement other LocationListener methods
            };
        }

        // Use a handler to schedule location updates
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (locationUpdatesEnabled) {
                    // Request location updates every second
                    if (ActivityCompat.checkSelfPermission(MainActivity2.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity2.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, LOCATION_UPDATE_INTERVAL, 10, locationListener);
                    // Continue updating every second
                    handler.postDelayed(this, LOCATION_UPDATE_INTERVAL);
                }
            }
        }, 0);
    }

    private void stopLocationUpdates() {
        if (locationListener != null) {
            locationManager.removeUpdates(locationListener);
        }
    }
}
