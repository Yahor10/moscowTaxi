package ru.moscowtaxi.android.moscowtaxi.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v13.app.FragmentTabHost;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;

import ru.moscowtaxi.android.moscowtaxi.R;
import ru.moscowtaxi.android.moscowtaxi.helpers.TabHostUtils;
import ru.moscowtaxi.android.moscowtaxi.helpers.adapters.Gift_HistoryFragmentViewPagetAdapter;

/**
 * Created by alex-pers on 12/9/14.
 */
public class PageGift_HIstory extends Fragment {

    public int positionOfViewPager;
    public Gift_HistoryFragmentViewPagetAdapter pagerAdapter;
    ViewPager mViewPager;
    private FragmentTabHost mTabHost;

    public PageGift_HIstory() {
    }

    public static PageGift_HIstory newInstance(int sectionNumber) {
        PageGift_HIstory fragment = new PageGift_HIstory();
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

        mTabHost = (FragmentTabHost) rootView.findViewById(R.id.tabhost);
        mTabHost.setup(getActivity(), getActivity().getFragmentManager(), R.id.tabFrameLayout);
        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {

            @Override
            public void onTabChanged(String tabId) {
                mViewPager.setCurrentItem(Integer.parseInt(tabId));

            }
        });

        TabHostUtils.setupTab("0", pagerAdapter.getPageTitle(0).toString(), mTabHost, PageGift.class);
        TabHostUtils.setupTab("1", pagerAdapter.getPageTitle(1).toString(), mTabHost, PageHistory.class);



        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {

            }
        });

        return rootView;
    }
}
