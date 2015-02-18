package ru.moscowtaxi.android.moscowtaxi.fragments;

import android.app.Fragment;
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
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.Serializable;

import ru.moscowtaxi.android.moscowtaxi.R;
import ru.moscowtaxi.android.moscowtaxi.activity.FromHistoryActivity;
import ru.moscowtaxi.android.moscowtaxi.activity.MainActivity;
import ru.moscowtaxi.android.moscowtaxi.helpers.WebUtils;
import ru.moscowtaxi.android.moscowtaxi.helpers.services.FollowOrderService;
import ru.moscowtaxi.android.moscowtaxi.orm.OrderHistoryORM;

/**
 * Created by alex-pers on 11/30/14.
 */
public class PageFollow extends Fragment implements View.OnTouchListener, View.OnClickListener {

    public static final String KEY_ORDER_ID = "key_order_id";
    public static final String KEY_RECIEVER_FOLLOW = "key_reciever_follow";
    public static final String KEY_RECIEVER_DATA_FROM_SERVER = "key_reciever_data_from_server";
    static final float ZOOM_MAP_WITH_LAYOUT = 11;
    static final float ZOOM_MAP_WITHOUT_LAYOUT = 13;
    static final int DURATION_ZOOM_ANIMATION = 2000;
    static final float CHANGES_DISTANCE_FOR_RELOADING_NEW_DATA = 500;
    public float fingerDownPoint_Y = 0;
    MapView mMapView;
    View mainLayout;
    View viewLevel;
    View viewLevelPoint;
    View viewBetweenLMainAndMap;
    Button butCallLayout;
    EditText edtTaxiId;
    FollowReciever recieverFollow;
    TextView txtStatus;
    TextView txtFrom;
    TextView txtWhere;
    TextView txtNumberOrder;


    private BroadcastReceiver receiver;
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
        txtStatus = (TextView) rootView.findViewById(R.id.txt_status);
        txtFrom = (TextView) rootView.findViewById(R.id.txt_from_here);
        txtWhere = (TextView) rootView.findViewById(R.id.txt_where);
        txtNumberOrder = (TextView) rootView.findViewById(R.id.txt_number_order);

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
                        try {
                            centerMapOnMyLocation();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
//            if(intent.getExtras().getBoolean(ConnectivityManager.EXTRA_NO_CONNECTIVITY,Boolean.FALSE)) {
//                Log.d("app","There's no network connectivity");
//            }
            }
        };
        try {
            getActivity().registerReceiver(receiver, filter);
        } catch (Exception e) {
            e.printStackTrace();
        }


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
        recieverFollow = new FollowReciever();

        IntentFilter filter = new IntentFilter();
        filter.addAction(KEY_RECIEVER_FOLLOW);

        getActivity().registerReceiver(recieverFollow, filter);

        setDataFromHistory(MainActivity.historyORM);
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();

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
    public void onStop() {
        super.onStop();

        try {
            getActivity().unregisterReceiver(receiver);

        } catch (Exception e) {

        }

        try {
            getActivity().unregisterReceiver(recieverFollow);

        } catch (Exception e) {

        }

        getActivity().stopService(new Intent(getActivity(),
                FollowOrderService.class));
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
                Intent i = new Intent(getActivity(), FromHistoryActivity.class);
                getActivity().startActivityForResult(i, MainActivity.KEY_ACTIVITY_RESULT_FROM_HISTORY);
                break;
            case R.id.but_follow_by_number:
                if (edtTaxiId.getText().length() < 1) {
                    edtTaxiId.setError("Слишком короткий код");
                    return;
                }
                Integer id = 0;
                try {
                    id = Integer.parseInt(edtTaxiId.getText().toString());
                } catch (Exception e) {
                    edtTaxiId.setError("Код не может содержать символы");
                    return;
                }

                if (WebUtils.isOnline(getActivity())) {
                    Intent intent = new Intent(getActivity(),
                            FollowOrderService.class);
                    intent.putExtra(KEY_ORDER_ID, id.intValue());
                    getActivity().startService(intent);
                }
                break;
            default:
                break;
        }
    }

    public void updatePage(FollowRequest followRequest) {
        if (followRequest != null) {

            if (followRequest.c == 1) {

                if (followRequest.d.latitude > 0 && followRequest.d.longitude > 0) {
                    googleMap.clear();
                    Marker marker = googleMap.addMarker(new MarkerOptions()
                            .position(new LatLng(followRequest.d.latitude, followRequest.d.longitude))
                            .title(followRequest.d.driver)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.point_taxi_small)));


                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                            new LatLng(followRequest.d.latitude, followRequest.d.longitude), 15));
                }
                if (followRequest.d.status != null) {
                    if ("complete".equals(followRequest.d.status)) {
                        txtStatus.setText(R.string.order_complete);
                    } else if ("exchange".equals(followRequest.d.status)) {
                        txtStatus.setText(R.string.order_allocation);
                    } else {
                        txtStatus.setText(R.string.order_allocation);
                    }

                }

            } else {
                txtStatus.setText(R.string.order_allocation);
            }

        } else {
            txtStatus.setText(R.string.order_allocation);
        }
    }

    public void setDataFromHistory(OrderHistoryORM history) {

        if (history != null) {
            if (null == edtTaxiId || null == txtWhere || null == txtFrom || null == txtStatus || null == txtNumberOrder)
                return;

            if (!"".equals(history.number)) {
                edtTaxiId.setText("" + history.number);
                txtNumberOrder.setText("№" + history.number);
            }

            txtStatus.setText(R.string.order_complete);
            if (!"".equals(history.addressFrom))
                txtFrom.setText(history.addressFrom);
            if (!"".equals(history.addressTo))
                txtWhere.setText(history.addressTo);


            MainActivity.historyORM = null;
        }
    }

    public class FollowReciever extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("FOLLOW_FRAGMENT", "RECIEVER!!!!");

            FollowRequest followRequest = (FollowRequest) intent.getExtras().getSerializable(KEY_RECIEVER_DATA_FROM_SERVER);
            updatePage(followRequest);


        }
    }

    public class FollowRequest implements Serializable {
        public int c;
        public Data d;

        public class Data implements Serializable {
            public String status;
            public long timestamp;
            public String driver;
            public String driverId;
            public String driverGeo;
            public String driverGeoTime;
            public String driverTel;
            public String feedTime;
            public String auto;
            public String cost;
            public String feed;

            public double latitude;
            public double longitude;


        }
    }


}


