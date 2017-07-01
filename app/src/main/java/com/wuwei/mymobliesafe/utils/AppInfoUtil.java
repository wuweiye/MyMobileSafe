package com.wuwei.mymobliesafe.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.wuwei.mymobliesafe.bean.AppInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 无为 on 2017/5/16.
 * 获取手机app信息
 */

public class AppInfoUtil {

    /**
     * 返回手机所有应用相关信息
     * @param context 获取包管理上下文
     */
    public static List<AppInfo> getAppInfoList(Context context){

        PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);

        List<AppInfo> appInfos = new ArrayList<>();

        for (PackageInfo p : packageInfos){
            AppInfo appInfo = new AppInfo();
            appInfo.packageName = p.packageName;

            ApplicationInfo applicationInfo = p.applicationInfo;
            appInfo.icon = applicationInfo.loadIcon(packageManager);

            appInfo.name = applicationInfo.loadLabel(packageManager).toString();
            //判断是否是系统应用
            if((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == ApplicationInfo.FLAG_SYSTEM){
                appInfo.isSystem = true;
            }else {
                appInfo.isSystem = false;
            }

            if((applicationInfo.flags & ApplicationInfo.FLAG_EXTERNAL_STORAGE)==ApplicationInfo.FLAG_EXTERNAL_STORAGE){
                appInfo.isSdCard = true;
            }else {
                appInfo.isSdCard = false;
            }

            //Log.d("TAG",appInfo.toString());
            appInfos.add(appInfo);
        }

        return appInfos;
    }
}
