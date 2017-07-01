package com.wuwei.mymobliesafe.act;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;

import com.wuwei.mymobliesafe.R;
import com.wuwei.mymobliesafe.act.base.BaseSetupActivity;
import com.wuwei.mymobliesafe.app.Constants;
import com.wuwei.mymobliesafe.utils.SPUtil;
import com.wuwei.mymobliesafe.view.SettingItemView;

public class Setup2Activity extends BaseSetupActivity {

    private SettingItemView siv_sim_bound;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup2);
        initUI();
    }

    private void initUI() {
        siv_sim_bound = (SettingItemView) findViewById(R.id.siv_sim_bound);

        final String sim_number = SPUtil.getString(this, Constants.SIM_NUMBER,"");
        if(TextUtils.isEmpty(sim_number)){
            siv_sim_bound.setCheck(false);
        }else {
            siv_sim_bound.setCheck(true);
        }

        siv_sim_bound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isCheck = siv_sim_bound.isChecked();
                siv_sim_bound.setCheck(!isCheck);

                if(!isCheck){
                    //存储
                    TelephonyManager manager=
                            (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                    String simSerialNumber = manager.getSimSerialNumber();
                    SPUtil.putString(getApplicationContext(),Constants.SIM_NUMBER,simSerialNumber);
                }else{
                    //清空
                    SPUtil.remove(getApplicationContext(),Constants.SIM_NUMBER);

                }
            }
        });
    }

    @Override
    public void showNextPage() {
        String serialNumber = SPUtil.getString(this,Constants.SIM_NUMBER,"");
        // if(!TextUtils.isEmpty(serialNumber)){
        startActivity(new Intent(this,Setup3Activity.class));

        finish();
        // }else {

        //   ToastUtil.show(getApplicationContext(),"请绑定sim卡");
        // }

        overridePendingTransition(R.anim.next_in_anim,R.anim.next_out_anim);
    }

    @Override
    public void showPrePage() {

        startActivity(new Intent(this,Setup1Activity.class));

        finish();

        overridePendingTransition(R.anim.pre_in_anim,R.anim.pre_out_anim);
    }


}
