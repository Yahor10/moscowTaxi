package ru.moscowtaxi.android.moscowtaxi.orm;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.PreparedUpdate;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.j256.ormlite.table.DatabaseTable;

import java.sql.SQLException;
import java.util.List;

import ru.moscowtaxi.android.moscowtaxi.data.EntityORM;
import ru.moscowtaxi.android.moscowtaxi.enumeration.OrderType;
import ru.moscowtaxi.android.moscowtaxi.preferences.PreferenceUtils;

/**
 * Created by alex-pers on 12/10/14.
 */

@DatabaseTable(tableName = "favorite_route")
public class FavoriteRouteORM extends EntityORM {
    //
    public static final String NAME = "name";
    public static final String ADDRESS_FROM = "address_from";
    public static final String ADDRESS_TO = "address_to";
    public static final String TIME = "time";
    public static final String TYPE = "type";

    @DatabaseField(columnName = NAME)
    public String name;
    @DatabaseField(columnName = ADDRESS_FROM)
    public String addressFrom;
    @DatabaseField(columnName = ADDRESS_TO)
    public String addressTo;
    @DatabaseField(columnName = TIME)
    public long time;
    @DatabaseField(columnName = TYPE)
    public OrderType type;

    public boolean is_edited_now;

    //
//
    public FavoriteRouteORM() {

    }

    public FavoriteRouteORM(String userName, String name, String addressFrom, String addressTo, long time, OrderType type) {
        this.name = name;
        this.addressFrom = addressFrom;
        this.addressTo = addressTo;
        this.time = time;
        this.type = type;
    }

    public static int insertFavoriteRoute(Context context, FavoriteRouteORM order) {
        int result;
        try {
            DatabaseHelper helper = OpenHelperManager.getHelper(context,
                    DatabaseHelper.class);
            final Dao<FavoriteRouteORM, String> dao = helper.getFavoriteRouteDao();
            result = dao.create(order);
        } catch (Exception e1) {
            return 0;
        } finally {
            OpenHelperManager.releaseHelper();
        }
        return result;
    }


    public static void insertOrUpdateFavoritePlace(Context context, FavoriteRouteORM routeORM) {

        try {
            DatabaseHelper helper = OpenHelperManager.getHelper(context,
                    DatabaseHelper.class);
            final Dao<FavoriteRouteORM, String> dao = helper.getFavoriteRouteDao();
            PreparedQuery<FavoriteRouteORM> prepare = dao.queryBuilder().where()
                    .eq(FavoriteRouteORM.KEY_ID, routeORM.getId()).prepare();
            List<FavoriteRouteORM> query = dao.query(prepare);
            if (query.isEmpty()) {

                if (insertFavoriteRoute(context, routeORM) == 0) {
                    throw new IllegalArgumentException("cannot create favorite place");
                }
            } else {
                UpdateBuilder<FavoriteRouteORM, String> updateBuilder = dao
                        .updateBuilder();
                updateBuilder.updateColumnValue(FavoriteRouteORM.NAME, routeORM.name);
                updateBuilder.updateColumnValue(FavoriteRouteORM.ADDRESS_FROM, routeORM.addressFrom);
                updateBuilder.updateColumnValue(FavoriteRouteORM.ADDRESS_TO, routeORM.addressTo);
                updateBuilder.updateColumnValue(FavoriteRouteORM.TIME, routeORM.time);
                updateBuilder.updateColumnValue(FavoriteRouteORM.TYPE, routeORM.type);
                updateBuilder.where().eq(FavoritePlaceORM.KEY_ID, routeORM.getId());
                PreparedUpdate<FavoriteRouteORM> prepare2 = updateBuilder.prepare();
                dao.update(prepare2);
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        } finally {
            OpenHelperManager.releaseHelper();
        }
    }


    public static List<FavoriteRouteORM> getFavoriteRoutes(Context context) {
        List<FavoriteRouteORM> query = null;
        try {
            DatabaseHelper helper = OpenHelperManager.getHelper(context,
                    DatabaseHelper.class);
            final Dao<FavoriteRouteORM, String> dao = helper.getFavoriteRouteDao();
            String currentUser = PreferenceUtils.getCurrentUser(context);
            PreparedQuery<FavoriteRouteORM> prepare = dao.queryBuilder().where()
                    .eq(FavoritePlaceORM.KEY_USER, currentUser).prepare();
            query = dao.query(prepare);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            OpenHelperManager.releaseHelper();
        }
        return query;
    }


    public static boolean deleteFavoriteRouteByID(Context context, long id) {
        try {
            DatabaseHelper helper = OpenHelperManager.getHelper(context,
                    DatabaseHelper.class);
            final Dao<FavoriteRouteORM, String> dao = helper.getFavoriteRouteDao();

            DeleteBuilder<FavoriteRouteORM, String> deleteBuilder = dao.deleteBuilder();
            deleteBuilder.where().eq(KEY_ID, id);
            int key = deleteBuilder.delete();

            if (key < 0) {
                Log.e(null, "delete event by id");
                return false;
            }

        } catch (SQLException e) {
        } finally {
            OpenHelperManager.releaseHelper();
        }
        return true;
    }


}
