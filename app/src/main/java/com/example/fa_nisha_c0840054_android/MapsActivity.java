package com.example.fa_nisha_c0840054_android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


import com.example.fa_nisha_c0840054_android.Room.FavPlacesDB;
import com.example.fa_nisha_c0840054_android.Room.FavPlacesEntity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.SphericalUtil;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private static final int REQUEST_CODE = 1;
    private static final String TAG = "MapsActivity";

    private FavPlacesDB favPlacesDB;

    // Fused location provider client
    private FusedLocationProviderClient mClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private double lat;
    private double lng;
    private String address;
    private String date;
    private int id;
    private Polyline polyline;

    public static Intent getNewIntent(Activity activity, double lat, double lng, String address, String date, int id) {
        Intent intent = new Intent(activity, MapsActivity.class);
        intent.putExtra("lat", lat);
        intent.putExtra("lng", lng);
        intent.putExtra("address", address);
        intent.putExtra("date", date);
        intent.putExtra("id", id);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Map");

        lat = getIntent().getDoubleExtra("lat", 0.0);
        lng = getIntent().getDoubleExtra("lng", 0.0);
        address = getIntent().getStringExtra("address");
        date = getIntent().getStringExtra("date");
        id = getIntent().getIntExtra("id", -1);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mClient = LocationServices.getFusedLocationProviderClient(this);
        favPlacesDB = FavPlacesDB.getInstance(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.getUiSettings().setAllGesturesEnabled(false);

        if (!hasLocationPermission())
            requestLocationPermission();
        else
            startUpdateLocation();
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(@NonNull LatLng latLng) {
                Log.d("LAt--->", latLng.toString());
                List<Address> addressList = getLocationAddress(latLng);
                saveData(addressList, latLng);
            }

        });

        if (hasLocationPermission() && lat > 0.0 && lng > 0.0) {
            //plotFaVPlacesAndShowMarker();
//            showDistancePolyline();
        }

        findViewById(R.id.btnSatellite).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
            }
        });

        findViewById(R.id.btnDefault).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            }
        });


    }

    private void saveData(List<Address> addressToSave, LatLng latLng) {

        // Create the object of AlertDialog Builder class
        AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);

