package ru.moscowtaxi.android.moscowtaxi.helpers;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import retrofit.client.Response;

/**
 * Created by alex-pers on 12/1/14.
 */
public class WebUtils {
    public static boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }


    // Reads an InputStream and converts it to a String.
    public static String convertStreamToString(InputStream stream) throws IOException {
        BufferedReader r = new BufferedReader(new InputStreamReader(stream));
        StringBuilder total = new StringBuilder();
        String line;
        while ((line = r.readLine()) != null) {
            total.append(line);
        }
        return total.toString();
    }

    public static String getResponseString(Response response) throws IOException {
        String result = null;
        int statusCode = response.getStatus();

        if ((statusCode == 200)) {
            // If the response does not enclose an entity, there is no need
            // to worry about connection release
            InputStream inStream = response.getBody().in();
            if (inStream != null) {
                String responseString = convertStreamToString(inStream);
                inStream.close();
                return responseString;
            }
        }
        //debug
        else {

            InputStream inStream = response.getBody().in();
            if (inStream != null) {
                String responseString = convertStreamToString(inStream);
                inStream.close();
                return responseString;
            }
        }

        return result;
    }

}
