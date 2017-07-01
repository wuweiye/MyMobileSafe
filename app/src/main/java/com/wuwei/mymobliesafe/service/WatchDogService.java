package com.wuwei.mymobliesafe.service;

import android.app.ActivityManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObservable;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.wuwei.mymobliesafe.act.EnterPsdActivity;
import com.wuwei.mymobliesafe.dao.AppLockDao;

import java.util.List;

public class WatchDogService extends Service {

    private boolean isWatch;
    private AppLockDao mDao;
    private List<String> mPacknameList;

    private InnerReceiver innerReceiver;
    private String skip;
    private MyContentObserver myContentObserver;

    @Override
    public void onCreate() {

       // Log.d("TAG","看门狗开启");
        mDao = AppLockDao.getInstance(this);
        isWatch = true;
        watch();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.skip");
        innerReceiver = new InnerReceiver();
        registerReceiver(innerReceiver,intentFilter);

        myContentObserver = new MyContentObserver(new Handler());
        getContentResolver().registerContentObserver(Uri.parse("content://applock/change"),true, myContentObserver);
        super.onCreate();
    }

    class MyContentObserver extends ContentObserver{

        public MyContentObserver(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
            new Thread(){
                @Override
                public void run() {
                    mPacknameList = mDao.findAll();
                    Log.d("TAG","change---------");
                }
            }.start();
            super.onChange(selfChange);
        }
    }
    class InnerReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {

            skip = intent.getStringExtra("packagename");
        }
    }
    private void watch() {
        new Thread(){
            @Override
            public void run() {

                mPacknameList = mDao.findAll();
                while (isWatch){
                    ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
                    List<ActivityManager.RunningTaskInfo> runningTasks =  am.getRunningTasks(1);
                    ActivityManager.RunningTaskInfo runningTask = runningTasks.get(0);
                    String packagename = runningTask.topActivity.getPackageName();
                    Log.d("TAG",packagename+"---------");
                    if(mPacknameList.contains(packagename)){
                        if (!packagename.contains(skip)){
                            Intent intent = new Intent(getApplicationContext(),EnterPsdActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("packagename",packagename);
                            startActivity(intent);
                        }

                    }
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onDestroy() {

        isWatch = false;
        if(innerReceiver!=null){
            unregisterReceiver(innerReceiver);
        }
        if(myContentObserver!= null){
            getContentResolver().unregisterContentObserver(myContentObserver);
        }
        super.onDestroy();
    }

}
