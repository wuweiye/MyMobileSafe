package com.wuwei.mymobliesafe.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by 无为 on 2017/3/16.
 */

public class SPUtil {

    private static SharedPreferences sp;
    public static String getString(Context context, String key, String value) {
        if(sp == null){
            sp = context.getSharedPreferences("config",Context.MODE_PRIVATE);
        }
        return  sp.getString(key,value);
    }

    public static void putString(Context context, String key, String value) {

        if(sp == null){
            sp = context.getSharedPreferences("config",Context.MODE_PRIVATE);
        }
        sp.edit().putString(key,value).commit();
    }

    public static boolean getBoolean(Context context, String key, boolean value) {
        if(sp == null){
            sp = context.getSharedPreferences("config",Context.MODE_PRIVATE);
        }
        return  sp.getBoolean(key,value);
    }
    public static void putBoolean(Context context, String key, boolean value) {

        if(sp == null){
            sp = context.getSharedPreferences("config",Context.MODE_PRIVATE);
        }
        sp.edit().putBoolean(key,value).commit();
    }


    public static void remove(Context context,String key){
        if(sp == null){
            sp = context.getSharedPreferences("config",Context.MODE_PRIVATE);
        }
        sp.edit().remove(key).commit();
    }
}
