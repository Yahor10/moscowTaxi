package ru.moscowtaxi.android.moscowtaxi.orm;

import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

/**
 * Database helper class used to manage the creation and upgrading of your
 * database. This class also usually provides the DAOs used by the other
 * classes.
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

	private static final Class<?>[] DATA_CLASSES = { FavoritePlaceORM.class ,OrderHistoryORM.class};

	public static final String DATABASE_NAME = "moscowtaxi.db";
	private static final int DATABASE_VERSION = 1;

	private Dao<FavoritePlaceORM, String> favoritePlacesDao = null;
    private Dao<OrderHistoryORM, String>  historyOrderDao = null;

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	/**
	 * This is called when the database is first created. Usually you should
	 * call createTable statements here to create the tables that will store
	 * your data.
	 */
	
	@Override
	public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
		try {
			for (Class<?> dataClass : DATA_CLASSES) {
				TableUtils.createTable(connectionSource, dataClass);
			}

		} catch (SQLException e) {
			Log.e(DatabaseHelper.class.getName(), "Can't create database", e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * This is called when your application is upgraded and it has a higher
	 * version number. This allows you to adjust the various data to match the
	 * new version number.
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource,
			int oldVersion, int newVersion) {
		try {
			for (Class<?> dataClass : DATA_CLASSES) {
				TableUtils.dropTable(connectionSource, dataClass, true);
			}

			onCreate(db, connectionSource);
		} catch (SQLException e) {
			Log.e(DatabaseHelper.class.getName(), "Can't drop databases", e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * Close the database connections and clear any cached DAOs.
	 */
	@Override
	public void close() {
		super.close();
	}

	public Dao<FavoritePlaceORM, String> getFavoritesPlacesDAO() throws SQLException {
		if (favoritePlacesDao == null) {
			favoritePlacesDao = getDao(FavoritePlaceORM.class);
		}
		return favoritePlacesDao;
	}


    public Dao<OrderHistoryORM, String> getHistoryOrderDao() throws SQLException {
        if (historyOrderDao == null) {
            historyOrderDao = getDao(OrderHistoryORM.class);
        }
        return historyOrderDao;
    }



}