package ru.moscowtaxi.android.moscowtaxi.helpers.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.google.gson.Gson;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import ru.moscowtaxi.android.moscowtaxi.fragments.PageFollow;
import ru.moscowtaxi.android.moscowtaxi.helpers.WebUtils;
import ru.moscowtaxi.android.moscowtaxi.helpers.http.TaxiApi;
import ru.moscowtaxi.android.moscowtaxi.preferences.PreferenceUtils;

/**
 * Created by alex-pers on 1/28/15.
 */
public class FollowOrderService extends Service {

    public UpdateMyResults updater;
    boolean flagEndWork = false;
    int id_order;

    public FollowOrderService() {

    }

    public static void sendMessageToFragment(PageFollow.FollowRequest followRequest, Context context) {
        Intent intent = new Intent(PageFollow.KEY_RECIEVER_FOLLOW);
        Bundle b = new Bundle();
        b.putSerializable(PageFollow.KEY_RECIEVER_DATA_FROM_SERVER, followRequest);
        intent.putExtras(b);
        context.sendBroadcast(intent);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            id_order = bundle.getInt(PageFollow.KEY_ORDER_ID, 0);
            if ((updater == null || !updater.isAlive()) && id_order > 0) {
                updater = new UpdateMyResults();
                updater.start();
            }
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public synchronized void onDestroy() {
        // TODO Auto-generated method stub
        flagEndWork = true;
        try {
            updater.join();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        super.onDestroy();

    }

    private class UpdateMyResults extends Thread {

        static final long DELAY = 20000;

        @Override
        public void run() {
            while (!flagEndWork) {
                try {
                    //Do stuff and pause
                    if (WebUtils.isOnline(getApplicationContext())) {


                        RestAdapter restAdapter = new RestAdapter.Builder()
                                .setEndpoint(TaxiApi.MAIN_URL)
                                .build();
                        TaxiApi service = restAdapter.create(TaxiApi.class);

                        String phone = PreferenceUtils.getCurrentUserPhone(getApplicationContext());
                        String id = PreferenceUtils.getDeviceId(getApplicationContext());
                        String hash = PreferenceUtils.getCurrentUserHash(getApplicationContext());

                        service.getStatusAsynk(phone, id, hash, "" + id_order, new Callback<Response>() {
                            @Override
                            public void success(Response response, Response response2) {


                                try {
                                    Gson gson = new Gson();
                                    String str = WebUtils.getResponseString(response);
                                    PageFollow.FollowRequest orderRequest = gson.fromJson(str, PageFollow.FollowRequest.class);
                                    if (orderRequest.c >= 1) {
                                        if (orderRequest.d.driverGeo!= null && orderRequest.d.driverGeo.contains(",")) {
                                            String[] geo_data = orderRequest.d.driverGeo.split(",");
                                            if (geo_data.length == 2) {


                                                orderRequest.d.latitude = Double.valueOf(geo_data[0]);
                                                orderRequest.d.longitude = Double.valueOf(geo_data[1]);
                                            }
                                        }
                                    }
                                    sendMessageToFragment(orderRequest, getApplicationContext());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void failure(RetrofitError error) {
                                Log.e("FOLLOW_FRAGMENT", "retrofit_error");
                            }
                        });
                    }

                    this.sleep(DELAY);

                } catch (Exception e) {

                    e.printStackTrace();
                }
            }
        }
    }

}
