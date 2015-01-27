package ru.moscowtaxi.android.moscowtaxi.data;

import com.google.gson.annotations.Expose;
import com.j256.ormlite.field.DatabaseField;

/**
 * Created by ychabatarou on 01.12.2014.
 */
public class EntityORM {

    public static final String KEY_ID = "id";
    public static final String KEY_USER = "user";


    @DatabaseField(columnName = KEY_ID,generatedId = true)
    protected long id;
    @DatabaseField(columnName = KEY_USER)
    public String userName;

    public long getId() {
        return id;
    }

}
