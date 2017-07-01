package com.wuwei.mymobliesafe.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * Created by 无为 on 2017/3/15.
 */


public class PackageUtils {


    /**
     * * 获取版本号
     * @param context
     * @return versionName
     */

    public static String getVersionName(Context context) {

        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo info = packageManager.getPackageInfo(context.getPackageName(),0);
            String versionName = info.versionName;
            return versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
            return "";

    }

    /**
     * 返回 versionCode
     * @param context
     * @return versionCode
     */
    public static int getVersionCode(Context context){
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo info = packageManager.getPackageInfo(context.getPackageName(),0);
            int versionCode = info.versionCode;
            return  versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;

    }
}
