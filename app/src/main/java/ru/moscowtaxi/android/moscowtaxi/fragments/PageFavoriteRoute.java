package ru.moscowtaxi.android.moscowtaxi.fragments;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import ru.moscowtaxi.android.moscowtaxi.R;
import ru.moscowtaxi.android.moscowtaxi.helpers.adapters.FavoriteRouteListAdapter;
import ru.moscowtaxi.android.moscowtaxi.helpers.adapters.HistoryListViewAdapter;
import ru.moscowtaxi.android.moscowtaxi.loaders.FavoriteRouteLoader;
import ru.moscowtaxi.android.moscowtaxi.orm.FavoriteRouteORM;
import ru.moscowtaxi.android.moscowtaxi.orm.OrderORM;

/**
 * Created by alex-pers on 12/2/14.
 */
public class PageFavoriteRoute extends Fragment implements LoaderManager.LoaderCallbacks<List<FavoriteRouteORM>> {


    private static final int LOADER_ID = 102;
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


        getLoaderManager().initLoader(LOADER_ID,null,this);
        return rootView;
    }

    @Override
    public Loader<List<FavoriteRouteORM>> onCreateLoader(int id, Bundle args) {
        return new FavoriteRouteLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<List<FavoriteRouteORM>> loader, List<FavoriteRouteORM> data) {
        adapter = new FavoriteRouteListAdapter(getActivity().getLayoutInflater(),data);
        listView.setAdapter(adapter);
    }

    @Override
    public void onLoaderReset(Loader<List<FavoriteRouteORM>> loader) {

    }
}
