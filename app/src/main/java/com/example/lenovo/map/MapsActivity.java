package com.example.lenovo.map;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationListener;
import android.os.Bundle;
import com.google.android.gms.maps.CameraUpdateFactory;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, ActivityCompat.OnRequestPermissionsResultCallback{

    private GoogleMap mMap;
    private Marker mMarker;
    private boolean bound = false;
    private boolean mPermissionDenied = false;
    private Location location = null;

    /**
     * Request code for location permission request.
     *
     * @see #onRequestPermissionsResult(int, String[], int[])
     */
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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
        enableMyLocation();
    }

    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (mMap != null) {
            // Access to the location has been granted to the app.
            mMap.getUiSettings().setZoomControlsEnabled(true);
            mMap.getUiSettings().setZoomGesturesEnabled(true);
            //mMap.setMyLocationEnabled(true);
            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    Toast.makeText(getApplicationContext(), marker.getTitle(), Toast.LENGTH_LONG).show();
                    return false;
                }
            });
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, 5000, 0,
                    new LocationListener() {
                        @Override
                        public void onLocationChanged(Location location) {
                            mMap.clear();
                            CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(),location.getLongitude()));
                            CameraUpdate zoom = CameraUpdateFactory.zoomTo(17);
                            double latitude = location.getLatitude();
                            double longitude = location.getLongitude();
                            LatLng latLng = new LatLng(latitude, longitude);
                            mMap.addMarker(new MarkerOptions().position(latLng).title("Me"));
                            mMap.moveCamera(center);
                            mMap.animateCamera(zoom);
                        }

                        @Override
                        public void onStatusChanged(String provider, int status, Bundle extras) {

                        }

                        @Override
                        public void onProviderEnabled(String provider) {

                        }

                        @Override
                        public void onProviderDisabled(String provider) {

                        }
                    });

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    3000, 0, new LocationListener() {
                        @Override
                        public void onLocationChanged(Location location) {
                            mMap.clear();
                            CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(),location.getLongitude()));
                            CameraUpdate zoom = CameraUpdateFactory.zoomTo(16);
                            double latitude = location.getLatitude();
                            double longitude = location.getLongitude();
                            LatLng latLng = new LatLng(latitude, longitude);
                            mMap.addMarker(new MarkerOptions().position(latLng).title("Me"));
                            mMap.moveCamera(center);
                            mMap.animateCamera(zoom);
                        }

                        @Override
                        public void onStatusChanged(String provider, int status, Bundle extras) {

                        }

                        @Override
                        public void onProviderEnabled(String provider) {

                        }

                        @Override
                        public void onProviderDisabled(String provider) {

                        }
                    });

        }
    }
}
