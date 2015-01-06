package ru.moscowtaxi.android.moscowtaxi.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.gson.annotations.SerializedName;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import ru.moscowtaxi.android.moscowtaxi.R;
import ru.moscowtaxi.android.moscowtaxi.activity.MainActivity;
import ru.moscowtaxi.android.moscowtaxi.helpers.WebUtils;
import ru.moscowtaxi.android.moscowtaxi.helpers.adapters.AutoCompleteAdressAdapter;
import ru.moscowtaxi.android.moscowtaxi.helpers.http.TaxiApi;
import ru.moscowtaxi.android.moscowtaxi.orm.OrderORM;
import ru.moscowtaxi.android.moscowtaxi.preferences.PreferenceUtils;

/**
 * Created by alex-pers on 11/30/14.
 */
public class PageOrder extends Fragment implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {


    AutoCompleteTextView edtFrom;
    AutoCompleteTextView edtWhere;
    EditText edtKoment;
    View butDetermine;
    View butMyAdresesFrom;
    View butMyAdresesWhere;
    View butGetCarNow;
    View butClearKoment;
    TextView textHour;
    TextView textMinutes;
    Spinner spinnerTariff;
    Spinner spinnerAdditionalSettings;
    Button butGetTaxi;
    Button butCallOperator;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;

    public PageOrder() {

    }

    public static Fragment newInstance() {
        PageOrder fragment = new PageOrder();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.page_order, container, false);

        edtFrom = (AutoCompleteTextView) rootView.findViewById(R.id.edt_from);
        edtWhere = (AutoCompleteTextView) rootView.findViewById(R.id.edt_where);
        edtKoment = (EditText) rootView.findViewById(R.id.edt_koment);

        butDetermine = (View) rootView.findViewById(R.id.view_but_determine);
        butMyAdresesFrom = (View) rootView.findViewById(R.id.view_but_my_adreeses_from);
        butMyAdresesWhere = (View) rootView.findViewById(R.id.view_but_my_adreeses_where);
        butGetCarNow = (View) rootView.findViewById(R.id.view_but_get_car_now);
        butClearKoment = (View) rootView.findViewById(R.id.view_but_cleat_koment);

        textHour = (TextView) rootView.findViewById(R.id.text_hour);
        textMinutes = (TextView) rootView.findViewById(R.id.text_minutes);

        spinnerTariff = (Spinner) rootView.findViewById(R.id.spinner_tariff);
        spinnerAdditionalSettings = (Spinner) rootView.findViewById(R.id.spinner_additional_settings);

        textHour.setOnClickListener(this);
        textMinutes.setOnClickListener(this);
        edtFrom.requestFocus();
        edtFrom.setAdapter(new AutoCompleteAdressAdapter(getActivity()));
//        edtWhere.setAdapter(new AutoCompleteAdressAdapter(getActivity(), R.layout.list_item_autocomplete));


        Activity activity = getActivity();
        ContextThemeWrapper wrapper = new ContextThemeWrapper(activity, android.R.style.Theme_DeviceDefault_Light_DarkActionBar);
        MainActivity.CustomSpinnerAdapter tariffAdapter =
                (MainActivity.CustomSpinnerAdapter) MainActivity.CustomSpinnerAdapter.createFromResource(wrapper, R.array.tariffs,
                        android.R.layout.simple_spinner_dropdown_item);

        spinnerTariff.setAdapter(tariffAdapter);

        MainActivity.CustomSpinnerAdapter additionSettingsAdapter =
                (MainActivity.CustomSpinnerAdapter) MainActivity.CustomSpinnerAdapter.createFromResource(wrapper, R.array.additional_settings,
                        android.R.layout.simple_spinner_dropdown_item);
        spinnerAdditionalSettings.setAdapter(additionSettingsAdapter);

        butGetTaxi = (Button) rootView.findViewById(R.id.button_get_taxi);
        butCallOperator = (Button) rootView.findViewById(R.id.button_call_operator);

        butDetermine.setOnClickListener(this);
        butMyAdresesFrom.setOnClickListener(this);
        butMyAdresesWhere.setOnClickListener(this);
        butGetCarNow.setOnClickListener(this);
        butClearKoment.setOnClickListener(this);
        butGetTaxi.setOnClickListener(this);
        butCallOperator.setOnClickListener(this);


