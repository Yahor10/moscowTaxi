package ru.moscowtaxi.android.moscowtaxi.helpers.adapters;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.support.v13.app.FragmentPagerAdapter;

import java.util.Locale;

import ru.moscowtaxi.android.moscowtaxi.R;
import ru.moscowtaxi.android.moscowtaxi.fragments.PageFollow;
import ru.moscowtaxi.android.moscowtaxi.fragments.PageMap;
import ru.moscowtaxi.android.moscowtaxi.fragments.PageOrder;

/**
 * Created by alex-pers on 11/30/14.
 */
public class MainFragmentViewPagerAdapter extends FragmentPagerAdapter {
    Context context;

    public MainFragmentViewPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return PageOrder.newInstance();

            case 1:
                return PageFollow.newInstance();
            case 2:
                return PageMap.newInstance();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Locale l = Locale.getDefault();
        switch (position) {
            case 0:
                return context.getString(R.string.order).toUpperCase(l);
            case 1:
                return context.getString(R.string.follow).toUpperCase(l);
            case 2:
                return context.getString(R.string.map).toUpperCase(l);
        }
        return null;
    }
}
