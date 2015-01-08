package ru.moscowtaxi.android.moscowtaxi.helpers.adapters;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import ru.moscowtaxi.android.moscowtaxi.preferences.PreferenceUtils;

/**
 * Created by alex-pers on 1/6/15.
 */
public class AutoCompleteAdressAdapter extends ArrayAdapter<String> implements Filterable {

    private LayoutInflater mInflater;
//    private Geocoder mGeocoder;
    private StringBuilder mSb = new StringBuilder();

    public AutoCompleteAdressAdapter(final Context context) {
        super(context, -1);
        mInflater = LayoutInflater.from(context);
//        Locale myLocale = new Locale("ru", "RU");
//        mGeocoder = new Geocoder(context,myLocale);
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {
        final TextView tv;
        if (convertView != null) {
            tv = (TextView) convertView;
        } else {
            tv = (TextView) mInflater.inflate(android.R.layout.simple_dropdown_item_1line, parent, false);
        }

//        tv.setText(createFormattedAddressFromAddress(getItem(position)));
        tv.setText(getItem(position));

        return tv;
    }

    private String createFormattedAddressFromAddress(final Address address) {
        mSb.setLength(0);
        final int addressLineSize = address.getMaxAddressLineIndex();
        for (int i = 0; i < addressLineSize; i++) {
            mSb.append(address.getAddressLine(i));
            if (i != addressLineSize - 1) {
                mSb.append(", ");
            }
        }
        return mSb.toString();
    }

    @Override
    public Filter getFilter() {
        Filter myFilter = new Filter() {
            @Override
            protected FilterResults performFiltering(final CharSequence constraint) {
                List<String> addressList = null;
                if (constraint != null) {
                    addressList = PreferenceUtils.getSavedAddresses(getContext(),constraint.toString());
                }

                if (addressList == null) {
                    addressList = new ArrayList<String>();
                }

                final FilterResults filterResults = new FilterResults();
                filterResults.values = addressList;
                filterResults.count = addressList.size();

                return filterResults;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(final CharSequence contraint, final FilterResults results) {
                clear();
                for (String string : (List<String>) results.values) {
                    add(string);
                }
                if (results.count > 0) {
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }

            @Override
            public CharSequence convertResultToString(final Object resultValue) {
                return resultValue == null ? "" : ((String) resultValue);
            }
        };
        return myFilter;
    }
}

