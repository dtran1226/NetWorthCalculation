package com.example.thuct.networthtracking.utility;

import android.content.Context;
import android.content.SharedPreferences;

public class Utility {
    final static String EMPTY_STRING = "";
    final static long DEFAULT_LONG = 0;

    public static void saveStringToSharePref(Context context, String sharePrefName, String key, String value) {
        SharedPreferences pref = context.getSharedPreferences(sharePrefName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static void saveLongToSharePref(Context context, String sharePrefName, String key, long value) {
        SharedPreferences pref = context.getSharedPreferences(sharePrefName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putLong(key, value);
        editor.apply();
    }

    public static String getStringFromSharePref(Context context, String sharePrefName, String key) {
        SharedPreferences pref = context.getSharedPreferences(sharePrefName, Context.MODE_PRIVATE);
        if (pref.contains(key)) {
            return pref.getString(key, EMPTY_STRING);
        }
        return EMPTY_STRING;
    }

    public static long getLongFromSharePref(Context context, String sharePrefName, String key) {
        SharedPreferences pref = context.getSharedPreferences(sharePrefName, Context.MODE_PRIVATE);
        if (pref.contains(key)) {
            return pref.getLong(key, DEFAULT_LONG);
        }
        return DEFAULT_LONG;
    }
}
