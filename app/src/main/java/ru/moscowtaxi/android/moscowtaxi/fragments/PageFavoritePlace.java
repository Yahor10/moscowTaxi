package ru.moscowtaxi.android.moscowtaxi.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import ru.moscowtaxi.android.moscowtaxi.R;
import ru.moscowtaxi.android.moscowtaxi.helpers.adapters.FavoritesPlacesListAdapter;
import ru.moscowtaxi.android.moscowtaxi.loaders.FavoritePlaceLoader;
import ru.moscowtaxi.android.moscowtaxi.orm.FavoritePlaceORM;
import ru.moscowtaxi.android.moscowtaxi.preferences.PreferenceUtils;

/**
 * Created by alex-pers on 12/2/14.
 */
public class PageFavoritePlace extends Fragment  implements LoaderManager.LoaderCallbacks<List<FavoritePlaceORM>>{

    ListView listView;
    FavoritesPlacesListAdapter adapter;

    private static final int LOADER_ID = 101;
    public static PageFavoritePlace newInstance() {
        PageFavoritePlace fragment = new PageFavoritePlace();
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.page_favorite_place, container, false);
        listView = (ListView) rootView.findViewById(R.id.list_favorite_place);

        listView.setItemsCanFocus(true);
        listView.setFocusable(false);
        listView.setFocusableInTouchMode(false);
        listView.setClickable(false);

        getLoaderManager().initLoader(LOADER_ID,null,this);
        return rootView;
    }


    @Override
    public Loader<List<FavoritePlaceORM>> onCreateLoader(int id, Bundle args) {
        return new FavoritePlaceLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<List<FavoritePlaceORM>> loader, List<FavoritePlaceORM> data) {
        // TODO check null adapters
        adapter = new FavoritesPlacesListAdapter(getActivity().getLayoutInflater(), data);
        listView.setAdapter(adapter);
    }

    @Override
    public void onLoaderReset(Loader<List<FavoritePlaceORM>> loader) {

    }
}
