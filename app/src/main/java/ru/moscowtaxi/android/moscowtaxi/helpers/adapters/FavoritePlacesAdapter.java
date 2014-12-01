package ru.moscowtaxi.android.moscowtaxi.helpers.adapters;

import android.content.Context;

import java.util.List;

import ru.moscowtaxi.android.moscowtaxi.orm.FavoritePlaceORM;

/**
 * Created by ychabatarou on 01.12.2014.
 */
public class FavoritePlacesAdapter extends EntityAdapter<FavoritePlaceORM> {

    public FavoritePlacesAdapter(Context context) {
        super(context);
    }

    @Override
    public List<FavoritePlaceORM> loadData() {
        return null;
    }
    @Override
    public void refreshData() {
        clear();
        List<FavoritePlaceORM> collection = loadData();
        addAll(collection);
    }
}
