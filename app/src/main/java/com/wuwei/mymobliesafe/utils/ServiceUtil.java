package com.wuwei.mymobliesafe.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ServiceInfo;
import android.util.Log;

import com.wuwei.mymobliesafe.R;
import com.wuwei.mymobliesafe.bean.MyServiceInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 无为 on 2017/5/15.
 */

public class ServiceUtil {

    public static boolean isRuning(Context cxt ,String serviceName){
        ActivityManager mAM = (ActivityManager) cxt.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> runningServiceInfos = mAM.getRunningServices(100);
        //Log.d("TAG","runningServiceInfos.size："+runningServiceInfos.size());
        for (ActivityManager.RunningServiceInfo runningServiceInfo :runningServiceInfos){
            //Log.d("TAG",runningServiceInfo.service.getClassName().toString());
            if(serviceName.equals(runningServiceInfo.service.getClassName())){
                return  true;
            }
        }

        return false;
    }

    public static int getServiceCount(Context context){
        ActivityManager am = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> runningServiceInfos = am.getRunningServices(100);

        if(runningServiceInfos!=null)
        return runningServiceInfos.size();

        return 0;

    }
    public static  List<MyServiceInfo> getService(Context context){

        PackageManager pm = context.getPackageManager();
        ActivityManager am = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> runningServiceInfos = am.getRunningServices(100);
        List<MyServiceInfo> myServiceInfos = new ArrayList<>();
        for(ActivityManager.RunningServiceInfo runningServiceInfo :runningServiceInfos){

            MyServiceInfo myServiceInfo = new MyServiceInfo();
            myServiceInfo.packName = runningServiceInfo.service.getPackageName() ;
            Log.d("TAG",myServiceInfo.getPackName()+"-----");

            try {
                ApplicationInfo applicationInfo = pm.getApplicationInfo(runningServiceInfo.service.getPackageName(),0);
                myServiceInfo.name = applicationInfo.loadLabel(pm).toString();
                myServiceInfo.icon = applicationInfo.loadIcon(pm);

                if ((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) ==ApplicationInfo.FLAG_SYSTEM){
                    myServiceInfo.isSystem = true;
                }else {
                    myServiceInfo.isSystem = false;
                }

            } catch (PackageManager.NameNotFoundException e) {
                myServiceInfo.name = runningServiceInfo.service.getPackageName();
                myServiceInfo.icon = context.getResources().getDrawable(R.mipmap.ic_launcher);
                myServiceInfo.isSystem = true;
                e.printStackTrace();
            }

            myServiceInfos.add(myServiceInfo);
        }


        return myServiceInfos;
    }
}
