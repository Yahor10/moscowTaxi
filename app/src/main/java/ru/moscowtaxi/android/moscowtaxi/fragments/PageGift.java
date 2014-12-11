package ru.moscowtaxi.android.moscowtaxi.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.moscowtaxi.android.moscowtaxi.R;

/**
 * Created by alex-pers on 12/10/14.
 */
public class PageGift extends Fragment {

    public static Fragment newInstance() {
        PageGift fragment = new PageGift();
        return fragment;
    }

    public PageGift(){

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.page_gift, container, false);

        return rootView;
    }
}
