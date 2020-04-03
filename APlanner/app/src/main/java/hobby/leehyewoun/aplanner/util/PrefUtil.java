package hobby.leehyewoun.aplanner.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;

public class PrefUtil {
    private static Dialog mProgDig;

    //저장하는 Preference..String
    public static void setData(Context context, String key, String value) {
        SharedPreferences pref =
                context.getSharedPreferences("test1", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        editor.commit();
    }

    //불러오는 Preference..String
    public static String getData(Context context, String key) {
        SharedPreferences pref =
                context.getSharedPreferences("test1", Activity.MODE_PRIVATE);
        return pref.getString(key, "");
    }

    //저장하는 Preference..Int
    public static void setData(Context context, String key, int value) {
        SharedPreferences pref =
                context.getSharedPreferences("test1", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    //불러오는 Preference..Int
    public static int getDataInt(Context context, String key) {
        SharedPreferences pref =
                context.getSharedPreferences("test1", Activity.MODE_PRIVATE);
        return pref.getInt(key,1);
    }
    //불러오는 Preference..Int
    public static int getDataInt0(Context context, String key) {
        SharedPreferences pref =
                context.getSharedPreferences("test1", Activity.MODE_PRIVATE);
        return pref.getInt(key,0);
    }

    //저장하는 Preference..Boolean
    public static void setData(Context context, String key, boolean value) {
        SharedPreferences pref =
                context.getSharedPreferences("test1", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    //불러오는 Preference..Boolean
    public static boolean getDataBoolean(Context context, String key) {
        SharedPreferences pref =
                context.getSharedPreferences("test1", Activity.MODE_PRIVATE);
        return pref.getBoolean(key, false);
    }

}
