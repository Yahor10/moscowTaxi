package ru.moscowtaxi.android.moscowtaxi.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import ru.moscowtaxi.android.moscowtaxi.R;
import ru.moscowtaxi.android.moscowtaxi.helpers.adapters.FavoritesPlacesListAdapter;
import ru.moscowtaxi.android.moscowtaxi.orm.FavoritePlaceORM;

/**
 * Created by alex-pers on 12/2/14.
 */
public class PageFavoritePlace extends Fragment {

    ListView listView;
    FavoritesPlacesListAdapter adapter;

    public static PageFavoritePlace newInstance() {
        PageFavoritePlace fragment = new PageFavoritePlace();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.page_favorite_place, container, false);
        listView = (ListView) rootView.findViewById(R.id.list_favorite_place);
        adapter = new FavoritesPlacesListAdapter(getActivity().getLayoutInflater(), new ArrayList<FavoritePlaceORM>());
        listView.setAdapter(adapter);

        return rootView;
    }
}
