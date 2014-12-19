package ru.moscowtaxi.android.moscowtaxi.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
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
import ru.moscowtaxi.android.moscowtaxi.orm.FavoritePlaceORM;
import ru.moscowtaxi.android.moscowtaxi.preferences.PreferenceUtils;

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

        listView.setItemsCanFocus(true);
        listView.setFocusable(false);
        listView.setFocusableInTouchMode(false);
        listView.setClickable(false);

        View footerView =  ((LayoutInflater)getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.footer_list_favorite, null, false);
        listView.addFooterView(footerView);

        footerView.findViewById(R.id.but_new_place).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<FavoritePlaceORM> items =  adapter.getItems();
                if(items==null){
                    items = new ArrayList<FavoritePlaceORM>();
                }

                FavoritePlaceORM item1  = new FavoritePlaceORM();
                item1.name =  "Название";
                item1.address = "Адресс";
                item1.is_edited_now = false;

                items.add(item1);
                adapter.setItems(items);
                adapter.notifyDataSetChanged();

            }
        });

        ArrayList<FavoritePlaceORM> data = new ArrayList<FavoritePlaceORM>();

       FavoritePlaceORM item1  = new FavoritePlaceORM();
        item1.name =  "Работа";
        item1.address = "Minsk ave.Nezavisimosti";
        item1.is_edited_now = false;

        FavoritePlaceORM item2  = new FavoritePlaceORM();
        item2.name =  "Дом";
        item2.address = "Гродненская обл. Кореличский р-н. д.Цирин";
        item2.is_edited_now = false;

        data.add(item1);
        data.add(item2);

        adapter = new FavoritesPlacesListAdapter(getActivity().getLayoutInflater(), data);

        listView.setAdapter(adapter);

        return rootView;
    }
}
