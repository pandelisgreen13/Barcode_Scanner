package com.example.padpad.qrcode.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.padpad.qrcode.R;

public class AppSharedPreferences {

    private AppSharedPreferences() {
    }

    private static synchronized SharedPreferences getSharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }


    public static synchronized void setStringList(Context context, String stringList) {
        SharedPreferences preferences = getSharedPreferences(context);
        preferences.edit().putString(context.getString(R.string.preferences_list), stringList).apply();
    }

    public static synchronized String getStringList(Context context) {
        SharedPreferences preferences = getSharedPreferences(context);
        return preferences.getString(context.getString(R.string.preferences_list), "");
    }

}
