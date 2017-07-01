package com.wuwei.mymobliesafe.act;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.wuwei.mymobliesafe.R;
import com.wuwei.mymobliesafe.receiver.DeviceAdmin;
import com.wuwei.mymobliesafe.utils.ToastUtil;

public class DeviceAction extends AppCompatActivity {



    private Button btLock;
    private Button btWipedata;
    private Button btUnistall;
    private Button btStart;
    private ComponentName mDeviceAdminSample;
    private DevicePolicyManager mDPM;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_action);
        initUI();




    }

    private void initUI() {
        btStart = (Button) findViewById(R.id.bt_start);
        btUnistall = (Button) findViewById(R.id.bt_unistall);
        btWipedata = (Button) findViewById(R.id.bt_wipedata);
        btLock =  (Button) findViewById(R.id.bt_lock);

        mDeviceAdminSample = new ComponentName(this, DeviceAdmin.class);

        mDPM = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);


        btStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN,mDeviceAdminSample);
                intent.putExtra(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN,"设备管理器");
                startActivity(intent);
            }
        });
        btLock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mDPM.isAdminActive(mDeviceAdminSample)){
                    mDPM.lockNow();
                    mDPM.resetPassword("123",0);
                }else {
                    ToastUtil.show(getApplicationContext(),"请激活再使用");
                }

            }
        });

        btWipedata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mDPM.isAdminActive(mDeviceAdminSample)){
                   // mDPM.wipeData(0);
                    ToastUtil.show(getApplicationContext(),"清除数据");
                }else {
                    ToastUtil.show(getApplicationContext(),"请激活再使用");
                }

            }
        });
        btUnistall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("android.intent.action.DELETE");
                intent.addCategory("android.intent.category.DEFAULT");
                intent.setData(Uri.parse("package:"+"com.dingkunming.myproject"));
                startActivity(intent);
            }
        });
    }


}