        buildGoogleApiClient();
        connectGoogleApiClient();
        return rootView;
    }

    private void connectGoogleApiClient() {
        if (mGoogleApiClient != null && !mGoogleApiClient.isConnected()) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onClick(View view) {

        Calendar mcurrentTime = null;
        int hour = 0;
        int minute = 0;
        switch (view.getId()) {
            case R.id.view_but_determine:
                Activity activity = getActivity();
                if (mLastLocation != null) {
                    new GetAddressTask(activity).execute(mLastLocation);// TODO get single task reference
                } else {
                    Toast.makeText(activity, activity.getString(R.string.failed_determinate_location), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.view_but_my_adreeses_from:
                break;
            case R.id.view_but_my_adreeses_where:
                break;
            case R.id.view_but_get_car_now:
                mcurrentTime = Calendar.getInstance();
                hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                minute = mcurrentTime.get(Calendar.MINUTE);
                textHour.setText(Integer.toString(hour));
                textMinutes.setText(Integer.toString(minute));
                break;
            case R.id.view_but_cleat_koment:
                edtKoment.setText("");
                break;
            case R.id.button_get_taxi:
                if (WebUtils.isOnline(getActivity())) {
                    int commentLenght = edtKoment.getText().toString().length();
                    if (commentLenght > 300) {
                        Toast.makeText(getActivity(), getActivity().getString(R.string.out_lenght_comment), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    final ProgressDialog progressDialog = new ProgressDialog(getActivity());
                    progressDialog.setMessage("Wait ...");
                    progressDialog.show();

                    RestAdapter restAdapter = new RestAdapter.Builder()
                            .setEndpoint(TaxiApi.MAIN_URL)
                            .build();
                    TaxiApi service = restAdapter.create(TaxiApi.class);

                    String phone = PreferenceUtils.getCurrentUserPhone(getActivity());
                    String id = PreferenceUtils.getDeviceId(getActivity());
                    String hash = PreferenceUtils.getCurrentUserHash(getActivity());


                    OrderModel orderModel = new OrderModel();
                    orderModel.date = "" + System.currentTimeMillis();
                    orderModel.route = new ArrayList<OrderORM>();

                    OrderORM orderORM_FROM = new OrderORM();
                    orderORM_FROM.street = edtFrom.getText().toString();
                    orderORM_FROM.house = "51a";
                    orderORM_FROM.comment = " coment 1";
                    orderORM_FROM.geoData = 1200;

                    OrderORM orderORM_WHERE = new OrderORM();
                    orderORM_WHERE.street = edtWhere.getText().toString();
                    orderORM_WHERE.house = "2";
                    orderORM_WHERE.comment = edtKoment.getText().toString();
                    orderORM_WHERE.geoData = 1400;

                    orderModel.route.add(orderORM_FROM);
                    orderModel.route.add(orderORM_WHERE);
                    orderModel.typeCar = "1";


                    service.orderCost(phone, id, hash, orderModel, new Callback<Response>() {
                        @Override
                        public void success(Response s, Response response) {
                            try {

                                progressDialog.dismiss();
                                Log.d("ORDER", WebUtils.getResponseString(s));
                                Toast.makeText(getActivity(), "Result = " + WebUtils.getResponseString(s), Toast.LENGTH_SHORT).show();

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity().getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.error_no_internet_connection), Toast.LENGTH_SHORT).show();
                }


                break;
            case R.id.button_call_operator:

                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "74957887880"));
                startActivity(intent);
                break;
            case R.id.text_minutes:
            case R.id.text_hour:
                mcurrentTime = Calendar.getInstance();
                hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        textHour.setText(Integer.toString(selectedHour));
                        textMinutes.setText(Integer.toString(selectedMinute));
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
                break;
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
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

    public class OrderModel {
        //        public String phone;
//        public String imei;
        public ArrayList<OrderORM> route;
        @SerializedName(value = "class")
        public String typeCar;
        public String date;

    }

    /**
     * A subclass of AsyncTask that calls getFromLocation() in the
     * background. The class definition has these generic types:
     * Location - A Location object containing
     * the current location.
     * Void     - indicates that progress units are not used
     * String   - An address passed to onPostExecute()
     */
    private class GetAddressTask extends
            AsyncTask<Location, Void, String> {
        Context mContext;

        public GetAddressTask(Context context) {
            super();
            mContext = context;
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
                return getActivity().getString(R.string.failed_determinate_location);
            } catch (IllegalArgumentException e2) {
                // Error message to post in the log
                String errorString = "Illegal arguments " +
                        Double.toString(loc.getLatitude()) +
                        " , " +
                        Double.toString(loc.getLongitude()) +
                        " passed to address service";
                Log.e("LocationSampleActivity", errorString);
                e2.printStackTrace();
                return getActivity().getString(R.string.failed_determinate_location);
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
                return getActivity().getString(R.string.failed_determinate_location);
            }
        }

        @Override
        protected void onPostExecute(String s) {
            Log.v(null, "ADDRESS" + s);
            if (isVisible() && !TextUtils.isEmpty(s)) {
                edtFrom.setText(s);
            }
            super.onPostExecute(s);
        }
    }

}
