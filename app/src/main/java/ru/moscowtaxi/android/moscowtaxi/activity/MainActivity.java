package ru.moscowtaxi.android.moscowtaxi.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import ru.moscowtaxi.android.moscowtaxi.fragments.NavigationDrawerFragment;
import ru.moscowtaxi.android.moscowtaxi.R;
import ru.moscowtaxi.android.moscowtaxi.fragments.PageMain;
import ru.moscowtaxi.android.moscowtaxi.fragments.PageFavorite;
import ru.moscowtaxi.android.moscowtaxi.fragments.PageGift_HIstory;


public class MainActivity extends Activity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
        // Action Bar

        // Adapter
        CustomSpinnerAdapter adapter =
                (CustomSpinnerAdapter) CustomSpinnerAdapter.createFromResource(this, R.array.cities,
                        android.R.layout.simple_spinner_dropdown_item);

// Callback
        ActionBar.OnNavigationListener callback = new ActionBar.OnNavigationListener() {

            String[] items = getResources().getStringArray(R.array.cities); // List items from res

            @Override
            public boolean onNavigationItemSelected(int position, long id) {
                return true;

            }

        };

        ActionBar actions = getActionBar();
        actions.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        actions.setDisplayShowTitleEnabled(false);
        actions.setListNavigationCallbacks(adapter, callback);
        Context themedContext = actions.getThemedContext();

    }


    @Override
    public void onNavigationDrawerItemSelected(int position) {
        Fragment fragment;
        switch (position) {
            case 0:
                fragment = PageMain.newInstance(0);
                break;
            case 1:
                fragment = PageFavorite.newInstance(0);
                break;
            case 2:
                fragment = PageFavorite.newInstance(1);
                break;
            case 3:
                fragment = PageGift_HIstory.newInstance(0);
                break;
            case 4:
                fragment = PageGift_HIstory.newInstance(1);
                break;
            default:
                fragment = PlaceholderFragment.newInstance(position + 1);
        }
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = "T1";
                break;
            case 2:
                mTitle = "T2";
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
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

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
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
            if (v instanceof TextView) ((TextView)v).setTextColor(color); // white
            return v;
        }
    }

}
