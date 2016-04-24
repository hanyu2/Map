package com.example.lenovo.map;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.lang.reflect.Array;
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

import android.os.Message;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, ActivityCompat.OnRequestPermissionsResultCallback{

    private GoogleMap mMap;
    private Marker mMarker;
    FloatingActionButton add;
    FloatingActionButton list;
    FloatingActionButton refresh;
    private boolean bound = false;
    private boolean mPermissionDenied = false;
    private boolean isLocated = false;
    private double  latitude;
    private double  longitude;
    Location location;
    String provider;
    ArrayList<MessageData> dataList = new ArrayList<MessageData>();

    MyHandler handler;
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
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int resp = googleAPI.isGooglePlayServicesAvailable(this);
        if(resp != ConnectionResult.SUCCESS){
            Toast.makeText(this, "Google Play Service Error " + resp, Toast.LENGTH_LONG).show();
        }
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
        add = (FloatingActionButton) findViewById(R.id.add);
        refresh = (FloatingActionButton) findViewById(R.id.refresh);
        list = (FloatingActionButton) findViewById(R.id.list);
        markers = new HashMap<Marker, MessageData>();
        enableMyLocation();

        //setMarkers();
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

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if (markers.containsKey(marker)) {
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
    }

    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (mMap != null) {
            mMap.setMyLocationEnabled(true);
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            provider = locationManager.getBestProvider(criteria, true);
            LocationListener locationListener = new LocationListener() {
                public void onLocationChanged(Location currentLocation) {
                    if(isBetterLocation(currentLocation, location)) {
                        makeUseOfNewLocation(currentLocation);
                        location = currentLocation;
                    }
                }

                public void onStatusChanged(String currentProvider, int status, Bundle extras) {
                    if(status == 2) {
                        provider = currentProvider;
                    }
                }

                public void onProviderEnabled(String provider) {}

                public void onProviderDisabled(String provider) {}
            };

            locationManager.requestLocationUpdates(provider, 1000, 0, locationListener);
            handler = new MyHandler();
            uploadThread = new UploadThread();
            thread = new Thread(uploadThread);
            thread.start();
        }
    }

    private static final int TWO_MINUTES = 1000 * 60 * 2;

    /** Determines whether one Location reading is better than the current Location fix
     * @param location  The new Location that you want to evaluate
     * @param currentBestLocation  The current Location fix, to which you want to compare the new one
     */
    protected boolean isBetterLocation(Location location, Location currentBestLocation) {
        if (currentBestLocation == null) {
            // A new location is always better than no location
            return true;
        }

        // Check whether the new location fix is newer or older
        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
        boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
        boolean isNewer = timeDelta > 0;

        // If it's been more than two minutes since the current location, use the new location
        // because the user has likely moved
        if (isSignificantlyNewer) {
            return true;
            // If the new location is more than two minutes older, it must be worse
        } else if (isSignificantlyOlder) {
            return false;
        }

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isMoreAccurate = accuracyDelta > 0 ? true : false;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        // Check if the old and new location are from the same provider
        boolean isFromSameProvider = isSameProvider(location.getProvider(),
                currentBestLocation.getProvider());

        // Determine location quality using a combination of timeliness and accuracy
        if (isMoreAccurate) {
            return true;
        }
        return false;
    }
    public void makeUseOfNewLocation(Location location){
        CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude()));
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(17);
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        mMap.moveCamera(center);
        mMap.animateCamera(zoom);
    }


    /** Checks whether two providers are the same */
    private boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }

    public void setMarkers(){
        mMap.clear();
        Marker marker;
        double latMax = 0.0;
        double longMax = 0.0;
        for (int i = 0; i < dataList.size(); i++) {
            MessageData msg = dataList.get(i);
            LatLng latLng2 = new LatLng(msg.getLatitude(), msg.getLongitude());
            latMax = Math.max(latMax, Math.abs(Math.abs(msg.getLatitude()) - Math.abs(latitude)));
            longMax = Math.max(longMax, Math.abs(Math.abs(msg.getLongitude()) - Math.abs(longitude)));
            switch (i % 5) {
                case 0:
                    marker = mMap.addMarker(new MarkerOptions().title(msg.getTitle()).position(latLng2).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
                    markers.put(marker, msg);
                    break;
                case 1:
                    marker = mMap.addMarker(new MarkerOptions().title(msg.getTitle()).position(latLng2).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                    markers.put(marker, msg);
                    break;
                case 2:
                    marker = mMap.addMarker(new MarkerOptions().title(msg.getTitle()).position(latLng2).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                    markers.put(marker, msg);
                    break;
                case 3:
                    marker = mMap.addMarker(new MarkerOptions().title(msg.getTitle()).position(latLng2).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
                    markers.put(marker, msg);
                    break;
                case 4:
                    marker = mMap.addMarker(new MarkerOptions().title(msg.getTitle()).position(latLng2).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));
                    markers.put(marker, msg);
                    break;
            }

        }

        LatLngBounds AUSTRALIA = new LatLngBounds(
                new LatLng(latitude - latMax, longitude - longMax), new LatLng(latitude + latMax, longitude + longMax));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(AUSTRALIA.getCenter(), 20));
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(AUSTRALIA, 20));
    }

    class MyHandler extends android.os.Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int msgId = msg.what;
            if(msgId==1){
                setMarkers();
            }
        }
    }
    class UploadThread implements Runnable{
        @Override
        public void run() {
            JSONObject msg = new JSONObject();
            try {
                SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String postTime = sDateFormat.format(new Date());
                double tempLat = latitude;
                double tempLng = longitude;
                msg.put("lat",tempLat);
                msg.put("lng",tempLng);
                msg.put("type",1);
                msg.put("time",postTime);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            List<MessageData> datas = NetUtils.getMsgs(msg);
            System.out.println("+++++++++++++++++++++" + datas.size() + "++++++++++++++++++");
            if(datas != null){
                dataList.clear();
                dataList.addAll(datas);

                Message message = new Message();
                message.what = 1;
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("dataList", dataList);
                message.setData(bundle);
                isLocated = false;
                handler.sendMessage(message);
            }
        }
    }
}