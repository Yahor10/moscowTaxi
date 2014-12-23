package ru.moscowtaxi.android.moscowtaxi.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v13.app.FragmentTabHost;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
public class PageFavorite extends Fragment implements View.OnClickListener{

    public int positionOfViewPager;
    public FavoriteFragmentViewPagerAdapter pagerAdapter;
    public ViewPager mViewPager;

    TextView txt_tab_favorites_place;
    TextView txt_tab_favorites_route;
    View tab_indicator_favorites_place;
    View tab_indicator_favorites_route;

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
        View rootView = inflater.inflate(R.layout.page_favorite, container, false);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        pagerAdapter = new FavoriteFragmentViewPagerAdapter(getChildFragmentManager(), getActivity().getApplicationContext());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) rootView.findViewById(R.id.pager);
        mViewPager.setAdapter(pagerAdapter);

        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                updateTabs(position);
            }
        });

        txt_tab_favorites_place = (TextView) rootView.findViewById(R.id.txt_tab_favorite_pace);
        txt_tab_favorites_route = (TextView) rootView.findViewById(R.id.txt_tab_favorites_route);

        tab_indicator_favorites_place = (View) rootView.findViewById(R.id.rel_tab_indicator_favorites_place);
        tab_indicator_favorites_route = (View) rootView.findViewById(R.id.rel_tab_indicator_favorites_route);


        rootView.findViewById(R.id.view_but_tab_favorite_place).setOnClickListener(this);
        rootView.findViewById(R.id.view_but_tab_favorites_route).setOnClickListener(this);

        updateTabs(positionOfViewPager);

        return rootView;
    }



    private void updateTabs(int currentTab) {

        tab_indicator_favorites_place.setVisibility(View.GONE);
        tab_indicator_favorites_route.setVisibility(View.GONE);
        txt_tab_favorites_place.setTextColor(getActivity().getResources().getColor(R.color.gray_text));
        txt_tab_favorites_route.setTextColor(getActivity().getResources().getColor(R.color.gray_text));
        switch (currentTab) {
            case 0:
                tab_indicator_favorites_place.setVisibility(View.VISIBLE);
                txt_tab_favorites_place.setTextColor(getActivity().getResources().getColor(R.color.orange_color));
                break;
            case 1:
                tab_indicator_favorites_route.setVisibility(View.VISIBLE);
                txt_tab_favorites_route.setTextColor(getActivity().getResources().getColor(R.color.orange_color));
                break;

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.view_but_tab_favorite_place:
                updateTabs(0);
                mViewPager.setCurrentItem(0, true);
                break;
            case R.id.view_but_tab_favorites_route:
                updateTabs(1);
                mViewPager.setCurrentItem(1, true);
                break;

        }


    }

    @Override
    public void onResume() {
        super.onResume();
        mViewPager.setCurrentItem(positionOfViewPager, true);
    }
}
