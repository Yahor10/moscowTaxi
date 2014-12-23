package ru.moscowtaxi.android.moscowtaxi.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

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
}
