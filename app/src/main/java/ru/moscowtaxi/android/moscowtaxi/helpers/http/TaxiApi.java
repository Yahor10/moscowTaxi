package ru.moscowtaxi.android.moscowtaxi.helpers.http;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;
import ru.moscowtaxi.android.moscowtaxi.fragments.PageOrder;

/**
 * Created by alex-pers on 12/16/14.
 */
public interface TaxiApi {
    public static String MAIN_URL = "http://788.opteum.ru";


    //    @POST("/client/api/register")
//    public void registration(@Body BaseRequestModel model, Callback<Response> callback);
    @FormUrlEncoded
    @POST("/client/api/register")
    public void registration(@Field("phone") String phoneNumber, @Field("imei") String phoneId, Callback<Response> callback);


    //    @POST("/client/api/auth")
//    public void login(@Body BaseRequestModel model, Callback<Response> callback);
    @FormUrlEncoded
    @POST("/client/api/auth")
    public void login(@Field("phone") String phoneNumber, @Field("imei") String phoneId, @Field("hash") String hashMD5, Callback<Response> callback);

    @POST("/client/api/order")
    public void order(@Body PageOrder.OrderModel orderModel, Callback<Response> callback);

    @FormUrlEncoded
    @POST("/client/api/order")
    public void orderCost(@Field("phone") String phoneNumber, @Field("imei") String phoneId, @Field("hash") String hashMD5,
                          @Field("body") PageOrder.OrderModel orderModel, Callback<Response> callback);

    @FormUrlEncoded
    @POST("/client/api/order/status")
    public void getStatus(@Field("phone") String phoneNumber, @Field("imei") String phoneId, @Field("hash") String hashMD5,
                          @Field("order") String  id_taxi, Callback<Response> callback);



    @FormUrlEncoded
    @POST("/client/api/driver")
    public void getAllDrivers(@Field("phone") String phoneNumber, @Field("imei") String phoneId, @Field("hash") String hashMD5, Callback<Response> callback);


}
