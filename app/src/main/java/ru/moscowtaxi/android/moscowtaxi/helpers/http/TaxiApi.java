package ru.moscowtaxi.android.moscowtaxi.helpers.http;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;
import ru.moscowtaxi.android.moscowtaxi.constants.ConstantsJson;

/**
 * Created by alex-pers on 12/16/14.
 */
public interface TaxiApi {
    public static String MAIN_URL = "http://788.opteum.ru";

    @FormUrlEncoded
    @POST("/client/api/register")
    public void registration(@Field("phone") String phoneNumber, @Field("imei") String phoneId, Callback<Response> callback);


}
