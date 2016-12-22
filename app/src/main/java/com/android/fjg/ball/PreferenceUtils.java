package com.android.fjg.ball;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;

/**
 * Created by fujianguo on 16-12-21.
 */

public class PreferenceUtils {

    private static final String NAME = "quickball";

    public static final String FLOATVIEW = "floatball";

    public static void setBoolean(String key, boolean value) {
        SharedPreferences pref = getSharedContext().getSharedPreferences(NAME,
                Context.MODE_PRIVATE | Context.MODE_MULTI_PROCESS);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static boolean getBoolean(String key) {
        SharedPreferences pref = getSharedContext().getSharedPreferences(NAME,
                Context.MODE_PRIVATE | Context.MODE_MULTI_PROCESS);
        return pref.getBoolean(key, true);
    }

    public static Context getSharedContext() {
        try {
            return QuickBallApplication.getInstance().createPackageContext(
                    "com.android.fjg.ball", Context.CONTEXT_IGNORE_SECURITY);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


}
