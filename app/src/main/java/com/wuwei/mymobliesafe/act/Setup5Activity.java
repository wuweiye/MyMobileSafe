package com.wuwei.mymobliesafe.act;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.wuwei.mymobliesafe.R;
import com.wuwei.mymobliesafe.act.base.BaseSetupActivity;
import com.wuwei.mymobliesafe.app.Constants;
import com.wuwei.mymobliesafe.utils.SPUtil;

public class Setup5Activity extends BaseSetupActivity {

    private CheckBox cb_box;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup4);
        initUI();
    }

    private void initUI() {
        cb_box = (CheckBox) findViewById(R.id.cb_box);
        boolean open_security = SPUtil.getBoolean(getApplicationContext(),Constants.OPEN_SECURITY,false);
        cb_box.setChecked(open_security);
        if(open_security)
            cb_box.setText("安全设置已经开启");
        else
            cb_box.setText("安全设置未开启");
        cb_box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SPUtil.putBoolean(getApplicationContext(),Constants.OPEN_SECURITY,isChecked);
                if(isChecked)
                    cb_box.setText("安全设置已经开启");
                else
                    cb_box.setText("安全设置未开启");
            }
        });

    }

    @Override
    public void showNextPage() {
        startActivity(new Intent(this,SetupOverActivity.class));
        finish();
        SPUtil.putBoolean(getApplicationContext(),Constants.SETUP_OVER,true);
        overridePendingTransition(R.anim.next_in_anim,R.anim.next_out_anim);
    }

    @Override
    public void showPrePage() {
        startActivity(new Intent(this,Setup4Activity.class));
        finish();

        overridePendingTransition(R.anim.pre_in_anim,R.anim.pre_out_anim);
    }


}
