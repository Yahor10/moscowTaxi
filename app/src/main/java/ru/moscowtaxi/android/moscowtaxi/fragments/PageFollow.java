package ru.moscowtaxi.android.moscowtaxi.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.MapFragment;

import ru.moscowtaxi.android.moscowtaxi.R;

/**
 * Created by alex-pers on 11/30/14.
 */
public class PageFollow extends MapFragment {


    public PageFollow() {

    }

    public static MapFragment newInstance() {
        PageFollow fragment = new PageFollow();
        return fragment;
    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        View rootView = inflater.inflate(R.layout.page_follow, container, false);
//        return rootView;
//    }
}
