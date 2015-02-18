package ru.moscowtaxi.android.moscowtaxi.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ru.moscowtaxi.android.moscowtaxi.R;
import ru.moscowtaxi.android.moscowtaxi.helpers.adapters.MainFragmentViewPagerAdapter;

/**
 * Created by alex-pers on 11/30/14.
 */
public class PageMain extends Fragment implements View.OnClickListener {

    public int positionOfViewPager;
    public MainFragmentViewPagerAdapter pagerAdapter;
    ViewPager mViewPager;


    //    Быдлорешение, но зато никаких багов
    TextView txt_tab_order;
    TextView txt_tab_follow;
    TextView txt_tab_map;
    View tab_indicator_order;
    View tab_indicator_follow;
    View tab_indicator_map;


    public PageMain() {
    }

    public static PageMain newInstance(int sectionNumber) {
        PageMain fragment = new PageMain();
        fragment.positionOfViewPager = sectionNumber;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.page_main, container, false);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        pagerAdapter = new MainFragmentViewPagerAdapter(this.getChildFragmentManager(), getActivity().getApplicationContext());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) rootView.findViewById(R.id.pager);
        mViewPager.setAdapter(pagerAdapter);
        mViewPager.setOffscreenPageLimit(3);


        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                updateTabs(position);
            }
        });


        txt_tab_order = (TextView) rootView.findViewById(R.id.txt_tab_order);
        txt_tab_follow = (TextView) rootView.findViewById(R.id.txt_tab_follow);
        txt_tab_map = (TextView) rootView.findViewById(R.id.txt_tab_map);

        tab_indicator_order = (View) rootView.findViewById(R.id.rel_tab_indicator_order);
        tab_indicator_follow = (View) rootView.findViewById(R.id.rel_tab_indicator_follow);
        tab_indicator_map = (View) rootView.findViewById(R.id.rel_tab_indicator_map);


        rootView.findViewById(R.id.view_but_tab_order).setOnClickListener(this);
        rootView.findViewById(R.id.view_but_tab_follow).setOnClickListener(this);
        rootView.findViewById(R.id.view_but_tab_map).setOnClickListener(this);

        updateTabs(positionOfViewPager);

        return rootView;
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.view_but_tab_order:
                updateTabs(0);
                mViewPager.setCurrentItem(0, true);
                break;
            case R.id.view_but_tab_follow:
                updateTabs(1);
                mViewPager.setCurrentItem(1, true);
                break;
            case R.id.view_but_tab_map:
                updateTabs(2);
                mViewPager.setCurrentItem(2, true);
                break;
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        setCurrentItem(positionOfViewPager);
    }

    public void setCurrentItem(int position) {
        positionOfViewPager= position;
        mViewPager.setCurrentItem(position, true);
    }

    private void updateTabs(int currentTab) {

        tab_indicator_order.setVisibility(View.GONE);
        tab_indicator_follow.setVisibility(View.GONE);
        tab_indicator_map.setVisibility(View.GONE);
        txt_tab_order.setTextColor(getActivity().getResources().getColor(R.color.gray_text));
        txt_tab_follow.setTextColor(getActivity().getResources().getColor(R.color.gray_text));
        txt_tab_map.setTextColor(getActivity().getResources().getColor(R.color.gray_text));
        switch (currentTab) {
            case 0:
                tab_indicator_order.setVisibility(View.VISIBLE);
                txt_tab_order.setTextColor(getActivity().getResources().getColor(R.color.orange_color));
                break;
            case 1:
                tab_indicator_follow.setVisibility(View.VISIBLE);
                txt_tab_follow.setTextColor(getActivity().getResources().getColor(R.color.orange_color));
                break;
            case 2:
                tab_indicator_map.setVisibility(View.VISIBLE);
                txt_tab_map.setTextColor(getActivity().getResources().getColor(R.color.orange_color));
                break;
        }
    }
}
