package ru.moscowtaxi.android.moscowtaxi.fragments;


import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ru.moscowtaxi.android.moscowtaxi.R;
import ru.moscowtaxi.android.moscowtaxi.activity.LoginActivity;
import ru.moscowtaxi.android.moscowtaxi.activity.MainActivity;
import ru.moscowtaxi.android.moscowtaxi.preferences.PreferenceUtils;

/**
 * Fragment used for managing interactions for and presentation of a navigation drawer.
 * See the <a href="https://developer.android.com/design/patterns/navigation-drawer.html#Interaction">
 * design guidelines</a> for a complete explanation of the behaviors implemented here.
 */
public class NavigationDrawerFragment extends Fragment {

    /**
     * Remember the position of the selected item.
     */
    private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";
    /**
     * Per the design guidelines, you should show the drawer on launch until the user manually
     * expands it. This shared preference tracks this.
     */
    private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";
    private static int ORDER_TAXI_MENU_ID = 23;
    // имена атрибутов для Map
    final String ATTRIBUTE_NAME_TEXT = "text";
    final String ATTRIBUTE_NAME_TEXT2 = "text2";
    /**
     * A pointer to the current callbacks instance (the Activity).
     */
    private NavigationDrawerCallbacks mCallbacks;
    /**
     * Helper component that ties the action bar to the navigation drawer.
     */
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerListView;
    private View mFragmentContainerView;
    private int mCurrentSelectedPosition = 0;
    private boolean mFromSavedInstanceState;
    private boolean mUserLearnedDrawer;
    private MenuItem orderTaxiMenuItem;

    public NavigationDrawerFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Read in the flag indicating whether or not the user has demonstrated awareness of the
        // drawer. See PREF_USER_LEARNED_DRAWER for details.
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mUserLearnedDrawer = sp.getBoolean(PREF_USER_LEARNED_DRAWER, false);

