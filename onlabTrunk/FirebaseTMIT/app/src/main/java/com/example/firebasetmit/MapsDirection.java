package com.example.firebasetmit;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.HandlerThread;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.firebasetmit.FirebaseObject.ActivityObject;
import com.example.firebasetmit.directionhelpers.FetchURL;
import com.example.firebasetmit.directionhelpers.GeoTask;
import com.example.firebasetmit.directionhelpers.TaskLoadedCallback;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;


public class MapsDirection extends AppCompatActivity
        implements TaskLoadedCallback, GeoTask.Geo, OnMapReadyCallback {

    private MarkerOptions place2;
    private Polyline currentPolyline;
    static double min;
    static int dist;

    //Play ServicesLocation

    public static final int DEFAULT_ZOOM = 15;
    public static final int PERMISSION_REQUEST_CODE = 9001;
    public static final int GPS_REQUEST_CODE = 9003;
    public static final String TAG = "MapDebug";
    private static final int PLAY_SERVICES_ERROR_CODE = 9002;
    HandlerThread mHandlerThread;
    private ImageButton mBtnLocate;
    private TextView mOutputText;
    private GoogleMap mGoogleMap;
    private FusedLocationProviderClient mLocationClient;
    private LocationCallback mLocationCallback;
    private String urlCode;
    private SeekBar mSeekBar;

    private LatLng mLocationCallbackResult;
    private Circle mCircle;


    // Get data from Firebase
    private DatabaseReference mDatabaseReference;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_direction);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Firebase reference
        mDatabaseReference = FirebaseDatabase.getInstance().getReference()
                .child("Activities/Places/Place");

        mBtnLocate = findViewById(R.id.btnGetDirection);
        mOutputText = findViewById(R.id.tv_location);
        mSeekBar = findViewById(R.id.verticalSeekBar);

        mBtnLocate.setOnClickListener(view -> {
            createUrlWithGpsPositions(new LatLng(mLocationCallbackResult.latitude
                    , mLocationCallbackResult.longitude)
                    ,new LatLng(47.4775984, 19.0463812));
            new GeoTask(MapsDirection.this).execute(urlCode);
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map_fragment_container);
            mapFragment.getMapAsync(this);
            new FetchURL(this).execute(getUrl(mLocationCallbackResult,
                    place2.getPosition()), "driving");
        });

        initGoogleMap();

        mLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {

                if (locationResult == null) {
                    return;
                }
                mLocationCallbackResult = new LatLng(locationResult.getLastLocation().getLatitude()
                        , locationResult.getLastLocation().getLongitude());
                runOnUiThread(() -> {
                    //gotoLocation(mLocationCallbackResult.latitude
                    //        , mLocationCallbackResult.longitude);
                    showMarker(mLocationCallbackResult.latitude
                            , mLocationCallbackResult.longitude, "Home");

                    showMarker(47.4775984, 19.0463812, "Charger point");

                    place2 = new MarkerOptions().position(new LatLng(47.4775984,
                            19.0463812)).title("Charger point");

                    Log.d(TAG, "inside runOnUiThread method: Thread name: " + Thread.currentThread().getName());

                });

                Log.d(TAG, "onLocationResult: " + mLocationCallbackResult.latitude + " \n" +
                        mLocationCallbackResult.longitude);

                Log.d(TAG, "onLocationResult: Thread name: " + Thread.currentThread().getName());

            }
        };
        getCurrentLocation();
        getFirebaseData(mDatabaseReference);
    }


    private void getFirebaseData(DatabaseReference reference){
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                for (DataSnapshot child : dataSnapshot.getChildren()){
                    Log.d(TAG, "onChildAdded: the dataSnapshot is" + child);
                    ActivityObject user = dataSnapshot.getValue(ActivityObject.class);

                    double lat = Double.parseDouble(user.locationLat);
                    double lng = Double.parseDouble(user.locationLng);
                    showMarker(lat, lng, user.placeName);

                    Log.d(TAG, "onChildAdded: The dataSnapshot name is: "
                            + Objects.requireNonNull(user).getPlaceName());
                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.d(TAG, "onChildChanged: the child value has been changed: " +
                        dataSnapshot);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void createUrlWithGpsPositions(LatLng pos1, LatLng pos2){
        String result11 = String.valueOf(pos1.latitude);
        String result12 = String.valueOf(pos1.longitude);
        String result21 = String.valueOf(pos2.latitude);
        String result22 = String.valueOf(pos2.longitude);
        urlCode = "https://maps.googleapis.com/maps/api/distancematrix/json?origins="
                + result11 + "," + result12 + "&destinations=" + result21 + "," + result22 +
                "&mode=driving&language=fr-FR&avoid=tolls&key=AIzaSyCfizrfXevvCDJqMMzvlYJU5wqM_vdL2Zo";
    }


    private String getUrl(LatLng origin, LatLng dest) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Mode
        String mode = "mode=" + "walking";
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        // Output format
        String output = "json";
        // Building the url to the web service
        String s = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters
                + "&key=" + "AIzaSyBjjTCypOOzKY6Bd-sDpNzmBpV7Tq4RqG0";
        Log.d(TAG, "getUrl: the output is: " + output);
        Log.d(TAG, "getUrl: the parameters is: " + parameters);
        Log.d(TAG, "getUrl: the maps key is: " + getString(R.string.google_maps_key));
        Log.d(TAG, "getUrl: the final string is: " + s);
        return s;
    }

    @Override
    public void onTaskDone(Object... values) {
        if (currentPolyline != null)
            currentPolyline.remove();
        currentPolyline = mGoogleMap.addPolyline((PolylineOptions) values[0]);

        mCircle = mGoogleMap.addCircle(new CircleOptions()
                .center(mLocationCallbackResult)
                .radius(dist * 1000 * 1.5)
                .strokeColor(Color.BLUE)
                .fillColor(0x220000ff)
                .strokeWidth(5.0f)
        );
        Toast.makeText(this, "Duration= " +
                (int) (min / 60) + " hr " + (int) (min % 60) + " mins", Toast.LENGTH_LONG).show();
        Toast.makeText(this, "Distance= " + dist + " kilometers"
                , Toast.LENGTH_LONG).show();
    }

    @Override
    public void setDouble(String result) {
        String[] res = result.split(",");
        min = Double.parseDouble(res[0]) / 60;
        dist = Integer.parseInt(res[1]) / 1000;
    }


    //private void geoLocate(View view) {
    //    hideSoftKeyboard(view);

    //    String locationName = mSearchAddress.getText().toString();

    //    Geocoder geocoder = new Geocoder(this, Locale.getDefault());

    //    try {
    //        List<Address> addressList = geocoder.getFromLocationName(locationName, 1);

    //        if (addressList.size() > 0) {
    //            Address address = addressList.get(0);
    //
    //            gotoLocation(address.getLatitude(), address.getLongitude());

    //            showMarker(address.getLatitude(), address.getLongitude());

    //            Toast.makeText(this, address.getLocality(), Toast.LENGTH_SHORT).show();

    //            Log.d(TAG, "geoLocate: Locality: " + address.getLocality());
    //        }

    //        for (Address address : addressList) {
    //            Log.d(TAG, "geoLocate: Address: " + address.getAddressLine(address.getMaxAddressLineIndex()));
    //        }


    //    } catch (IOException e) {


    //    }


    //}

    private void showMarker(double lat, double lng, String title) {
        mGoogleMap.addMarker(new MarkerOptions()
                .position(new LatLng(lat, lng))
                .title(title)
        );
    }

    private void hideSoftKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        assert imm != null;
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void initGoogleMap() {

        if (isServicesOk()) {
            if (isGPSEnabled()) {
                if (checkLocationPermission()) {
                    Toast.makeText(this, "Ready to Map", Toast.LENGTH_SHORT).show();

                    SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager()
                            .findFragmentById(R.id.map_fragment_container);

                    supportMapFragment.getMapAsync(this);
                } else {
                    requestLocationPermission();
                }
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG, "onMapReady: map is showing on the screen");

        mGoogleMap = googleMap;
        //gotoLocation(ISLAMABAD_LAT, ISLAMABAD_LNG);
        //        mGoogleMap.setMyLocationEnabled(true);

        mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
        mGoogleMap.getUiSettings().setMapToolbarEnabled(true);
    }

    private void gotoLocation(double lat, double lng) {

        LatLng latLng = new LatLng(lat, lng);

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 13);

        mGoogleMap.moveCamera(cameraUpdate);
        //        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

    }

    private boolean isGPSEnabled() {

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        boolean providerEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (providerEnabled) {
            return true;
        } else {

            AlertDialog alertDialog = new AlertDialog.Builder(this)
                    .setTitle("GPS Permissions")
                    .setMessage("GPS is required for this app to work. Please enable GPS.")
                    .setPositiveButton("Yes", ((dialogInterface, i) -> {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivityForResult(intent, GPS_REQUEST_CODE);
                    }))
                    .setCancelable(false)
                    .show();

        }

        return false;
    }

    private boolean checkLocationPermission() {

        return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;
    }

    private boolean isServicesOk() {

        GoogleApiAvailability googleApi = GoogleApiAvailability.getInstance();

        int result = googleApi.isGooglePlayServicesAvailable(this);

        if (result == ConnectionResult.SUCCESS) {
            return true;
        } else if (googleApi.isUserResolvableError(result)) {
            Dialog dialog = googleApi.getErrorDialog(this, result, PLAY_SERVICES_ERROR_CODE, task ->
                    Toast.makeText(this, "Dialog is cancelled by User", Toast.LENGTH_SHORT).show());
            dialog.show();
        } else {
            Toast.makeText(this, "Play services are required by this application", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    private void requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {

            case R.id.maptype_none: {
                mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NONE);
                break;
            }
            case R.id.maptype_normal: {
                mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                break;
            }
            case R.id.maptype_satellite: {
                mGoogleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                break;
            }
            case R.id.maptype_terrain: {
                mGoogleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                break;
            }
            case R.id.maptype_hybrid: {
                mGoogleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                break;
            }
            case R.id.current_location: {
                getLocationUpdates();
                break;
            }

        }

        return super.onOptionsItemSelected(item);
    }

    private void getCurrentLocation() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mLocationClient.getLastLocation().addOnCompleteListener(task -> {

            if (task.isSuccessful()) {
                Location location = task.getResult();
                if (location != null) {
                    gotoLocation(location.getLatitude(), location.getLongitude());
                }
            } else {
                Log.d(TAG, "getCurrentLocation: Error: " + task.getException().getMessage());
            }
        });
    }

    private void getLocationUpdates() {

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(1000);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        mHandlerThread = new HandlerThread("LocationCallbackThread");
        mHandlerThread.start();

        mLocationClient.requestLocationUpdates(locationRequest, mLocationCallback, mHandlerThread.getLooper());

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            //boolean mLocationPermissionGranted = true;
            Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Permission not granted", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GPS_REQUEST_CODE) {

            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

            boolean providerEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            if (providerEnabled) {
                Toast.makeText(this, "GPS is enabled", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "GPS not enabled. Unable to show user location", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mLocationCallback != null) {
            mLocationClient.removeLocationUpdates(mLocationCallback);
            if (mOutputText != null){
                Toast.makeText(this, mOutputText.getText(), Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHandlerThread != null) {
            mHandlerThread.quit();
        }
    }

}









