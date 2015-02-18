package ru.moscowtaxi.android.moscowtaxi.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import ru.moscowtaxi.android.moscowtaxi.R;
import ru.moscowtaxi.android.moscowtaxi.activity.FromLibraryAddress;
import ru.moscowtaxi.android.moscowtaxi.activity.MainActivity;
import ru.moscowtaxi.android.moscowtaxi.helpers.adapters.FavoritesPlacesListAdapter;
import ru.moscowtaxi.android.moscowtaxi.loaders.FavoritePlaceLoader;
import ru.moscowtaxi.android.moscowtaxi.orm.FavoritePlaceORM;

/**
 * Created by alex-pers on 12/2/14.
 */
public class PageFavoritePlace extends Fragment implements LoaderManager.LoaderCallbacks<List<FavoritePlaceORM>> {

    private static final int LOADER_ID = 101;
    ListView listView;
    FavoritesPlacesListAdapter adapter;
    public boolean flag_edit_text = false;

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


        View footerView = ((LayoutInflater) getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.footer_list_favorite, null, false);
        listView.addFooterView(footerView);


        footerView.findViewById(R.id.but_new_place).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<FavoritePlaceORM> items = adapter.getItems();
                for (FavoritePlaceORM item : items) {
                    item.is_edited_now = false;
                }
                if (items == null) {
                    items = new ArrayList<FavoritePlaceORM>();
                }

                FavoritePlaceORM item1 = new FavoritePlaceORM();
                item1.name = "Название";
                item1.address = "Адресс";
                item1.is_edited_now = true;

                items.add(item1);
                adapter.setItems(items);
                adapter.notifyDataSetChanged();

            }
        });

        if (getActivity() instanceof FromLibraryAddress) {
            Intent intent = getActivity().getIntent();
            if (intent!=null){
                flag_edit_text = intent.getBooleanExtra(MainActivity.KEY_PLAСE_NUMBER,true);
            }
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Activity activity = getActivity();
                if (activity instanceof FromLibraryAddress) {
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(MainActivity.KEY_ACTIVITY_RESULT_FROM_FAVORITE_PLACE_data, adapter.getItem(i));
                    intent.putExtra(MainActivity.KEY_PLAСE_NUMBER,flag_edit_text);
                    intent.putExtras(bundle);
                    activity.setResult(MainActivity.KEY_ACTIVITY_RESULT_FROM_FAVORITE_PLACE, intent);
                    activity.finish();
                }
            }
        });

        ArrayList<FavoritePlaceORM> data = new ArrayList<FavoritePlaceORM>();


        getLoaderManager().initLoader(LOADER_ID, null, this);
        return rootView;
    }


    @Override
    public Loader<List<FavoritePlaceORM>> onCreateLoader(int id, Bundle args) {
        return new FavoritePlaceLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<List<FavoritePlaceORM>> loader, List<FavoritePlaceORM> data) {
        // TODO check null adapters
        adapter = new FavoritesPlacesListAdapter(getActivity(), data);
        listView.setAdapter(adapter);
    }

    @Override
    public void onLoaderReset(Loader<List<FavoritePlaceORM>> loader) {

    }
}
