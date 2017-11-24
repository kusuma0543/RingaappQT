package com.getinstaapp.instaapp;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static com.getinstaapp.instaapp.MapsActivity.hasPermissions;

public class UserSelMap extends FragmentActivity implements OnMapReadyCallback,LocationListener {

    private GoogleMap mMap;
    private TrackGps gps;
    int PERMISSION_ALL = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_sel_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.maps);
        mapFragment.getMapAsync(this);



        String[] PERMISSIONS = {android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION};

        if(!hasPermissions(this, PERMISSIONS)){
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        gps = new TrackGps(UserSelMap.this);

        Double lat = gps.getLatitude();
        Double lng=gps.getLongitude();
        LatLng sydney = new LatLng(lat,lng);
        float zoomLevel =10;
        //    new kilomilo().execute(global_url.URLGEO+"?glati="+lat+"&glongi="+lng);

        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

        List<Address> addresses  = null;
        try {
            addresses = geocoder.getFromLocation(lat,lng, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }


//        String state = addresses.get(0).getAdminArea();
//
//
//
//Toast.makeText(getApplicationContext(),state,Toast.LENGTH_SHORT).show();
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(lat,lng)).icon(BitmapDescriptorFactory.fromResource(R.drawable.ourloc
                )));

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,6.5f));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(12.5f), 2000, null);
        mMap.setMaxZoomPreference(15.5f);
        mMap.setMinZoomPreference(6.5f);


        mMap.addCircle(new CircleOptions()
                .center(new LatLng(lat,lng))
                .radius(1000)
                .fillColor(Color.argb(20, 255, 0, 255))
                .strokeColor(Color.BLUE)
                .strokeWidth(2.0f));


    }



    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
}
