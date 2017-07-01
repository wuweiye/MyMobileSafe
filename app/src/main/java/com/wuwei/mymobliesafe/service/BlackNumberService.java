package com.wuwei.mymobliesafe.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.widget.ActivityChooserView;
import android.telephony.PhoneStateListener;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;

import com.android.internal.telephony.ITelephony;
import com.wuwei.mymobliesafe.dao.BlackNumberDao;

import java.lang.reflect.Method;

/**
 * Created by 无为 on 2017/5/15.
 */

public class BlackNumberService extends Service {
    private InnerSmsReceiver mInnerSmsReceiver;
    private BlackNumberDao mDao;
    private TelephonyManager mTM;
    private MyPhoneStateListener mPhoneStateListener;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        mDao = BlackNumberDao.getInstance(getApplicationContext());
        //监听短信
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
        intentFilter.setPriority(1000);
        mInnerSmsReceiver = new InnerSmsReceiver();
        registerReceiver(mInnerSmsReceiver,intentFilter);

        //监听电话
        mTM = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        mPhoneStateListener = new MyPhoneStateListener();
        mTM.listen(mPhoneStateListener,PhoneStateListener.LISTEN_CALL_STATE);
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        if(mInnerSmsReceiver!=null){
            unregisterReceiver(mInnerSmsReceiver);
        }
        if(mTM!=null && mPhoneStateListener!= null){
            mTM.listen(mPhoneStateListener,PhoneStateListener.LISTEN_NONE);
        }
        super.onDestroy();
    }
    class MyPhoneStateListener extends PhoneStateListener{
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            switch (state){
                case TelephonyManager.CALL_STATE_IDLE:
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    break;
                case TelephonyManager.CALL_STATE_RINGING:
                    //挂断电话
                    endCall(incomingNumber);
                    break;
            }
            super.onCallStateChanged(state, incomingNumber);
        }
    }

    private void endCall(String phone) {
        int mode = mDao.getMode(phone);
        if (mode == 2 || mode == 3){

            try {
                Class<?> c = TelephonyManager.class;
                Method method = c.getDeclaredMethod("getITelephony",(Class[])null);
                method.setAccessible(true);
                ITelephony iTelephony = null;
                iTelephony = (ITelephony) method.invoke(mTM,(Object[])null);
                iTelephony.endCall();
                //Class<?> clazz = Class.forName("android.os.ServiceManager");
                //Method method = clazz.getMethod("getService",String.class);
                //IBinder iBinder = (IBinder) method.invoke(null,Context.TELECOM_SERVICE);
                //IPhoneSubInfo ip = IPhoneSubInfo.Stub.asInterface(iBinder);

                //sip.endCall();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //getContentResolver().delete(Uri.parse("content://call_log/calls"),"number = ?",new String[]{phone});

    }

    class InnerSmsReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            Object[] objects = (Object[]) intent.getExtras().get("pdus");
            for (Object object :objects){
                SmsMessage sms = SmsMessage.createFromPdu((byte[])object);



                String originatingAddress = sms.getOriginatingAddress();
                String messageBody =sms.getMessageBody();

                String regex = "^1[3-8]\\d{9}";
                while (!originatingAddress.matches(regex)){
                    originatingAddress = originatingAddress.substring(1,originatingAddress.length());
                    if (originatingAddress.length() < 11)
                        break;

                }

                int mode = mDao.getMode(originatingAddress);
                if(mode == 1 || mode == 3){
                    abortBroadcast();

                }

            }
        }
    }

}
