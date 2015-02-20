package ru.moscowtaxi.android.moscowtaxi.activity;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import ru.moscowtaxi.android.moscowtaxi.R;
import ru.moscowtaxi.android.moscowtaxi.fragments.NavigationDrawerFragment;
import ru.moscowtaxi.android.moscowtaxi.fragments.PageFavorite;
import ru.moscowtaxi.android.moscowtaxi.fragments.PageHistory;
import ru.moscowtaxi.android.moscowtaxi.fragments.PageMain;
import ru.moscowtaxi.android.moscowtaxi.fragments.PageReward_History;
import ru.moscowtaxi.android.moscowtaxi.orm.FavoritePlaceORM;
import ru.moscowtaxi.android.moscowtaxi.orm.OrderHistoryORM;


public class MainActivity extends Activity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    public final static int KEY_ACTIVITY_RESULT_FROM_HISTORY = 1;
    public final static int KEY_ACTIVITY_RESULT_FROM_FAVORITE_PLACE = 2;
    public static int PAGE_MAIN = 0;
    public static int PAGE_FAVORITE_PLACE = 1;
    public static int PAGE_FAVORITE_ROUTE = 2;
    public static int PAGE_HISTORY = 3;
    public static int PAGE_REWARDS = 4;
    public static String KEY_ACTIVITY_RESULT_FROM_HISTORY_data = "key_from_history_data";
    public static String KEY_ACTIVITY_RESULT_FROM_FAVORITE_PLACE_data = "key_from_favorite_place";
    public static String KEY_PLAСE_NUMBER = "key_place_number";

    public static OrderHistoryORM historyORM;
    public static String adressFromLibrirary = "";
    public static boolean flag_from_or_to_editText = true;//true--from___false-to

    public Fragment current_fragment = null;


    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    public NavigationDrawerFragment mNavigationDrawerFragment;
    private int current_page = -1;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;

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


        buildGoogleApiClient();
        connectGoogleApiClient();
    }


    @Override
    public void onNavigationDrawerItemSelected(int position) {


        try {
            final InputMethodManager imm = (InputMethodManager) getSystemService(
                    Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {

        }

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
                fragment = PageHistory.newInstance();
                break;
            case 4:
                return;
//                if (current_page == PAGE_REWARDS) {
//                    return;
//                } else if (current_page == PAGE_HISTORY) {
//                    ViewPager pager = ((PageReward_History) current_fragment).mViewPager;
//                    if (pager != null) {
//                        pager.setCurrentItem(0, true);
//                        current_page = position;
//                    }
//                    return;
//                }
//                fragment = PageReward_History.newInstance(0);
//                break;
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


    private void connectGoogleApiClient() {
        if (mGoogleApiClient != null && !mGoogleApiClient.isConnected()) {
            mGoogleApiClient.connect();
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }


    @Override
    public void onConnected(Bundle bundle) {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    public Location getLastLocation() {
        return mLastLocation;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            return;
        }
        Bundle bundle;
        switch (requestCode) {
            case KEY_ACTIVITY_RESULT_FROM_HISTORY:
                bundle = data.getExtras();
                OrderHistoryORM historyORM = (OrderHistoryORM) bundle.getSerializable(KEY_ACTIVITY_RESULT_FROM_HISTORY_data);


                if (current_fragment instanceof PageMain) {
                    ((PageMain) current_fragment).setCurrentItem(1);
                    MainActivity.historyORM = historyORM;

                }
                break;
            case KEY_ACTIVITY_RESULT_FROM_FAVORITE_PLACE:
                flag_from_or_to_editText = data.getBooleanExtra(KEY_PLAСE_NUMBER, true);
                bundle = data.getExtras();

                FavoritePlaceORM favoritePlaceORM = (FavoritePlaceORM) bundle.getSerializable(KEY_ACTIVITY_RESULT_FROM_FAVORITE_PLACE_data);
                adressFromLibrirary = favoritePlaceORM.address;
                if (current_fragment instanceof PageMain) {
                    ((PageMain) current_fragment).setCurrentItem(0);
                } else {
                    onNavigationDrawerItemSelected(0);
                }
                break;
        }
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

    /**
     * A subclass of AsyncTask that calls getFromLocation() in the
     * background. The class definition has these generic types:
     * Location - A Location object containing
     * the current location.
     * Void     - indicates that progress units are not used
     * String   - An address passed to onPostExecute()
     */
    public static class GetAddressTask extends
            AsyncTask<Location, Void, String> {
        private final EditText edtFrom;
        private final BaseAdapter mAdapter;
        Context mContext;

        public GetAddressTask(Context context, EditText editText) {
            super();
            mContext = context;
            edtFrom = editText;
            mAdapter = null;
        }

        public GetAddressTask(Context context, EditText editText, BaseAdapter adapter) {
            super();
            mContext = context;
            edtFrom = editText;
            mAdapter = adapter;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Activity activity = (Activity) mContext;
            activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        }

        /**
         * Get a Geocoder instance, get the latitude and longitude
         * look up the address, and return it
         *
         * @return A string containing the address of the current
         * location, or an empty string if no address can be found,
         * or an error message
         * @params params One or more Location objects
         */
        @Override
        protected String doInBackground(Location... params) {
            Locale myLocale = new Locale("ru", "RU");
            Geocoder geocoder =
                    new Geocoder(mContext, myLocale);
            // Get the current location from the input parameter list
            Location loc = params[0];
            // Create a list to contain the result address
            List<Address> addresses = null;
            try {
                /*
                 * Return 1 address.
                 */
                addresses = geocoder.getFromLocation(loc.getLatitude(),
                        loc.getLongitude(), 1);
            } catch (IOException e1) {
                Log.e("LocationSampleActivity",
                        "IO Exception in getFromLocation()");
                e1.printStackTrace();
                return mContext.getString(R.string.failed_determinate_location);
            } catch (IllegalArgumentException e2) {
                // Error message to post in the log
                String errorString = "Illegal arguments " +
                        Double.toString(loc.getLatitude()) +
                        " , " +
                        Double.toString(loc.getLongitude()) +
                        " passed to address service";
                Log.e("LocationSampleActivity", errorString);
                e2.printStackTrace();
                return mContext.getString(R.string.failed_determinate_location);
            }
            // If the reverse geocode returned an address
            if (addresses != null && addresses.size() > 0) {
                // Get the first address
                Address address = addresses.get(0);
                /*
                 * Format the first line of address (if available),
                 * city, and country name.
                 */
                String addressText = String.format(
                        "%s, %s, %s",
                        // If there's a street address, add it
                        address.getMaxAddressLineIndex() > 0 ?
                                address.getAddressLine(0) : "",
                        // Locality is usually a city
                        address.getLocality(),
                        // The country of the address
                        address.getCountryName()
                );
                // Return the text
                return addressText;
            } else {
                return mContext.getString(R.string.failed_determinate_location);
            }
        }

        @Override
        protected void onPostExecute(String s) {
            Log.v(null, "ADDRESS" + s);
            if (!TextUtils.isEmpty(s)) {
                edtFrom.setText(s);
                edtFrom.requestFocus();
            }

            if (mAdapter != null) {
                Log.v(null, "update adapter");
            }

            Activity activity = (Activity) mContext;
            activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
            super.onPostExecute(s);
        }
    }


}
