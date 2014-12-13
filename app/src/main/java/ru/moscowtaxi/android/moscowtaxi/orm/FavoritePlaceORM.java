package ru.moscowtaxi.android.moscowtaxi.orm;

import android.content.Context;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.table.DatabaseTable;

import java.sql.SQLException;
import java.util.List;

import ru.moscowtaxi.android.moscowtaxi.data.EntityORM;

/**
 * Created by ychabatarou on 01.12.2014.
 */

@DatabaseTable(tableName = "favorite_place")
public class FavoritePlaceORM  extends EntityORM {


    public static final String NAME = "name";
    public static final String ADDRESS = "address";

    @DatabaseField(columnName = KEY_ID)
    public int id;
    @DatabaseField(columnName = KEY_USER)
    public int userName;
    @DatabaseField(columnName = NAME)
    public String name;
    @DatabaseField(columnName = ADDRESS)
    public int address;


    public static int insertFavoritePlace(Context context, FavoritePlaceORM place) {
        int result;
        try {
            DatabaseHelper helper = OpenHelperManager.getHelper(context,
                    DatabaseHelper.class);
            final Dao<FavoritePlaceORM, String> dao = helper.getFavoritesPlacesDAO();
            result = dao.create(place);
        } catch (Exception e1) {
            return 0;
        } finally {
            OpenHelperManager.releaseHelper();
        }
        return result;
    }

    public static List<FavoritePlaceORM> getFavoritesPlaces(Context context) {
        List<FavoritePlaceORM> query = null;
        try {
            DatabaseHelper helper = OpenHelperManager.getHelper(context,
                    DatabaseHelper.class);
            final Dao<FavoritePlaceORM, String> dao = helper.getFavoritesPlacesDAO();
//            String currentUser = PreferenceUtils.getCurrentUser(context);
            PreparedQuery<FavoritePlaceORM> prepare = dao.queryBuilder().where()
                    .eq(FavoritePlaceORM.KEY_USER, "Test").prepare();
            query = dao.query(prepare);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            OpenHelperManager.releaseHelper();
        }
        return query;
    }

}