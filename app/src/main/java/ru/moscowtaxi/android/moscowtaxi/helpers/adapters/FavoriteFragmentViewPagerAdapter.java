package ru.moscowtaxi.android.moscowtaxi.helpers.adapters;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.support.v13.app.FragmentPagerAdapter;

import java.util.Locale;

import ru.moscowtaxi.android.moscowtaxi.R;
import ru.moscowtaxi.android.moscowtaxi.fragments.PageFavoritePlace;
import ru.moscowtaxi.android.moscowtaxi.fragments.PageFavoriteRoute;
import ru.moscowtaxi.android.moscowtaxi.fragments.PageFollow;
import ru.moscowtaxi.android.moscowtaxi.fragments.PageMap;
import ru.moscowtaxi.android.moscowtaxi.fragments.PageOrder;

/**
 * Created by alex-pers on 12/2/14.
 */
public class FavoriteFragmentViewPagerAdapter extends FragmentPagerAdapter {
    Context context;

    public FavoriteFragmentViewPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return PageFavoritePlace.newInstance();

            case 1:
                return PageFavoriteRoute.newInstance();

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Locale l = Locale.getDefault();
        switch (position) {
            case 0:
                return context.getString(R.string.favorites_places).toUpperCase(l);
            case 1:
                return context.getString(R.string.favorites_route).toUpperCase(l);

        }
        return null;
    }
}

