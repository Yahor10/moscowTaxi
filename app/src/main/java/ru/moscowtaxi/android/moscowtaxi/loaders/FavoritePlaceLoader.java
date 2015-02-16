package ru.moscowtaxi.android.moscowtaxi.loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;

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


        return FavoritePlaceORM.getFavoritesPlaces(getContext());
    }
}
