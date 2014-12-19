package ru.moscowtaxi.android.moscowtaxi.loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Loader;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import ru.moscowtaxi.android.moscowtaxi.orm.FavoritePlaceORM;

/**
 * Created by ychabatarou on 19.12.2014.
 */
public class FavoritePlaceLoader extends AsyncTaskLoader<List<FavoritePlaceORM>> {
    /**
     * Stores away the application context associated with context.
     * Since Loaders can be used across multiple activities it's dangerous to
     * store the context directly; always use {@link #getContext()} to retrieve
     * the Loader's Context, don't use the constructor argument directly.
     * The Context returned by {@link #getContext} is safe to use across
     * Activity instances.
     *
     * @param context used to retrieve the application context.
     */
    public FavoritePlaceLoader(Context context) {
        super(context);
    }


    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    protected void onForceLoad() {
        super.onForceLoad();
    }

    @Override
    public List<FavoritePlaceORM> loadInBackground() {
        List<FavoritePlaceORM> data = FavoritePlaceORM.getFavoritesPlaces(getContext());


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



        return data;
    }
}
