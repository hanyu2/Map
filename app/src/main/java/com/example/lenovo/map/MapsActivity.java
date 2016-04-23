package com.example.lenovo.map;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import com.google.android.gms.maps.CameraUpdateFactory;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;


import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, ActivityCompat.OnRequestPermissionsResultCallback{

    private GoogleMap mMap;
    private Marker mMarker;
    FloatingActionButton add;
    FloatingActionButton list;
    FloatingActionButton refresh;
    private Location location = null;
    private boolean bound = false;
    private boolean mPermissionDenied = false;
    private boolean isLocated = false;
    private double  latitude;
    private double  longitude;
    ArrayList<MessageData> dataList = new ArrayList<MessageData>();
    Thread thread;
    UploadThread uploadThread;
    Map<Marker, MessageData> markers;

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

        add = (FloatingActionButton) findViewById(R.id.add);
        refresh = (FloatingActionButton) findViewById(R.id.refresh);
        list = (FloatingActionButton) findViewById(R.id.list);
        View.OnClickListener myListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.add:
                        Intent intent = new Intent();
                        Bundle bundle = new Bundle();
                        bundle.putDouble("lat",latitude);
                        bundle.putDouble("lng",longitude);
                        intent.putExtras(bundle);
                        intent.setClass(MapsActivity.this, SendMessageActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.refresh:
                        uploadThread = new UploadThread();
                        thread = new Thread(uploadThread);
                        thread.start();
                        break;
                    case R.id.list:
                        Intent myIntent = new Intent();
                        Bundle myBundle = new Bundle();
                        myBundle.putParcelableArrayList("MessageList",dataList);
                        myIntent.putExtras(myBundle);
                        myIntent.setClass(MapsActivity.this, MessageListActivity.class);
                        startActivity(myIntent);
                        break;
                }
            }
        };
        add.setOnClickListener(myListener);
        refresh.setOnClickListener(myListener);
        list.setOnClickListener(myListener);
    }

    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (mMap != null) {
            // Access to the location has been granted to the app.
            //mMap.getUiSettings().setZoomControlsEnabled(true);
            //mMap.getUiSettings().setZoomGesturesEnabled(true);
            //mMap.setMyLocationEnabled(true);
            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    if(markers.containsKey(marker)){
                        MessageData messageData = markers.get(marker);
                        Intent intent = new Intent(MapsActivity.this, MessageDetailActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("MessageDetail", messageData);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                    return false;
                }
            });
            final LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            if(!isLocated) {
                locationManager.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER, 1000, 0,
                        new LocationListener() {
                            @Override
                            public void onLocationChanged(Location location) {
                                //mMap.clear();
                                CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude()));
                                CameraUpdate zoom = CameraUpdateFactory.zoomTo(17);
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                                LatLng latLng = new LatLng(latitude, longitude);
                                mMarker = mMap.addMarker(new MarkerOptions().position(latLng));

                               /* mMap.moveCamera(center);
                                mMap.animateCamera(zoom);*/
                                List<MessageData> messages = new ArrayList<MessageData>();
                                double[] l1 = {33.4248535, -111.9495278};
                                String[] tag1 = {"hello", "world"};
                                MessageData m1 = new MessageData("h@163.com", "2000", "2001", "nick", l1, "test1", "message1", 3, tag1);
                                double[] l2 = {33.4436965, -111.9297588};
                                String[] tag2 = {"hello2", "world2"};
                                MessageData m2 = new MessageData("h@163.com", "2000", "2001", "nick", l2, "test2", "message2", 3, tag2);
                                messages.add(m1);
                                messages.add(m2);
                                double latMax = 0.0;
                                double longMax = 0.0;
                                for (int i = 0; i < messages.size(); i++) {
                                    MessageData msg = messages.get(i);
                                    LatLng latLng2 = new LatLng(msg.getLatitude(), msg.getLongitude());
                                    latMax = Math.max(latMax, Math.abs(Math.abs(msg.getLatitude()) - Math.abs(latitude)));
                                    longMax = Math.max(longMax, Math.abs(Math.abs(msg.getLongitude()) - Math.abs(longitude)));
                                    switch (i % 5) {
                                        case 0:
                                            mMap.addMarker(new MarkerOptions().title(msg.getTitle()).position(latLng2).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                                            break;
                                        case 1:
                                            mMap.addMarker(new MarkerOptions().title(msg.getTitle()).position(latLng2).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                                            break;
                                        case 2:
                                            mMap.addMarker(new MarkerOptions().title(msg.getTitle()).position(latLng2).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                                            break;
                                        case 3:
                                            mMap.addMarker(new MarkerOptions().title(msg.getTitle()).position(latLng2).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
                                            break;
                                        case 4:
                                            mMap.addMarker(new MarkerOptions().title(msg.getTitle()).position(latLng2).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));
                                            break;
                                    }

                                }

                                LatLngBounds AUSTRALIA = new LatLngBounds(
                                        new LatLng(latitude - latMax, longitude - longMax), new LatLng(latitude + latMax, longitude + longMax));
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(AUSTRALIA.getCenter(), 20));
                                mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(AUSTRALIA, 20));
                                isLocated = true;

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


        //Toast.makeText(getApplicationContext(), latMax + " " + longMax, Toast.LENGTH_LONG).show();
    }

    class UploadThread implements Runnable{
        @Override
        public void run() {
            JSONObject msg = new JSONObject();
            try {
                msg.put("email", "krishnazongsi");
                msg.put("lat", "1.001");
                msg.put("lng", "2.001");
                msg.put("time", "2016-04-13 11:20:22");
                msg.put("type", 2);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            List<MessageData> datas = NetUtils.getMsgs(msg);
            if(datas != null){

            }
        }
    }
}