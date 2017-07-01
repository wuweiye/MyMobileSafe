package com.wuwei.mymobliesafe.service;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.wuwei.mymobliesafe.R;

/**
 * Created by 无为 on 2017/5/15.
 */

public class AddressService extends Service {

    private TelephonyManager mTM;
    private MyphoneStateListener myphoneStateListener;
    private WindowManager.LayoutParams mParams;
    private WindowManager mWM;
    private View mViewToast;
    Button mFloatView;
    private LinearLayout mFloatLayout;

    @Override
    public void onCreate() {

        //showToast("1234");
        mTM = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);

        myphoneStateListener = new MyphoneStateListener();
        mTM.listen(myphoneStateListener,PhoneStateListener.LISTEN_CALL_STATE);

        super.onCreate();
    }

    class MyphoneStateListener extends PhoneStateListener{
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            switch (state){
                case TelephonyManager.CALL_STATE_IDLE:

                    if (mWM!=null && mViewToast!=null){
                        mWM.removeView(mViewToast);
                    }
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    break;
                case TelephonyManager.CALL_STATE_RINGING:
                    showToast(incomingNumber);
                    break;
            }
            super.onCallStateChanged(state, incomingNumber);
        }
    }

    private void showToast(String incomingNumber) {

        mParams = new WindowManager.LayoutParams();
        mWM = (WindowManager) getApplication().getSystemService(WINDOW_SERVICE);
        Log.d("TAG","WINDOW_SERVICE"+mWM);
        mParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        mParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        //设置图片格式，效果为背景透明
        mParams.format = PixelFormat.RGBA_8888;
        mParams.type = WindowManager.LayoutParams.TYPE_PHONE;

        mParams.y = 0;
        mParams.x = 0;
        mParams.gravity = Gravity.LEFT | Gravity.TOP;
        LayoutInflater inflater = LayoutInflater.from(getApplication());

        mFloatLayout = (LinearLayout) inflater.inflate(R.layout.toast_view, null);


        //Toast显示效果
        mViewToast = View.inflate(this, R.layout.toast_view,null);
        mWM.addView(mFloatLayout,mParams);



    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        if(mTM!= null&& myphoneStateListener!=null){
            //取消监听
            mTM.listen(myphoneStateListener,PhoneStateListener.LISTEN_NONE);
        }
        super.onDestroy();
    }
}
