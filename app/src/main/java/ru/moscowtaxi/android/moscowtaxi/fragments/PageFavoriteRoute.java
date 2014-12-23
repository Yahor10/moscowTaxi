package ru.moscowtaxi.android.moscowtaxi.fragments;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import ru.moscowtaxi.android.moscowtaxi.R;
import ru.moscowtaxi.android.moscowtaxi.enumeration.OrderType;
import ru.moscowtaxi.android.moscowtaxi.helpers.adapters.FavoriteRouteListAdapter;
import ru.moscowtaxi.android.moscowtaxi.helpers.adapters.HistoryListViewAdapter;
import ru.moscowtaxi.android.moscowtaxi.loaders.FavoriteRouteLoader;
import ru.moscowtaxi.android.moscowtaxi.orm.FavoritePlaceORM;
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


        View footerView = ((LayoutInflater) getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.footer_list_favorite, null, false);
        listView.addFooterView(footerView);


        footerView.findViewById(R.id.but_new_place).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<FavoriteRouteORM> items = adapter.getItems();
                if (items == null) {
                    items = new ArrayList<FavoriteRouteORM>();
                }

                FavoriteRouteORM item1 = new FavoriteRouteORM();
                item1.name = "Название";
                item1.addressFrom = "Адресс откуда";
                item1.addressTo = "Адресс куда";
                item1.time = 123123123;
                item1.type = OrderType.Business;
                item1.is_edited_now = true;

                items.add(item1);
                adapter.setItems(items);
                adapter.notifyDataSetChanged();

            }
        });


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
