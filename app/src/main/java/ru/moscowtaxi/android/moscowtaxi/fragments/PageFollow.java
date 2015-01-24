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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import ru.moscowtaxi.android.moscowtaxi.R;
import ru.moscowtaxi.android.moscowtaxi.dialogs.DialogMessageAndTitle;
import ru.moscowtaxi.android.moscowtaxi.helpers.WebUtils;
import ru.moscowtaxi.android.moscowtaxi.helpers.http.TaxiApi;
import ru.moscowtaxi.android.moscowtaxi.preferences.PreferenceUtils;

/**
 * Created by alex-pers on 11/30/14.
 */
public class PageFollow extends Fragment implements View.OnTouchListener, View.OnClickListener {

    static final float ZOOM_MAP_WITH_LAYOUT = 11;
    static final float ZOOM_MAP_WITHOUT_LAYOUT = 13;
    static final int DURATION_ZOOM_ANIMATION = 2000;
    static final float CHANGES_DISTANCE_FOR_RELOADING_NEW_DATA = 500;


    private BroadcastReceiver receiver;


    public float fingerDownPoint_Y = 0;

    MapView mMapView;
    View mainLayout;
    View viewLevel;
    View viewLevelPoint;
    View viewBetweenLMainAndMap;
    Button butCallLayout;
    EditText edtTaxiId;
    private GoogleMap googleMap;
    private GoogleMap.OnMyLocationChangeListener myLocationChangeListener = new GoogleMap.OnMyLocationChangeListener() {
        @Override
        public void onMyLocationChange(Location location) {
            LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
//            mMarker = googleMap.addMarker(new MarkerOptions().position(loc));
            if (googleMap != null) {
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, ZOOM_MAP_WITHOUT_LAYOUT));
            }
        }
    };

    public PageFollow() {

    }

    public static Fragment newInstance() {
        PageFollow fragment = new PageFollow();
        return fragment;
    }

    public static void setMargins(View v, int l, int t, int r, int b) {
        // if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
        // ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v
        // .getLayoutParams();
        // p.setMargins(l, t, r, b);
        v.setPadding(l, t, r, b);
        v.requestLayout();
        // }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.page_follow, container, false);

        mainLayout = (View) rootView.findViewById(R.id.layot_main);
        viewBetweenLMainAndMap = (View) rootView.findViewById(R.id.view_on_map);
        viewLevel = (View) rootView.findViewById(R.id.view_level);
        viewLevelPoint = (View) rootView.findViewById(R.id.view_level_point);
        edtTaxiId = (EditText) rootView.findViewById(R.id.edt_taxi_id);
        mainLayout.setOnTouchListener(this);

        rootView.findViewById(R.id.view_but_from_history).setOnClickListener(this);
        rootView.findViewById(R.id.but_follow_by_number).setOnClickListener(this);


        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume();// needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        googleMap = mMapView.getMap();
        // latitude and longitude
//        double latitude = 17.385044;
//        double longitude = 78.486671;
//
//        // create marker
//        MarkerOptions marker = new MarkerOptions().position(
//                new LatLng(latitude, longitude)).title("Hello Maps");
//
//        // Changing marker icon
//        marker.icon(BitmapDescriptorFactory
//                .defaultMarker(BitmapDescriptorFactory.HUE_ROSE));
//
//        // adding marker
//        googleMap.addMarker(marker);
//        CameraPosition cameraPosition = new CameraPosition.Builder()
//                .target(new LatLng(17.385044, 78.486671)).zoom(12).build();
//        googleMap.animateCamera(CameraUpdateFactory
//                .newCameraPosition(cameraPosition));

//        googleMap.setOnMyLocationChangeListener(myLocationChangeListener);
        centerMapOnMyLocation();

        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");

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
        getActivity().registerReceiver(receiver, filter);


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
                    .zoom(ZOOM_MAP_WITHOUT_LAYOUT)                   // Sets the zoom
                    .bearing(90)                // Sets the orientation of the camera to east
                    .build();                   // Creates a CameraPosition from the builder
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        }
    }

    private void hideLayout() {
//        butCallLayout.setVisibility(View.VISIBLE);
        mainLayout.setVisibility(View.GONE);
        zoomMap(ZOOM_MAP_WITHOUT_LAYOUT);
    }

    private void showLayout() {
//        butCallLayout.setVisibility(View.GONE);
        mainLayout.setVisibility(View.VISIBLE);
        zoomMap(ZOOM_MAP_WITH_LAYOUT);
    }

    private void zoomMap(float zoom) {
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(zoom));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(zoom),
                DURATION_ZOOM_ANIMATION, null);

    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
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

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        switch (motionEvent.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                fingerDownPoint_Y = motionEvent.getY();

                break;

            case MotionEvent.ACTION_UP:

                setMargins(mainLayout, 0, 0, 0, 0);
                int[] pos1 = {0, 0};
                int[] pos2 = {0, 0};

                viewLevel.getLocationOnScreen(pos1);
                viewLevelPoint.getLocationOnScreen(pos2);
                if (pos2[1] < pos1[1]) {
                    hideLayout();
                }


                break;

            case MotionEvent.ACTION_MOVE:
                float delta = (fingerDownPoint_Y - motionEvent.getY());
                if (delta > 0) {
                    setMargins(mainLayout, 0, -(int) delta, 0, 0);
                }

                break;

            default:
                break;
        }

        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.view_but_from_history:
//                ((MainActivity)getActivity()).onNavigationDrawerItemSelected(3);
                break;
            case R.id.but_follow_by_number:
                if (edtTaxiId.getText().length() < 1) {
                    edtTaxiId.setError("Слишком короткий код");
                    return;
                }
                if (WebUtils.isOnline(getActivity())) {
                    final ProgressDialog progressDialog = new ProgressDialog(getActivity());
                    progressDialog.setMessage("Wait ...");
                    progressDialog.show();

                    RestAdapter restAdapter = new RestAdapter.Builder()
                            .setEndpoint(TaxiApi.MAIN_URL)
                            .build();
                    TaxiApi service = restAdapter.create(TaxiApi.class);

                    String phone = PreferenceUtils.getCurrentUserPhone(getActivity());
                    String id = PreferenceUtils.getDeviceId(getActivity());
                    String hash = PreferenceUtils.getCurrentUserHash(getActivity());
                    String id_taxi = edtTaxiId.getText().toString();

                    service.getStatus(phone, id, hash, id_taxi, new Callback<Response>() {
                        @Override
                        public void success(Response s, Response response) {
                            String message = "Статут заказа не известен";
                            try {
                                progressDialog.dismiss();
                                message = WebUtils.getResponseString(s);
                                Log.d("ORDER", message);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            try {
                                int number = Integer.parseInt(message);
                                DialogMessageAndTitle messageAndTitle = new DialogMessageAndTitle("Номер заказа = " + number, "Заказ успешно обработан");
                                messageAndTitle.show(getChildFragmentManager(), "");
                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(getActivity(), "Result = " + message, Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity().getApplicationContext(), "ОШИБКА С СЕРВЕРА = " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                break;
            default:
                break;
        }
    }

}


