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

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;

import ru.moscowtaxi.android.moscowtaxi.data.EntityORM;
import ru.moscowtaxi.android.moscowtaxi.enumeration.OrderType;
import ru.moscowtaxi.android.moscowtaxi.preferences.PreferenceUtils;

/**
 * Created by alex-pers on 12/10/14.
 */

@DatabaseTable(tableName = "order_history")
public class OrderHistoryORM extends EntityORM implements Serializable {
    //
    public static final String NUMBER = "number";
    public static final String NAME = "name";
    public static final String ADDRESS_FROM = "address_from";
    public static final String ADDRESS_TO = "address_to";
    public static final String TIME = "time";
    public static final String TYPE = "type";


    @DatabaseField(columnName = NUMBER)
    public int number;
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

    //
//
    public OrderHistoryORM() {

    }

    public OrderHistoryORM(String userName, String name, String addressFrom, String addressTo, long time, OrderType type) {
        this.name = name;
        this.addressFrom = addressFrom;
        this.addressTo = addressTo;
        this.time = time;
        this.type = type;
    }

    public static int insertOrderHistory(Context context, OrderHistoryORM order) {
        int result;
        try {
            DatabaseHelper helper = OpenHelperManager.getHelper(context,
                    DatabaseHelper.class);
            final Dao<OrderHistoryORM, String> dao = helper.getHistoryOrderDao();
            result = dao.create(order);
        } catch (Exception e1) {
            return 0;
        } finally {
            OpenHelperManager.releaseHelper();
        }
        return result;
    }

    public static void insertOrUpdateHistory(Context context, OrderHistoryORM historyORM) {

        try {
            DatabaseHelper helper = OpenHelperManager.getHelper(context,
                    DatabaseHelper.class);
            final Dao<OrderHistoryORM, String> dao = helper.getHistoryOrderDao();
            PreparedQuery<OrderHistoryORM> prepare = dao.queryBuilder().where()
                    .eq(OrderHistoryORM.KEY_ID, historyORM.getId()).prepare();
            List<OrderHistoryORM> query = dao.query(prepare);
            if (query.isEmpty()) {

                if (insertOrderHistory(context, historyORM) == 0) {
                    throw new IllegalArgumentException("cannot create favorite place");
                }
            } else {
                UpdateBuilder<OrderHistoryORM, String> updateBuilder = dao
                        .updateBuilder();
                updateBuilder.updateColumnValue(OrderHistoryORM.NAME, historyORM.name);
                updateBuilder.updateColumnValue(OrderHistoryORM.ADDRESS_FROM, historyORM.addressFrom);
                updateBuilder.updateColumnValue(OrderHistoryORM.ADDRESS_TO, historyORM.addressTo);
                updateBuilder.updateColumnValue(OrderHistoryORM.TIME, historyORM.time);
                updateBuilder.updateColumnValue(OrderHistoryORM.TYPE, historyORM.type);
                updateBuilder.updateColumnValue(OrderHistoryORM.NUMBER, historyORM.number);
                updateBuilder.where().eq(FavoritePlaceORM.KEY_ID, historyORM.getId());
                PreparedUpdate<OrderHistoryORM> prepare2 = updateBuilder.prepare();
                dao.update(prepare2);
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        } finally {
            OpenHelperManager.releaseHelper();
        }
    }

    public static List<OrderHistoryORM> getHistoryOrders(Context context) {
        List<OrderHistoryORM> query = null;
        try {
            DatabaseHelper helper = OpenHelperManager.getHelper(context,
                    DatabaseHelper.class);
            final Dao<OrderHistoryORM, String> dao = helper.getHistoryOrderDao();
            String currentUser = PreferenceUtils.getCurrentUser(context);
            PreparedQuery<OrderHistoryORM> prepare = dao.queryBuilder().where()
                    .eq(FavoritePlaceORM.KEY_USER, currentUser).prepare();
            query = dao.query(prepare);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            OpenHelperManager.releaseHelper();
        }
        return query;
    }


    public static boolean deleteHistoryByID(Context context, long id) {
        try {
            DatabaseHelper helper = OpenHelperManager.getHelper(context,
                    DatabaseHelper.class);
            final Dao<OrderHistoryORM, String> dao = helper.getHistoryOrderDao();

            DeleteBuilder<OrderHistoryORM, String> deleteBuilder = dao.deleteBuilder();
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
