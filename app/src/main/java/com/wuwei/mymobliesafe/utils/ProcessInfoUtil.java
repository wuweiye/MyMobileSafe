package com.wuwei.mymobliesafe.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Debug;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.jaredrummler.android.processes.ProcessManager;
import com.jaredrummler.android.processes.models.AndroidAppProcess;
import com.wuwei.mymobliesafe.R;
import com.wuwei.mymobliesafe.bean.ProcessInfo;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 无为 on 2017/5/25.
 */

public class ProcessInfoUtil {

    public static int getProcessCount(Context context){

        //ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<AndroidAppProcess> processList = ProcessManager.getRunningAppProcesses();
        Log.d("TAG","size:"+processList.size());

        return processList.size();
    }

    public static long getAvailSpace(Context context){
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(memoryInfo);
        return  memoryInfo.availMem;
    }

    /**
     *
     * @param context
     * @return 返回可用内存数 byte
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public static long getTotalSpace(Context context){
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(memoryInfo);

        return memoryInfo.totalMem;
    }

    public static long getTotalSpace2(Context context){

        FileReader fileReader = null;
        BufferedReader reader = null;
        try {
            fileReader = new FileReader("proc/meminfo");
            reader = new BufferedReader(fileReader);
            String lineOne = reader.readLine();
            char[] charArray = lineOne.toCharArray();
            StringBuffer stringBuffer = new StringBuffer();
            for(char c :charArray){
                if(c>='0'&&c<='9')
                    stringBuffer.append(c);
            }

            return Long.parseLong(stringBuffer.toString())*1024;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(fileReader!= null){
                try {
                    fileReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(reader!=null){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return 0;
    }


    public static List<ProcessInfo> getProcessInfo(Context context){
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        PackageManager pm = context.getPackageManager();
        List<ProcessInfo> processInfos = new ArrayList<>();
        List<AndroidAppProcess> runningAppProcessInfos = ProcessManager.getRunningAppProcesses();
        for (AndroidAppProcess info : runningAppProcessInfos){
            ProcessInfo processInfo = new ProcessInfo();
            processInfo.packName = info.getPackageName();
            //获取进程占用内存大小（传递一个进程对应的pid数组）
            Debug.MemoryInfo[] processMemoryInfo = am.getProcessMemoryInfo(new int[]{info.pid});
            Debug.MemoryInfo memoryInfo = processMemoryInfo[0];
            processInfo.memSize = memoryInfo.getTotalPrivateDirty()*1024;

            try {
                ApplicationInfo applicationInfo = pm.getApplicationInfo(processInfo.packName,0);
                processInfo.name = applicationInfo.loadLabel(pm).toString();
                processInfo.icon = applicationInfo.loadIcon(pm);
                if ((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) ==ApplicationInfo.FLAG_SYSTEM){
                    processInfo.isSystem = true;
                }else {
                    processInfo.isSystem = false;
                }
            } catch (PackageManager.NameNotFoundException e) {

                processInfo.name = info.getPackageName();
                processInfo.icon = context.getResources().getDrawable(R.mipmap.ic_launcher);
                processInfo.isSystem = true;
                e.printStackTrace();
            }

            processInfos.add(processInfo);

        }

        return processInfos;
    }

    public static void killprocess(Context context,ProcessInfo processInfo){
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
       am.killBackgroundProcesses(processInfo.packName );

    }

    public static void killAll(Context context){
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<AndroidAppProcess> runningAppProcessInfos = ProcessManager.getRunningAppProcesses();

        for(AndroidAppProcess info :runningAppProcessInfos){
            if(info.getPackageName().equals(context.getPackageName())){
                continue;
            }
            am.killBackgroundProcesses(info.getPackageName());
        }
    }
}
