package ru.moscowtaxi.android.moscowtaxi.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import ru.moscowtaxi.android.moscowtaxi.R;
import ru.moscowtaxi.android.moscowtaxi.fragments.NavigationDrawerFragment;
import ru.moscowtaxi.android.moscowtaxi.fragments.PageFavorite;
import ru.moscowtaxi.android.moscowtaxi.fragments.PageMain;
import ru.moscowtaxi.android.moscowtaxi.fragments.PageReward_History;


public class MainActivity extends Activity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {
    public static int PAGE_MAIN = 0;
    public static int PAGE_FAVORITE_PLACE = 1;
    public static int PAGE_FAVORITE_ROUTE = 2;
    public static int PAGE_HISTORY = 3;
    public static int PAGE_REWARDS = 4;
    private int current_page = -1;
    public Fragment current_fragment = null;
    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);


        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));


    }


    @Override
    public void onNavigationDrawerItemSelected(int position) {
        Fragment fragment;
        switch (position) {
            case 0:
                if (current_page == PAGE_MAIN)
                    return;
                fragment = PageMain.newInstance(0);
                break;
            case 1:
                if (current_page == PAGE_FAVORITE_PLACE) {
                    return;
                } else if (current_page == PAGE_FAVORITE_ROUTE) {
                    ViewPager pager = ((PageFavorite) current_fragment).mViewPager;
                    if (pager != null) {
                        pager.setCurrentItem(0, true);
                        current_page = position;
                    }
                    return;
                }
                fragment = PageFavorite.newInstance(0);
                break;
            case 2:
                if (current_page == PAGE_FAVORITE_ROUTE) {
                    return;
                } else if (current_page == PAGE_FAVORITE_PLACE) {
                    ViewPager pager = ((PageFavorite) current_fragment).mViewPager;
                    if (pager != null) {
                        pager.setCurrentItem(1, true);
                        current_page = position;
                    }
                    return;
                }
                fragment = PageFavorite.newInstance(1);
                break;
            case 3:
                if (current_page == PAGE_HISTORY) {
                    return;
                } else if (current_page == PAGE_REWARDS) {
                    ViewPager pager = ((PageReward_History) current_fragment).mViewPager;
                    if (pager != null) {
                        pager.setCurrentItem(1, true);
                        current_page = position;
                    }
                    return;
                }
                fragment = PageReward_History.newInstance(1);
                break;
            case 4:
                if (current_page == PAGE_REWARDS) {
                    return;
                } else if (current_page == PAGE_HISTORY) {
                    ViewPager pager = ((PageReward_History) current_fragment).mViewPager;
                    if (pager != null) {
                        pager.setCurrentItem(0, true);
                        current_page = position;
                    }
                    return;
                }
                fragment = PageReward_History.newInstance(0);
                break;
            default:
                fragment = PlaceholderFragment.newInstance(position + 1);
        }

        current_fragment = fragment;
        current_page = position;
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

    public int getCurrentpage() {
        return current_page;
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }


    }

    public static class CustomSpinnerAdapter<T> extends ArrayAdapter<T> {

        public CustomSpinnerAdapter(Context context, int textViewResourceId, T[] objects) {
            super(context, textViewResourceId, objects);
        }

        public static CustomSpinnerAdapter<CharSequence> createFromResource(Context context, int textArrayResId, int textViewResId) {
            CharSequence[] strings = context.getResources().getTextArray(textArrayResId);
            return new CustomSpinnerAdapter<CharSequence>(context, textViewResId, strings);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = super.getView(position, convertView, parent);
            return SetOrangeColor(view);
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            View view = super.getView(position, convertView, parent);
            return SetOrangeColor(view);
        }

        private View SetOrangeColor(View v) {
            Resources resources = getContext().getResources();
            int color = resources.getColor(R.color.orange_color);
            if (v instanceof TextView) ((TextView) v).setTextColor(color); // white
            return v;
        }


    }

}
