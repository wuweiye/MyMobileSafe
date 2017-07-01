package com.wuwei.mymobliesafe.act;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.wuwei.mymobliesafe.R;
import com.wuwei.mymobliesafe.app.Constants;
import com.wuwei.mymobliesafe.service.AddressService;
import com.wuwei.mymobliesafe.service.BlackNumberService;
import com.wuwei.mymobliesafe.service.WatchDogService;
import com.wuwei.mymobliesafe.utils.SPUtil;
import com.wuwei.mymobliesafe.utils.ServiceUtil;
import com.wuwei.mymobliesafe.utils.ToastUtil;
import com.wuwei.mymobliesafe.view.SettingItem2View;
import com.wuwei.mymobliesafe.view.SettingItemView;

public class Setting2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting2_action);

        initUpdate();

        initAddress();

        initBlacknumber();
        initAppLock();
    }

    private void initAppLock() {
        final SettingItem2View siv_app_lock = (SettingItem2View) findViewById(R.id.siv_app_lock);
        boolean isRuning = ServiceUtil.isRuning(this,"com.wuwei.mymobliesafe.service.WatchDogService");
        siv_app_lock.setCheck(isRuning);

        siv_app_lock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean isCheck = siv_app_lock.isTaggle();
                siv_app_lock.setCheck(!isCheck);
                if(!isCheck){
                    startService(new Intent(getApplicationContext(),WatchDogService.class));
                }else {
                    stopService(new Intent(getApplicationContext(),WatchDogService.class));
                }
            }
        });
    }

    private void initBlacknumber() {
        final SettingItem2View siv_blacknumber = (SettingItem2View) findViewById(R.id.siv_blacknumber);
        boolean isRuning = ServiceUtil.isRuning(this,"com.wuwei.mymobliesafe.service.BlackNumberService");
        siv_blacknumber.setCheck(isRuning);
        siv_blacknumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isCheck = siv_blacknumber.isTaggle();
                siv_blacknumber.setCheck(!isCheck);
                if(!isCheck){
                    ToastUtil.show(getApplicationContext(),"startSevice");
                    //Log.d("TAG","startSevice");
                    startService(new Intent(getApplicationContext(),BlackNumberService.class));

                }else{
                    stopService(new Intent(getApplicationContext(),BlackNumberService.class));
                }
            }
        });
    }

    private void initAddress() {
        final SettingItem2View siv_address = (SettingItem2View) findViewById(R.id.siv_address);

        boolean isRuning = ServiceUtil.isRuning(this,"com.wuwei.mymobliesafe.service.AddressService");
        siv_address.setCheck(isRuning);
        siv_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isCheck = siv_address.isTaggle();
                siv_address.setCheck(!isCheck);
                if(!isCheck){
                    ToastUtil.show(getApplicationContext(),"startSevice");
                    Log.d("TAG","startSevice");
                    startService(new Intent(getApplicationContext(),AddressService.class));

                }else{
                    stopService(new Intent(getApplicationContext(),AddressService.class));
                }
            }
        });
    }

    private void initUpdate() {
        final SettingItem2View sit_update = (SettingItem2View) findViewById(R.id.sit_update);
        boolean isCheck = SPUtil.getBoolean(getApplicationContext(), Constants.ISUPDATE,false);
        sit_update.setCheck(isCheck);
        sit_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isCheck = sit_update.isTaggle();
                sit_update.setCheck(!isCheck);
                SPUtil.putBoolean(getApplicationContext(),Constants.ISUPDATE,!isCheck);
            }
        });
    }


}