        if (savedInstanceState != null) {
            mCurrentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION);
            mFromSavedInstanceState = true;
        }

        // Select either the default item (0) or the last selected item.
        selectItem(mCurrentSelectedPosition);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Indicate that this fragment would like to influence the set of actions in the action bar.
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mDrawerListView = (ListView) inflater.inflate(
                R.layout.fragment_navigation_drawer, container, false);
        mDrawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItem(position);
            }
        });


        View footerView = (View) inflater.inflate(R.layout.footer_view_menu, null);

        footerView.findViewById(R.id.txt_log_out).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PreferenceUtils.setCurrentHash(getActivity(), "");
                PreferenceUtils.setCurrentPhone(getActivity(), "");
                PreferenceUtils.setCurrentUser(getActivity(), "");
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                getActivity().startActivity(intent);
                getActivity().finish();
            }
        });

        mDrawerListView.addFooterView(footerView);

        String[] texts1 = {getString(R.string.navigation_personal_info), getString(R.string.navigation_favorites), getString(R.string.navigation_favorites_rides),
                getString(R.string.navigation_history_rides), getString(R.string.navigation_rewards)};
        String[] texts2 = {getString(R.string.navigation_personal_info_change), getString(R.string.navigation_favorites_list), getString(R.string.navigation_favorites_list),
                getString(R.string.navigation_history_rides_list), getString(R.string.navigation_private_rewards)};
        int img = R.drawable.ic_launcher;

        // упаковываем данные в понятную для адаптера структуру
        ArrayList<Map<String, Object>> data = new ArrayList<Map<String, Object>>(
                texts1.length);
        Map<String, Object> m;
        for (int i = 0; i < texts1.length - 1; i++) {
            m = new HashMap<String, Object>();
            m.put(ATTRIBUTE_NAME_TEXT, texts1[i]);
            m.put(ATTRIBUTE_NAME_TEXT2, texts2[i]);
            data.add(m);
        }

        // массив имен атрибутов, из которых будут читаться данные
        String[] from = {ATTRIBUTE_NAME_TEXT, ATTRIBUTE_NAME_TEXT2,
        };
        // массив ID View-компонентов, в которые будут вставлять данные
        int[] to = {android.R.id.text1, android.R.id.text2};

        // создаем адаптер
        SimpleAdapter sAdapter = new SimpleAdapter(getActivity(), data, android.R.layout.simple_list_item_activated_2,
                from, to) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                Resources resources = getResources();
                TextView text2 = (TextView) view.findViewById(android.R.id.text2);
                text1.setTextColor(resources.getColor(R.color.orange_color));
                text2.setTextColor(resources.getColor(android.R.color.white));

                return view;
            }
        };


        mDrawerListView.setAdapter(sAdapter);
        mDrawerListView.setItemChecked(mCurrentSelectedPosition, true);
        return mDrawerListView;
    }

    public boolean isDrawerOpen() {
        return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(mFragmentContainerView);
    }

    /**
     * Users of this fragment must call this method to set up the navigation drawer interactions.
     *
     * @param fragmentId   The android:id of this fragment in its activity's layout.
     * @param drawerLayout The DrawerLayout containing this fragment's UI.
     */
    public void setUp(int fragmentId, DrawerLayout drawerLayout) {
        mFragmentContainerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;

        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener

        ActionBar actionBar = getActionBar();
        // Action Bar

        // Adapter
        MainActivity.CustomSpinnerAdapter adapter =
                (MainActivity.CustomSpinnerAdapter) MainActivity.CustomSpinnerAdapter.createFromResource(getActivity(), R.array.cities,
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

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);


        // ActionBarDrawerToggle ties together the the proper interactions
        // between the navigation drawer and the action bar app icon.
        mDrawerToggle = new ActionBarDrawerToggle(
                getActivity(),                    /* host Activity */
                mDrawerLayout,                    /* DrawerLayout object */
                R.drawable.ic_drawer,             /* nav drawer image to replace 'Up' caret */
                R.string.navigation_drawer_open,  /* "open drawer" description for accessibility */
                R.string.navigation_drawer_close  /* "close drawer" description for accessibility */
        ) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if (!isAdded()) {
                    return;
                }

                getActivity().invalidateOptionsMenu(); // calls onPrepareOptionsMenu()
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (!isAdded()) {
                    return;
                }

                if (!mUserLearnedDrawer) {
                    // The user manually opened the drawer; store this flag to prevent auto-showing
                    // the navigation drawer automatically in the future.
                    mUserLearnedDrawer = true;
                    SharedPreferences sp = PreferenceManager
                            .getDefaultSharedPreferences(getActivity());
                    sp.edit().putBoolean(PREF_USER_LEARNED_DRAWER, true).apply();
                }

                getActivity().invalidateOptionsMenu(); // calls onPrepareOptionsMenu()
            }
        };

        // If the user hasn't 'learned' about the drawer, open it to introduce them to the drawer,
        // per the navigation drawer design guidelines.
        if (!mUserLearnedDrawer && !mFromSavedInstanceState) {
            mDrawerLayout.openDrawer(mFragmentContainerView);
        }

        // Defer code dependent on restoration of previous instance state.
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    private void selectItem(int position) {
        mCurrentSelectedPosition = position;
//        Activity activity = getActivity();
//        switch (position){
//            case 1:
//                startActivity(FavoritesActivity.buildIntent(activity));
//                break;
//            case 2:
//                startActivity(FavoritesActivity.buildIntent(activity));
//                break;
//        }

        if (mDrawerListView != null) {
            mDrawerListView.setItemChecked(position, true);
        }
        if (mDrawerLayout != null) {
            mDrawerLayout.closeDrawer(mFragmentContainerView);
        }
        if (mCallbacks != null) {
            mCallbacks.onNavigationDrawerItemSelected(position);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallbacks = (NavigationDrawerCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement NavigationDrawerCallbacks.");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Forward the new configuration the drawer toggle component.
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // If the drawer is open, show the global app actions in the action bar. See also
        // showGlobalContextActionBar, which controls the top-left area of the action bar.
        MainActivity activity = (MainActivity) getActivity();
        int currentpage = activity.getCurrentpage();

        orderTaxiMenuItem = menu.add(0, ORDER_TAXI_MENU_ID, 0, R.string.order_taxi);
        orderTaxiMenuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        orderTaxiMenuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                ((MainActivity) getActivity()).onNavigationDrawerItemSelected(0);
                return true;
            }
        });

        if (currentpage == 0) {
            menu.removeItem(orderTaxiMenuItem.getItemId());
        }

        if (mDrawerLayout != null && isDrawerOpen()) {
            showGlobalContextActionBar();
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        if (item.getItemId() == R.id.action_example) {
            Toast.makeText(getActivity(), "Example action.", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Per the navigation drawer design guidelines, updates the action bar to show the global app
     * 'context', rather than just what's in the current screen.
     */
    private void showGlobalContextActionBar() {
        ActionBar actionBar = getActionBar();
        // Action Bar

        // Adapter
        MainActivity.CustomSpinnerAdapter adapter =
                (MainActivity.CustomSpinnerAdapter) MainActivity.CustomSpinnerAdapter.createFromResource(getActivity(), R.array.cities,
                        android.R.layout.simple_spinner_dropdown_item);

// Callback
        ActionBar.OnNavigationListener callback = new ActionBar.OnNavigationListener() {

            String[] items = getResources().getStringArray(R.array.cities); // List items from res

            @Override
            public boolean onNavigationItemSelected(int position, long id) {
                return true;

            }

        };


        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setListNavigationCallbacks(adapter, callback);

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

    }

    private ActionBar getActionBar() {
        return getActivity().getActionBar();
    }

    /**
     * Callbacks interface that all activities using this fragment must implement.
     */
    public static interface NavigationDrawerCallbacks {
        /**
         * Called when an item in the navigation drawer is selected.
         */
        void onNavigationDrawerItemSelected(int position);
    }
}
