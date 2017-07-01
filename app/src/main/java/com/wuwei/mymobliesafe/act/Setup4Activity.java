package com.wuwei.mymobliesafe.act;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.wuwei.mymobliesafe.R;
import com.wuwei.mymobliesafe.act.base.BaseSetupActivity;
import com.wuwei.mymobliesafe.app.Constants;
import com.wuwei.mymobliesafe.receiver.DeviceAdmin;
import com.wuwei.mymobliesafe.utils.SPUtil;

public class Setup4Activity extends BaseSetupActivity {

    private ComponentName mDeviceAdminSample;
    private DevicePolicyManager mDPM;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup5);
        initUI();
    }

    private void initUI() {

        Button bt_activate = (Button) findViewById(R.id.bt_activate);
        mDeviceAdminSample = new ComponentName(this, DeviceAdmin.class);

        mDPM = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);

        bt_activate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN,mDeviceAdminSample);
                intent.putExtra(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN,"设备管理器");
                startActivity(intent);
            }
        });
    }

    @Override
    public void showNextPage() {

        //SPUtil.putString(getApplicationContext(), Constants.CONTACT_PHONE,et_phone_number.getText().toString());
        startActivity(new Intent(this,Setup5Activity.class));

        finish();
        overridePendingTransition(R.anim.next_in_anim,R.anim.next_out_anim);

    }

    @Override
    public void showPrePage() {

        startActivity(new Intent(this,Setup3Activity.class));

        finish();
        overridePendingTransition(R.anim.pre_in_anim,R.anim.pre_out_anim);

    }
}
