package com.wuwei.mymobliesafe.act;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wuwei.mymobliesafe.R;
import com.wuwei.mymobliesafe.app.Constants;
import com.wuwei.mymobliesafe.utils.SPUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SetupOverActivity extends Activity {

    private TextView tv_reset_setup;
    private TextView tv_phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        boolean setup_over = SPUtil.getBoolean(this, Constants.SETUP_OVER, false);
        if (setup_over) {
            setContentView(R.layout.activity_setup_over);
            ButterKnife.bind(this);
            initUI();
        } else {
            startActivity(new Intent(this, Setup1Activity.class));
            finish();
        }




    }

    private void initUI() {

        tv_phone = (TextView) findViewById(R.id.tv_phone);
        String phone = SPUtil.getString(getApplicationContext(),Constants.CONTACT_PHONE,"123456");
        tv_phone.setText(phone);

        tv_reset_setup = (TextView) findViewById(R.id.tv_reset_setup);

        tv_reset_setup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Setup1Activity.class));
            }
        });

    }
}
