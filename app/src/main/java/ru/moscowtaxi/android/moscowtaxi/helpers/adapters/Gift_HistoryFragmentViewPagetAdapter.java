package ru.moscowtaxi.android.moscowtaxi.helpers.adapters;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.support.v13.app.FragmentPagerAdapter;

import java.util.Locale;

import ru.moscowtaxi.android.moscowtaxi.R;
import ru.moscowtaxi.android.moscowtaxi.fragments.PageGift;
import ru.moscowtaxi.android.moscowtaxi.fragments.PageHistory;

/**
 * Created by alex-pers on 12/10/14.
 */
public class Gift_HistoryFragmentViewPagetAdapter extends FragmentPagerAdapter {
    Context context;

    public Gift_HistoryFragmentViewPagetAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return PageGift.newInstance();

            case 1:
                return PageHistory.newInstance();

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
                return context.getString(R.string.gifts).toUpperCase(l);
            case 1:
                return context.getString(R.string.history).toUpperCase(l);

        }
        return null;
    }
}

