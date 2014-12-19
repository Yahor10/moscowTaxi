package ru.moscowtaxi.android.moscowtaxi.loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

import ru.moscowtaxi.android.moscowtaxi.orm.FavoriteRouteORM;

/**
 * Created by ychabatarou on 19.12.2014.
 */
public class FavoriteRouteLoader extends AsyncTaskLoader<List<FavoriteRouteORM>> {

    public FavoriteRouteLoader(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public List<FavoriteRouteORM> loadInBackground() {
        List<FavoriteRouteORM> favoriteRoutes = FavoriteRouteORM.getFavoriteRoutes(getContext());
        return favoriteRoutes;
    }
}
