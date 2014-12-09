package ru.moscowtaxi.android.moscowtaxi.helpers;


import android.app.Fragment;
import android.content.Context;
import android.support.v13.app.FragmentTabHost;

import android.view.LayoutInflater;
import android.view.View;

import android.widget.TabHost;
import android.widget.TextView;

import ru.moscowtaxi.android.moscowtaxi.R;

/**
 * Created by ychabatarou on 08.12.2014.
 */
public class TabHostUtils {

    public static void setupTab(String tag, String text,FragmentTabHost tabHost,
                          Class<? extends Fragment> fragment) {

        View tabview = createTabView(tabHost.getContext(), text);

        TabHost.TabSpec setContent = tabHost.newTabSpec(tag).setIndicator(tabview);

        tabHost.addTab(setContent, fragment, null);
    }

    private static View createTabView(final Context context, final String text) {
        View view = LayoutInflater.from(context).inflate(
                R.layout.tab_text_layout, null);
        TextView tv = (TextView) view.findViewById(R.id.txt_tab);
        tv.setText(text);

        return view;
    }
}
