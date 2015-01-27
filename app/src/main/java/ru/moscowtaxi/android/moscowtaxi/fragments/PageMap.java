package ru.moscowtaxi.android.moscowtaxi.fragments;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import ru.moscowtaxi.android.moscowtaxi.R;
import ru.moscowtaxi.android.moscowtaxi.helpers.WebUtils;
import ru.moscowtaxi.android.moscowtaxi.helpers.http.TaxiApi;
import ru.moscowtaxi.android.moscowtaxi.preferences.PreferenceUtils;

/**
 * Created by alex-pers on 11/30/14.
 */
public class PageMap extends Fragment {
    static final float ZOOM_MAP_USER = 17;

    MapView mMapView;
    private GoogleMap googleMap;
    private BroadcastReceiver receiver;

    public PageMap() {

    }

    public static Fragment newInstance() {
        PageMap fragment = new PageMap();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.page_map, container, false);

        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume();// needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        googleMap = mMapView.getMap();

        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");


        getActivity().registerReceiver(receiver, filter);
        centerMapOnMyLocation();


        return rootView;
    }

    private void centerMapOnMyLocation() {

        googleMap.setMyLocationEnabled(true);

        LocationManager locationManager = (LocationManager) getActivity().getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();

        Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
        if (location != null) {
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(location.getLatitude(), location.getLongitude()), 13));

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(location.getLatitude(), location.getLongitude()))      // Sets the center of the map to location user
                    .zoom(ZOOM_MAP_USER)                   // Sets the zoom
                    .bearing(90)                // Sets the orientation of the camera to east
                    .build();                   // Creates a CameraPosition from the builder
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d("app", "Network connectivity change");
                if (intent.getExtras() != null) {
                    NetworkInfo ni = (NetworkInfo) intent.getExtras().get(ConnectivityManager.EXTRA_NETWORK_INFO);
                    if (ni != null && ni.getState() == NetworkInfo.State.CONNECTED) {
                        centerMapOnMyLocation();
                    }
                }
//            if(intent.getExtras().getBoolean(ConnectivityManager.EXTRA_NO_CONNECTIVITY,Boolean.FALSE)) {
//                Log.d("app","There's no network connectivity");
//            }
            }
        };

        if (WebUtils.isOnline(getActivity())) {

            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint(TaxiApi.MAIN_URL)
                    .build();
            TaxiApi service = restAdapter.create(TaxiApi.class);

            String phone = PreferenceUtils.getCurrentUserPhone(getActivity());
            String id = PreferenceUtils.getDeviceId(getActivity());
            String hash = PreferenceUtils.getCurrentUserHash(getActivity());


            service.getAllDrivers(phone, id, hash, new Callback<Response>() {
                @Override
                public void success(Response s, Response response) {
                    String message = "Статут заказа не известен";
                    try {
                        message = WebUtils.getResponseString(s);
                        Log.d("ORDER", message);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    try {
                        Gson gson = new Gson();
                        String str = WebUtils.getResponseString(s);
                        AllDriversRequest drivers = gson.fromJson(str, AllDriversRequest.class);

                        for (AllDriversRequest.ArrayTypes.Driver d : drivers.d.drivers) {
                            String[] geo_data = d.geo.split(",");
                            d.latitude = Double.valueOf(geo_data[0]);
                            d.longitude = Double.valueOf(geo_data[1]);

                            Marker marker = googleMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(d.latitude, d.longitude))
                                    .title(d.name));

                        }


//                        DialogMessageAndTitle messageAndTitle = new DialogMessageAndTitle(message, "водители");
//                        messageAndTitle.show(getChildFragmentManager(), "");
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity(), "Result = " + message, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    Toast.makeText(getActivity().getApplicationContext(), "ОШИБКА С СЕРВЕРА = " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }


    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
        try {
            getActivity().unregisterReceiver(receiver);
        } catch (Exception e) {

        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }


    public class AllDriversRequest {
        public int c;
        public ArrayTypes d;


        public class ArrayTypes {
            ArrayList<Driver> drivers;

            public class Driver {
                int id;
                String name;
                String surname;
                String geo;
                long geo_time;
                String auto;

                double latitude;
                double longitude;
            }
        }

    }
}