//                    // Set the message show for the Alert time
//                    builder.setMessage("Do you want to save this address?");

        // Set Alert Title
        builder.setTitle("Do you want to save this address?");

        // Set Cancelable false for when the user clicks on the outside the Dialog Box then it will remain show
        builder.setCancelable(false);

        // Set the positive button with yes name OnClickListener method is use of DialogInterface interface.
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (addressToSave != null && addressToSave.size() > 0) {
                    favPlacesDB.favPlacesDao().insert(
                            new FavPlacesEntity(
                                    addressToSave.get(0).getLocality(),
                                    getCurrentDate(),
                                    latLng.latitude,
                                    latLng.longitude
                            )
                    );
                } else {
                    favPlacesDB.favPlacesDao().insert(
                            new FavPlacesEntity(
                                    "",
                                    getCurrentDate(),
                                    latLng.latitude,
                                    latLng.longitude
                            )
                    );
                }

                finish();
                Toast.makeText(getApplicationContext(), "The place has been successfully added!",
                        Toast.LENGTH_LONG).show();
            }
        });

        // Set the Negative button OnClickListener method is use of DialogInterface interface.
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog,
                                int which) {

                // If user click no
                // then dialog box is canceled.
                dialog.cancel();
            }
        });

        // Create the Alert dialog
        AlertDialog alertDialog = builder.create();

        // Show the Alert Dialog box
        alertDialog.show();
    }

    private void
    plotFaVPlacesAndShowMarker() {
        if (lat > 0 && lng > 0) {
            mMap.clear();
            String title = "";
            if (TextUtils.isEmpty(address)) {
                title = date;
            } else {
                title = address;
            }
            LatLng selectedLocation = new LatLng(lat, lng);
            mMap.addMarker(new MarkerOptions().position(selectedLocation).title(title + "\n" + lat + "," + lng));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(selectedLocation, 10));
        }
    }

    private String getCurrentDate() {
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        return df.format(c);
    }

    private List<Address> getLocationAddress(LatLng latLng) {
        Geocoder geocoder;
        List<Address> addresses = null;
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

        } catch (Exception e) {
            e.printStackTrace();
        }
        return addresses;
    }

    private void startUpdateLocation() {
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(3000);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                mMap.clear();
                if (locationResult != null) {
                    Location location = locationResult.getLastLocation();
                    LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
                    mMap.addMarker(new MarkerOptions().position(userLocation).title("Your location!"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 10));
                    if (lat != 0 && lng != 0) {
                        showDistancePolyline(userLocation, new LatLng(lat, lng));
                        setUpMarkerDragListener();
                    }

                }
            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mClient.requestLocationUpdates(locationRequest, locationCallback, null);
    }

    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
    }

    private boolean hasLocationPermission() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                new AlertDialog.Builder(this)
                        .setMessage("The permission is mandatory")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(MapsActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
                            }
                        }).create().show();
            } else
                startUpdateLocation();
        }
    }

    private void showDistancePolyline(LatLng userLocation, LatLng selectedLocation) {
        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions.width(10f);
        polylineOptions.color(Color.GRAY);
        if (polyline != null)
            polyline.remove();
        polylineOptions.add(
                userLocation,
                selectedLocation
        );

        polyline = mMap.addPolyline(
                polylineOptions
        );

        String title = "";
        if (TextUtils.isEmpty(address)) {
            title = date;
        } else {
            title = address;
        }
        double newDistance = SphericalUtil.computeDistanceBetween(userLocation, selectedLocation);
        MarkerOptions marker = new MarkerOptions()
                .position(selectedLocation)
                .draggable(true)
                .title(title)
                .snippet(getFormattedDistance(newDistance))
                .anchor(0.5f, 0.5f);
        Marker distanceMarker = mMap.addMarker(marker);
        distanceMarker.showInfoWindow();
    }

    String getFormattedDistance(Double distanceToFormat) {
        if (distanceToFormat >= 1000) {
            double newDistance = distanceToFormat / 1000;
            String formattedDistance = new DecimalFormat("#.##").format(newDistance);
            return formattedDistance + "km";
        } else {
            String formattedDistance = new DecimalFormat("#.##").format(distanceToFormat);
            return formattedDistance + "m";
        }
    }


    private void setUpMarkerDragListener() {
        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @SuppressLint("PotentialBehaviorOverride")
            @Override
            public void onMarkerDragStart(Marker marker) {

            }


            @SuppressLint("PotentialBehaviorOverride")
            @Override
            public void onMarkerDragEnd(Marker marker) {
                lat = marker.getPosition().latitude;
                lng = marker.getPosition().longitude;

                List<Address> addressList = getLocationAddress(new LatLng(lat, lng));

                if (addressList != null && addressList.size() > 0) {
                    mMap.clear();
                    mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).draggable(true)
                            .title(addressList.get(0).getLocality()));
                    updateData(addressList, new LatLng(lat, lng));

                }

            }

            @SuppressLint("PotentialBehaviorOverride")
            @Override
            public void onMarkerDrag(Marker marker) {

            }
        });


    }

    private void updateData(List<Address> addressToSave, LatLng latLng) {
        // Create the object of AlertDialog Builder class
        AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);

        // Set Alert Title
        builder.setTitle("Do you want to update this address?");

        // Set Cancelable false for when the user clicks on the outside the Dialog Box then it will remain show
        builder.setCancelable(false);

        // Set the positive button with yes name OnClickListener method is use of DialogInterface interface.
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (addressToSave != null && addressToSave.size() > 0) {
                    favPlacesDB.favPlacesDao().update(id, addressToSave.get(0).getLocality(),
                            getCurrentDate(),
                            latLng.latitude,
                            latLng.longitude);
                } else {
                    favPlacesDB.favPlacesDao().update(id, "",
                            getCurrentDate(),
                            latLng.latitude,
                            latLng.longitude);
                }

                finish();
            }
        });

        // Set the Negative button OnClickListener method is use of DialogInterface interface.
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog,
                                int which) {

                // If user click no
                // then dialog box is canceled.
                dialog.cancel();
            }
        });

        // Create the Alert dialog
        AlertDialog alertDialog = builder.create();

        // Show the Alert Dialog box
        alertDialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return  true;
        } else
            return super.onOptionsItemSelected(item);
    }
}