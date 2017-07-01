package com.wuwei.mymobliesafe.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

import com.wuwei.mymobliesafe.utils.ProcessInfoUtil;

public class LockService extends Service {

    private IntentFilter intentFilter;
    private InnerReceiver innerReceiver;


    @Override
    public void onCreate() {
        intentFilter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
        innerReceiver = new InnerReceiver();
        registerReceiver(innerReceiver,intentFilter);
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        if(innerReceiver != null){
            unregisterReceiver(innerReceiver);
        }
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    class InnerReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {

            ProcessInfoUtil.killAll(context);
        }
    }
}
