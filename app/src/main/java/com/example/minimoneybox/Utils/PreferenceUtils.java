package com.example.minimoneybox.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.example.minimoneybox.R;

/*
class to update and get user preference
 */
public final class PreferenceUtils {

    public static String getBrokerToken(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);

        String key = context.getString(R.string.pref_bearer_token_key);

        return sp.getString(key, "");
    }

    public static void setBrokerToken(Context context, String value) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);

        String key = context.getString(R.string.pref_bearer_token_key);

        SharedPreferences.Editor editor = sp.edit();

        editor.putString(key, value);
        editor.commit();
    }

    public static String getName(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);

        String key = context.getString(R.string.pref_name_key);

        return sp.getString(key, "");
    }

    public static void setName(Context context, String value) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);

        String key = context.getString(R.string.pref_name_key);

        SharedPreferences.Editor editor = sp.edit();

        editor.putString(key, value);
        editor.commit();
    }
}
