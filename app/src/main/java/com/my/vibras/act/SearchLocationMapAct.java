package com.my.vibras.act;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.FirebaseDatabase;
import com.my.vibras.R;
import com.my.vibras.chat.ChatMessage;
import com.my.vibras.databinding.ActMapLocationSearchBinding;
import com.my.vibras.utility.GPSTracker;
import com.my.vibras.utility.Session;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SearchLocationMapAct extends AppCompatActivity implements OnMapReadyCallback {
    GoogleMap gMap;
    GPSTracker gpsTracker;
    ActMapLocationSearchBinding binding;
    Marker marker;
    String lat = "";
    String lon = "";
    String useriddeivce = "";
    String friend_idlast = "";
    String friendnamelast = "";
    String friendimage = "";
    String getChatImage = "";
    String from = "";
    String id = "";
Session session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.act_map_location_search);
        session = new Session(this);
        try {
            from = getIntent().getStringExtra("from");
            if (from.equalsIgnoreCase("121")) {
                useriddeivce = getIntent().getStringExtra("useriddeivce");
                friend_idlast = getIntent().getStringExtra("friend_idlast");
                friendnamelast = getIntent().getStringExtra("friendnamelast");
                friendimage = getIntent().getStringExtra("friendimage");
                getChatImage = getIntent().getStringExtra("getChatImage");
            } else {
                id = getIntent().getStringExtra("id");}
        } catch (Exception e) {

        }
        gpsTracker = new GPSTracker(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(SearchLocationMapAct.this);
        binding.sendBtn.setOnClickListener(v -> {

            if (from.equalsIgnoreCase("group")) {
                SimpleDateFormat dateFormat = new SimpleDateFormat(" hh:mm");
                Log.e("TAG", "onClick: " + dateFormat.format(new Date()));
                String time = dateFormat.format(new Date());

                LatLng latLng = new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude());
                FirebaseDatabase.getInstance()
                        .getReference()
                        .child("group_chat").child(id)
                        .push()
                        .setValue(new ChatMessage(session.getUserId(), "group",
                                "", session.getChatName()
                                , "", "", time, "", session.getChatImage(),
                                session.getChatImage(), lat,
                                lon));
                Snackbar.make(binding.getRoot(), "Sending...", Snackbar.LENGTH_SHORT).show();
            } else {
                SimpleDateFormat dateFormat = new SimpleDateFormat(" hh:mm");
                Log.e("TAG", "onClick: " + dateFormat.format(new Date()));
                String time = dateFormat.format(new Date());
                FirebaseDatabase.getInstance()
                        .getReference()
                        .child("chat")
                        .push()
                        .setValue(new ChatMessage(useriddeivce, friend_idlast,
                                "", friendnamelast,
                                "", "", time, "",
                                friendimage, getChatImage, lat, lon));
                Snackbar.make(binding.getRoot(), "Sending...", Snackbar.LENGTH_SHORT).show();
            }
            onBackPressed();
        });


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        try {
            gMap = googleMap;

            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(
                            this, Manifest.permission.ACCESS_COARSE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            gMap.setMyLocationEnabled(true);
            gMap.getUiSettings().setMyLocationButtonEnabled(true);

            LatLng sydney = new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude());
            binding.eventLocation.setText(gpsTracker.getLatitude() + " , " + gpsTracker.getLongitude());
               /* gMap.addMarker(new MarkerOptions()
                        .position(sydney)
                        .title("Marker"));*/
            //   gMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(sydney)      // Sets the center of the map to location user
                    .zoom(13)                   // Sets the zoom
                    .build();
            gMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            gMap.setOnMapClickListener(latLng -> {
                gMap.clear();

                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                googleMap.clear();
                googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                googleMap.addMarker(markerOptions);
                lat = "" + latLng.latitude;
                lon = "" + latLng.longitude;
                binding.eventLocation.setText(lat + " , " + lon);
            });

        } catch (Exception e) {
            Log.d("TAG", "onMapReady: " + e);
        }
    }
}