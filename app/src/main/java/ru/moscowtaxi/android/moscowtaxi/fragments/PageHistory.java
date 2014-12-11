package ru.moscowtaxi.android.moscowtaxi.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import ru.moscowtaxi.android.moscowtaxi.R;
import ru.moscowtaxi.android.moscowtaxi.helpers.adapters.HistoryListViewAdapter;
import ru.moscowtaxi.android.moscowtaxi.orm.OrderORM;

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
        View rootView = inflater.inflate(R.layout.page_history, container, true);
        listView  = (ListView) rootView.findViewById(R.id.list_history);
        adapter = new HistoryListViewAdapter(getActivity().getLayoutInflater(),new ArrayList<OrderORM>());
        listView.setAdapter(adapter);

        return rootView;
    }
}
