package com.wuwei.mymobliesafe.act;

import android.content.Intent;
import android.os.Bundle;

import com.wuwei.mymobliesafe.R;
import com.wuwei.mymobliesafe.act.base.BaseSetupActivity;

public class Setup1Activity extends BaseSetupActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup1);
    }



    @Override
    public void showNextPage() {
        startActivity(new Intent(this,Setup2Activity.class));

        overridePendingTransition(R.anim.next_in_anim,R.anim.next_out_anim);

    }

    @Override
    public void showPrePage() {

    }
}
