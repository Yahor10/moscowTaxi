package ru.moscowtaxi.android.moscowtaxi.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import ru.moscowtaxi.android.moscowtaxi.R;
import ru.moscowtaxi.android.moscowtaxi.enumeration.OrderType;
import ru.moscowtaxi.android.moscowtaxi.helpers.adapters.HistoryListViewAdapter;
import ru.moscowtaxi.android.moscowtaxi.orm.OrderHistoryORM;
import ru.moscowtaxi.android.moscowtaxi.preferences.PreferenceUtils;


/**
 * Created by alex-pers on 12/10/14.
 */
public class PageHistory extends Fragment {

    ListView listView;
    HistoryListViewAdapter adapter;

    public static Fragment newInstance() {
        PageHistory fragment = new PageHistory();
        return fragment;
    }

    public PageHistory() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.page_history, container, false);
        listView  = (ListView) rootView.findViewById(R.id.list_history);
        Activity activity = getActivity();

        String currentUser = PreferenceUtils.getCurrentUser(activity);
        OrderHistoryORM order = new OrderHistoryORM(currentUser,"name","address","address",0, OrderType.Business);
        OrderHistoryORM.insertOrderHistory(getActivity(), order);
        List<OrderHistoryORM> historyOrders = OrderHistoryORM.getHistoryOrders(activity);
        Log.v(null,"HISTORY ORDERS SIZE" + historyOrders.size());

        adapter = new HistoryListViewAdapter(activity.getLayoutInflater(), historyOrders);
        listView.setAdapter(adapter);

        return rootView;
    }
}
