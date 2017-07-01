package com.wuwei.mymobliesafe.act;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.wuwei.mymobliesafe.R;
import com.wuwei.mymobliesafe.app.Constants;
import com.wuwei.mymobliesafe.service.LockService;
import com.wuwei.mymobliesafe.utils.SPUtil;
import com.wuwei.mymobliesafe.utils.ServiceUtil;

public class ProcessSettingActivity extends AppCompatActivity {

    private  CheckBox cb_show_system,cb_lock_clear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process_setting);


        initSystemShow();
        initLockScreenClear();
    }

    private void initLockScreenClear() {
        cb_lock_clear = (CheckBox) findViewById(R.id.cb_lock_clear);
        boolean isRunning = ServiceUtil.isRuning(this,"com.wuwei.mymobliesafe.service.LockService");
        cb_lock_clear.setChecked(isRunning);
        if(isRunning){
            cb_lock_clear.setText("锁屏清理已开启");
        }else {
            cb_lock_clear.setText("锁屏清理已关闭");
        }
        cb_lock_clear.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    cb_lock_clear.setText("锁屏清理已开启");
                    startService(new Intent(getApplicationContext(),LockService.class));
                }else {
                    cb_lock_clear.setText("锁屏清理已关闭");
                    stopService(new Intent(getApplicationContext(),LockService.class));
                }

            }
        });
    }

    private void initSystemShow() {
        cb_show_system = (CheckBox) findViewById(R.id.cb_show_system);
        boolean isChecked = SPUtil.getBoolean(getApplicationContext(),Constants.SHOW_SYSTEM,true);
        cb_show_system.setChecked(isChecked);
        if(isChecked){
            cb_show_system.setText("显示系统进程");
        }else {
            cb_show_system.setText("隐藏系统进程");
        }
        cb_show_system.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    cb_show_system.setText("显示系统进程");
                }else {
                    cb_show_system.setText("隐藏系统进程");
                }
                SPUtil.putBoolean(getApplicationContext(), Constants.SHOW_SYSTEM,isChecked);
            }
        });
    }
}
