package ru.moscowtaxi.android.moscowtaxi.fragments;

import android.app.ActionBar;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ru.moscowtaxi.android.moscowtaxi.R;
import ru.moscowtaxi.android.moscowtaxi.helpers.adapters.Reward_HistoryFragmentViewPagetAdapter;

/**
 * Created by alex-pers on 12/9/14.
 */
public class PageReward_History extends Fragment implements View.OnClickListener {

    public int positionOfViewPager;
    public Reward_HistoryFragmentViewPagetAdapter pagerAdapter;
    public ViewPager mViewPager;

    TextView txt_tab_gifts;
    TextView txt_tab_history;
    View tab_indicator_gifts;
    View tab_indicator_history;

    public PageReward_History() {
    }

    public static PageReward_History newInstance(int sectionNumber) {
        PageReward_History fragment = new PageReward_History();
        fragment.positionOfViewPager = sectionNumber;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.page_gift_history, container, false);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        pagerAdapter = new Reward_HistoryFragmentViewPagetAdapter(getChildFragmentManager(), getActivity().getApplicationContext());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) rootView.findViewById(R.id.pager);

        mViewPager.setAdapter(pagerAdapter);
        mViewPager.setOffscreenPageLimit(2);


        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                updateTabs(position);
            }
        });

        txt_tab_gifts = (TextView) rootView.findViewById(R.id.txt_tab_gifts);
        txt_tab_history = (TextView) rootView.findViewById(R.id.txt_tab_history);

        tab_indicator_gifts = (View) rootView.findViewById(R.id.rel_tab_indicator_gifts);
        tab_indicator_history = (View) rootView.findViewById(R.id.rel_tab_indicator_history);


        rootView.findViewById(R.id.view_but_tab_gifts).setOnClickListener(this);
        rootView.findViewById(R.id.view_but_tab_history).setOnClickListener(this);

        updateTabs(positionOfViewPager);



        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mViewPager.setCurrentItem(positionOfViewPager, true);
        pagerAdapter.notifyDataSetChanged();
    }

    private void updateTabs(int currentTab) {

        tab_indicator_gifts.setVisibility(View.GONE);
        tab_indicator_history.setVisibility(View.GONE);
        txt_tab_gifts.setTextColor(getActivity().getResources().getColor(R.color.gray_text));
        txt_tab_history.setTextColor(getActivity().getResources().getColor(R.color.gray_text));
        switch (currentTab) {
            case 0:
                tab_indicator_gifts.setVisibility(View.VISIBLE);
                txt_tab_gifts.setTextColor(getActivity().getResources().getColor(R.color.orange_color));
                break;
            case 1:
                tab_indicator_history.setVisibility(View.VISIBLE);
                txt_tab_history.setTextColor(getActivity().getResources().getColor(R.color.orange_color));
                break;

        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.view_but_tab_gifts:
                updateTabs(0);
                mViewPager.setCurrentItem(0, true);
                break;
            case R.id.view_but_tab_history:
                updateTabs(1);
                mViewPager.setCurrentItem(1, true);
                break;

        }

    }
}
