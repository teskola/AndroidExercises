package com.example.commonintents;

import static java.net.Proxy.Type.HTTP;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.time.OffsetTime;

public class MainActivity extends AppCompatActivity implements LocationListener {
    private Button mapButton;
    private Button alarmButton;
    private Button smsButton;
    private Button callButton;
    private EditText numberText;
    private TextView locationText;
    double lat, lon;
    static public final int PERMISSION_REQUEST_CODE = 1;
    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        alarmButton = findViewById(R.id.alarmButton);
        mapButton = findViewById(R.id.mapButton);
        smsButton = findViewById(R.id.smsButton);
        callButton = findViewById(R.id.callButton);
        numberText = findViewById(R.id.phoneText);
        locationText = findViewById(R.id.locationTextView);

        callButton.setOnClickListener(view -> {
            String phoneNumber = numberText.getText().toString();
            callPhoneNumber(phoneNumber);
        });

        smsButton.setOnClickListener(view -> composeSmsMessage("Hello."));
        mapButton.setOnClickListener(view -> showMap(Uri.parse("geo:61.5,23.79")));
        alarmButton.setOnClickListener(view -> createAlarm("Herätys!", 8, 0, true));

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermission();
        }

    }

    public void onResume() {
        super.onResume();
        startLocationListener();
    }
    public void onPause() {
        super.onPause();
        locationManager = null;
    }



    public void startLocationListener() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Location currentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (currentLocation != null) {
                lat = currentLocation.getLatitude();
                lon = currentLocation.getLongitude();
                locationText.setText("lat: " + lat + "\nlon: " + lon);
            } else {
                locationText.setText("No GPS data.");
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        }
    }

    public void requestPermission() {
        String[] permissions = new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
        };
        ActivityCompat.requestPermissions(this, permissions, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    startLocationListener();
                } else {
                    locationText.setText("No access to GPS data.");
                }
        }
    }

    public void callPhoneNumber(String phoneNumber) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:" + phoneNumber));
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
        } else {
            String[] permissions = new String[]{
                    Manifest.permission.CALL_PHONE
            };
            ActivityCompat.requestPermissions(this, permissions, 0);
        }

    }


    public void composeSmsMessage(String message) {
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:+358400000000"));
        intent.putExtra("sms_body", message);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }


    public void createAlarm(String message, int hour, int minutes, boolean skip) {
        Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM)
                .putExtra(AlarmClock.EXTRA_MESSAGE, message)
                .putExtra(AlarmClock.EXTRA_HOUR, hour)
                .putExtra(AlarmClock.EXTRA_MINUTES, minutes)
                .putExtra(AlarmClock.EXTRA_SKIP_UI, skip);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    public void showMap(Uri geoLocation) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(geoLocation);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }


    @Override
    public void onLocationChanged(@NonNull Location location) {
        lat = location.getLatitude();
        lon = location.getLongitude();
        locationText.setText("lat: " + lat + "\nlon: " + lon);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }
}