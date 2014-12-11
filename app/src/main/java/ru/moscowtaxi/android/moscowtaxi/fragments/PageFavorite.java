package ru.moscowtaxi.android.moscowtaxi.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v13.app.FragmentTabHost;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TextView;

import ru.moscowtaxi.android.moscowtaxi.R;
import ru.moscowtaxi.android.moscowtaxi.helpers.TabHostUtils;
import ru.moscowtaxi.android.moscowtaxi.helpers.adapters.FavoriteFragmentViewPagerAdapter;
import ru.moscowtaxi.android.moscowtaxi.helpers.adapters.MainFragmentViewPagerAdapter;

/**
 * Created by alex-pers on 12/1/14.
 */
public class PageFavorite extends Fragment {

    public int positionOfViewPager;
    public FavoriteFragmentViewPagerAdapter pagerAdapter;
    ViewPager mViewPager;

    public PageFavorite() {
    }

    public static PageFavorite newInstance(int sectionNumber) {
        PageFavorite fragment = new PageFavorite();
        fragment.positionOfViewPager = sectionNumber;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.page_main, container, false);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        pagerAdapter = new FavoriteFragmentViewPagerAdapter(getActivity().getFragmentManager(), getActivity().getApplicationContext());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) rootView.findViewById(R.id.pager);
        mViewPager.setAdapter(pagerAdapter);

        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {

            }
        });

        return rootView;
    }
}
