package com.example.appmomos.googlemaps;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public   class AdminViewActivity extends AppCompatActivity implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleMap.OnMarkerDragListener,
        GoogleMap.OnMapLongClickListener
{

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    //Our Map
    private GoogleMap mMap;

    //To store longitude and latitude from map
    private double longitude;
    private double latitude;


    //Google ApiClient
    private GoogleApiClient googleApiClient;


    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view);

        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Admin Section");



        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //Initializing googleapi client
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.blank_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getItemId() == android.R.id.home)
        {
            startActivity(new Intent(AdminViewActivity.this, MapsActivity.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed()
    {
        startActivity(new Intent(AdminViewActivity.this, MapsActivity.class));
        finish();
    }



    @Override
    protected void onStart() {
        googleApiClient.connect();
        super.onStart();
    }


    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }

    //Getting current location
    private void getCurrentLocation()
    {
            mMap.clear();

            //Creating a location object
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            if (location != null) {
                //Getting longitude and latitude
                longitude = location.getLongitude();
                latitude = location.getLatitude();

                //moving the map to location
                 moveMap1(latitude,longitude);
            }
    }

    private void moveMap1(double latitude, double longitude)
    {
      LatLng latLng0 = new LatLng(latitude, longitude);

        ArrayList<Double> latitudeArray = new ArrayList<>();
        latitudeArray.add(latitude);
        latitudeArray.add(latitude+1);
        latitudeArray.add(latitude+0.2);
        latitudeArray.add(latitude+0.5);
        latitudeArray.add(latitude-0.5);


        ArrayList<Double> longitudeArray = new ArrayList<>();
        longitudeArray.add(longitude);
        longitudeArray.add(longitude+1);
        longitudeArray.add(longitude+0.2);
        longitudeArray.add(longitude+0.5);
        longitudeArray.add(longitude-0.5);

        int zoomCapacity = 8;

        for (int i =0;i<5;i++)
        {
            LatLng latLng = new LatLng(latitudeArray.get(i), longitudeArray.get(i));

            mMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .draggable(false)
                    .title("Emp "+(i+1) ));
        }

        /*mMap.addMarker(new MarkerOptions()
                .position(latLng1)
                .draggable(false)
                .title("Emp 2"));

        mMap.addMarker(new MarkerOptions()
                .position(latLng2)
                .draggable(false)
                .title("Emp 3"));


        mMap.addMarker(new MarkerOptions()
                .position(latLng3)
                .draggable(false)
                .title("Emp 4"));

        mMap.addMarker(new MarkerOptions()
                .position(latLng4)
                .draggable(false)
                .title("Emp 5"));*/

        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng0));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(zoomCapacity));
    }


    @Override
    public void onConnected(Bundle bundle) {
        getCurrentLocation();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        //Clearing all the markers
        /*mMap.clear();

        //Adding a new marker to the current pressed position
        mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .draggable(false));// false means not dragable and true means map icon draggable*/
    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        //Getting the coordinates
        latitude = marker.getPosition().latitude;
        longitude = marker.getPosition().longitude;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng latLng = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(latLng).draggable(true));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.setOnMarkerDragListener(this);
        mMap.setOnMapLongClickListener(this);
    }


}
