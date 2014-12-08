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
import android.widget.Toast;

import ru.moscowtaxi.android.moscowtaxi.R;
import ru.moscowtaxi.android.moscowtaxi.helpers.TabHostUtils;
import ru.moscowtaxi.android.moscowtaxi.helpers.adapters.MainFragmentViewPagerAdapter;

/**
 * Created by alex-pers on 11/30/14.
 */
public class FragmentMain extends Fragment {

    public int positionOfViewPager;
    public MainFragmentViewPagerAdapter pagerAdapter;
    ViewPager mViewPager;
    private FragmentTabHost mTabHost;

    public FragmentMain() {
    }

    public static FragmentMain newInstance(int sectionNumber) {
        FragmentMain fragment = new FragmentMain();
        fragment.positionOfViewPager = sectionNumber;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.page_main, container, false);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        pagerAdapter = new MainFragmentViewPagerAdapter(getActivity().getFragmentManager(), getActivity().getApplicationContext());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) rootView.findViewById(R.id.pager);
        mViewPager.setAdapter(pagerAdapter);

        mTabHost = (FragmentTabHost) rootView.findViewById(R.id.tabhost);
        mTabHost.setup(getActivity(), getActivity().getFragmentManager(), R.id.tabFrameLayout);
        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {

            @Override
            public void onTabChanged(String tabId) {
                mViewPager.setCurrentItem(Integer.parseInt(tabId));

            }
        });

        TabHostUtils.setupTab("0", pagerAdapter.getPageTitle(0).toString(),mTabHost, PageFollow.class);
        TabHostUtils.setupTab("1", pagerAdapter.getPageTitle(1).toString(),mTabHost, PageFollow.class);
        TabHostUtils.setupTab("2", pagerAdapter.getPageTitle(2).toString(),mTabHost, PageFollow.class);

        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {

            }
        });

        return rootView;
    }


}
