package ru.moscowtaxi.android.moscowtaxi.helpers.http;

import android.content.Context;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ru.moscowtaxi.android.moscowtaxi.constants.ConstantsJson;

/**
 * Created by alex-pers on 12/1/14.
 */
public class TaxiHttpClient extends DefaultHttpClient {
    public static String URL_REGISTRATE = "http://788.opteum.ru/client/api/register/";
    Context context;

    public TaxiHttpClient(Context context) {
        this.context = context;
    }

    protected String postHttpData(String url, List<NameValuePair> nameValuePairs) {
        // Create a new HttpClient and Post Header
        String result = "";
        HttpPost httppost = new HttpPost(url);

        try {
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = execute(httppost);
            result = EntityUtils.toString(response.getEntity());
            return result;
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public String registrateUser(String phoneNumber, String phoneId) {
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
        nameValuePairs.add(new BasicNameValuePair(ConstantsJson.KEY_PHONE_NUMBER,
                phoneNumber));
        nameValuePairs.add(new BasicNameValuePair(ConstantsJson.KEY_PHONE_ID,
                phoneId));

        String result = postHttpData(URL_REGISTRATE, nameValuePairs);

        if (result == null || result.isEmpty())
            return null;


        return result;
    }


}
