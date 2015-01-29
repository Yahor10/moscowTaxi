package ru.moscowtaxi.android.moscowtaxi.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class PreferenceUtils {

    public static void setCurrentUser(Context context, String masterPassword) {
        Editor pEditor = PreferenceManager.getDefaultSharedPreferences(context)
                .edit();
        pEditor.putString(PreferenceKeys.CURRENT_USER, masterPassword);
        pEditor.commit();
    }

    public static String getCurrentUser(Context context) {
        SharedPreferences defaultSharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        return defaultSharedPreferences.getString(PreferenceKeys.CURRENT_USER,
                "Johny");
    }

    public static void setCurrentPhone(Context context, String phone) {
        SharedPreferences.Editor pEditor = PreferenceManager.getDefaultSharedPreferences(context)
                .edit();
        pEditor.putString(PreferenceKeys.KEY_CURRENT_PHONE_NUMBER, phone);
        pEditor.commit();
    }

    public static String getCurrentUserPhone(Context context) {
        SharedPreferences defaultSharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        return defaultSharedPreferences.getString(
                PreferenceKeys.KEY_CURRENT_PHONE_NUMBER, "");
    }

    public static void setCurrentHash(Context context, String hash) {
        SharedPreferences.Editor pEditor = PreferenceManager.getDefaultSharedPreferences(context)
                .edit();
        pEditor.putString(PreferenceKeys.KEY_CURRENT_HASH, hash);
        pEditor.commit();
    }

    public static String getCurrentUserHash(Context context) {
        SharedPreferences defaultSharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        return defaultSharedPreferences.getString(
                PreferenceKeys.KEY_CURRENT_HASH, "");
    }

    public static void setDeviceId(Context context, String id) {
        SharedPreferences.Editor pEditor = PreferenceManager.getDefaultSharedPreferences(context)
                .edit();
        pEditor.putString(PreferenceKeys.KEY_DEVICE_ID, id);
        pEditor.commit();
    }

    public static String getDeviceId(Context context) {
        SharedPreferences defaultSharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        return defaultSharedPreferences.getString(
                PreferenceKeys.KEY_DEVICE_ID, "");
    }


//    public static void setLastOrderId(Context context, int id) {
//        SharedPreferences.Editor pEditor = PreferenceManager.getDefaultSharedPreferences(context)
//                .edit();
//        pEditor.putInt(PreferenceKeys.KEY_LAST_ORDER_ID, id);
//        pEditor.commit();
//    }
//
//    public static int getLastOrderId(Context context) {
//        SharedPreferences defaultSharedPreferences = PreferenceManager
//                .getDefaultSharedPreferences(context);
//        return defaultSharedPreferences.getInt(
//                PreferenceKeys.KEY_LAST_ORDER_ID, 0);
//    }

    public static List<String> getSavedAddresses(Context context, String key) {
        SharedPreferences defaultSharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        final HashSet<String> defValues = new HashSet<String>();
        defValues.add("ул.Солнечкая");
        defValues.add("ул.Лунная");
        final Set<String> stringSet = defaultSharedPreferences.getStringSet(PreferenceKeys.KEY_SAVED_PLACES, defValues);
        final Iterator<String> iterator = stringSet.iterator();
        List<String> list = new ArrayList<String>();
        while (iterator.hasNext()) {
            final String next = iterator.next().toLowerCase();
            if (next.startsWith(key)) {
                list.add(next);
            }
        }
        return list;
    }

}
