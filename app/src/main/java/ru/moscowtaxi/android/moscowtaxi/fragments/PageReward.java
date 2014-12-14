package ru.moscowtaxi.android.moscowtaxi.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import ru.moscowtaxi.android.moscowtaxi.R;
import ru.moscowtaxi.android.moscowtaxi.helpers.adapters.RewardListAdapter;
import ru.moscowtaxi.android.moscowtaxi.orm.RewardsORM;

/**
 * Created by alex-pers on 12/10/14.
 */
public class PageReward extends Fragment {
    ListView listView;
    RewardListAdapter adapter;

    public PageReward() {

    }

    public static Fragment newInstance() {
        PageReward fragment = new PageReward();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.page_reward, container, false);
        listView = (ListView) rootView.findViewById(R.id.list_reward);
        adapter = new RewardListAdapter(getActivity().getLayoutInflater(), new ArrayList<RewardsORM>());
        listView.setAdapter(adapter);

        return rootView;
    }
}
