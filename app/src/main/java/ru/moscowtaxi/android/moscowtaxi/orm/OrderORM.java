package ru.moscowtaxi.android.moscowtaxi.orm;

import com.google.gson.annotations.SerializedName;

import ru.moscowtaxi.android.moscowtaxi.data.EntityORM;

/**
 * Created by alex-pers on 12/10/14.
 */
public class OrderORM extends EntityORM {

    @SerializedName(value = "a")
    public String street;
    @SerializedName(value = "p")
    public String house;
    @SerializedName(value = "c")
    public String comment;
    @SerializedName(value = "g")
    public int geoData;

}
