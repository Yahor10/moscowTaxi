package ru.moscowtaxi.android.moscowtaxi.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import ru.moscowtaxi.android.moscowtaxi.R;

/**
 * Created by alex-pers on 11/30/14.
 */
public class PageFollow extends Fragment implements View.OnTouchListener{

    static final float ZOOM_MAP_WITH_LAYOUT = 11;
    static final float ZOOM_MAP_WITHOUT_LAYOUT = 13;
    static final int DURATION_ZOOM_ANIMATION = 2000;
    static final float CHANGES_DISTANCE_FOR_RELOADING_NEW_DATA = 500;


    public float fingerDownPoint_Y = 0;

    MapView mMapView;
    private GoogleMap googleMap;

    View mainLayout;
    View viewLevel;
    View viewLevelPoint;
    View viewBetweenLMainAndMap;
    Button butCallLayout;


    public PageFollow() {

    }

    public static Fragment newInstance() {
        PageFollow fragment = new PageFollow();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.page_follow, container, false);

        mainLayout = (View) rootView.findViewById(R.id.layot_main);
        viewBetweenLMainAndMap = (View) rootView.findViewById(R.id.view_on_map);
        viewLevel = (View)rootView.findViewById(R.id.view_level);
        viewLevelPoint = (View)rootView.findViewById(R.id.view_level_point);
        butCallLayout = (Button) rootView.findViewById(R.id.but_call_layout);
        mainLayout.setOnTouchListener(this);

        butCallLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLayout();
            }
        });


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
        double latitude = 17.385044;
        double longitude = 78.486671;

        // create marker
        MarkerOptions marker = new MarkerOptions().position(
                new LatLng(latitude, longitude)).title("Hello Maps");

        // Changing marker icon
        marker.icon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_ROSE));

        // adding marker
        googleMap.addMarker(marker);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(17.385044, 78.486671)).zoom(12).build();
        googleMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));

        return rootView;
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


    private void hideLayout() {
        butCallLayout.setVisibility(View.VISIBLE);
        mainLayout.setVisibility(View.GONE);
        zoomMap(ZOOM_MAP_WITHOUT_LAYOUT);
    }

    private void showLayout() {
        butCallLayout.setVisibility(View.GONE);
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
                int[] pos1 = { 0, 0 };
                int[] pos2 = { 0, 0 };

                viewLevel.getLocationOnScreen(pos1);
                viewLevelPoint.getLocationOnScreen(pos2);
                if (pos2[1] < pos1[1]) {
                    hideLayout();
                }


                break;

            case MotionEvent.ACTION_MOVE:
                float delta = (fingerDownPoint_Y - motionEvent.getY()  );
                if(delta>0){
                    setMargins(mainLayout,0,-(int)delta,0,0);
                }

                break;

            default:
                break;
        }

        return true;
    }
}
