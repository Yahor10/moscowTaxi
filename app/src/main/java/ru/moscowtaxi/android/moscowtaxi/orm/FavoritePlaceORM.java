package ru.moscowtaxi.android.moscowtaxi.orm;

import android.content.Context;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.j256.ormlite.table.DatabaseTable;

import java.sql.SQLException;
import java.util.List;

import ru.moscowtaxi.android.moscowtaxi.data.EntityORM;
import ru.moscowtaxi.android.moscowtaxi.preferences.PreferenceUtils;

/**
 * Created by ychabatarou on 01.12.2014.
 */

@DatabaseTable(tableName = "favorite_place")
public class FavoritePlaceORM extends EntityORM {


    public static final String NAME = "name";
    public static final String ADDRESS = "address";

    @DatabaseField(columnName = NAME)
    public String name;
    @DatabaseField(columnName = ADDRESS)
    public String address;

    public boolean is_edited_now;

    public FavoritePlaceORM() {

    }

    public FavoritePlaceORM(String userName, String name, String address) {
        this.userName = userName;
        this.name = name;
        this.address = address;
    }

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
            String currentUser = PreferenceUtils.getCurrentUser(context);
            PreparedQuery<FavoritePlaceORM> prepare = dao.queryBuilder().where()
                    .eq(FavoritePlaceORM.KEY_USER, currentUser).prepare();
            query = dao.query(prepare);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            OpenHelperManager.releaseHelper();
        }
        return query;
    }


    @Override
    public String toString() {
        return "FavoritePlaceORM{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", user='" + userName + '\'' +
                '}';
    }

    public static boolean isPlaceExist(Context context, String currentUser, String name) {
        DatabaseHelper helper = OpenHelperManager.getHelper(context,
                DatabaseHelper.class);
        final Dao<FavoritePlaceORM, String> dao;
        try {
            dao = helper.getFavoritesPlacesDAO();
            QueryBuilder<FavoritePlaceORM, String> queryBuilder = dao.queryBuilder();
            Where<FavoritePlaceORM, String> eq = queryBuilder.where().eq(FavoritePlaceORM.NAME, name);

            PreparedQuery<FavoritePlaceORM> preparedQuery = queryBuilder.prepare();
            List<FavoritePlaceORM> placeList = dao.query(preparedQuery);
            return !placeList.isEmpty();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }
}
