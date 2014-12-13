package ru.moscowtaxi.android.moscowtaxi.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import ru.moscowtaxi.android.moscowtaxi.R;
import ru.moscowtaxi.android.moscowtaxi.helpers.adapters.FavoriteRouteListAdapter;
import ru.moscowtaxi.android.moscowtaxi.helpers.adapters.HistoryListViewAdapter;
import ru.moscowtaxi.android.moscowtaxi.orm.OrderORM;

/**
 * Created by alex-pers on 12/2/14.
 */
public class PageFavoriteRoute extends Fragment {


    ListView listView;
    FavoriteRouteListAdapter adapter;

    public static PageFavoriteRoute newInstance() {
        PageFavoriteRoute fragment = new PageFavoriteRoute();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.page_favorite_route, container, false);
        listView = (ListView) rootView.findViewById(R.id.list_favorite_route);
        adapter = new FavoriteRouteListAdapter(getActivity().getLayoutInflater(), new ArrayList<OrderORM>());
        listView.setAdapter(adapter);

        return rootView;
    }
}
