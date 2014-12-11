package ru.moscowtaxi.android.moscowtaxi.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.moscowtaxi.android.moscowtaxi.R;
import ru.moscowtaxi.android.moscowtaxi.helpers.adapters.Gift_HistoryFragmentViewPagetAdapter;

/**
 * Created by alex-pers on 12/9/14.
 */
public class PageGift_History extends Fragment {

    public int positionOfViewPager;
    public Gift_HistoryFragmentViewPagetAdapter pagerAdapter;
    ViewPager mViewPager;

    public PageGift_History() {
    }

    public static PageGift_History newInstance(int sectionNumber) {
        PageGift_History fragment = new PageGift_History();
        fragment.positionOfViewPager = sectionNumber;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.page_gift_history, container, false);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        pagerAdapter = new Gift_HistoryFragmentViewPagetAdapter(getActivity().getFragmentManager(), getActivity().getApplicationContext());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) rootView.findViewById(R.id.pager);
        mViewPager.setAdapter(pagerAdapter);
        mViewPager.setOffscreenPageLimit(2);


        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {

            }
        });

        return rootView;
    }
}
