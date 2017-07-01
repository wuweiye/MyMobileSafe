package com.wuwei.mymobliesafe.act;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.wuwei.mymobliesafe.R;
import com.wuwei.mymobliesafe.act.base.BaseSetupActivity;
import com.wuwei.mymobliesafe.app.Constants;
import com.wuwei.mymobliesafe.utils.SPUtil;

public class Setup3Activity extends BaseSetupActivity {

    private EditText et_phone_number;
    private Button bt_select_number;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup3);
        initUI();
    }

    private void initUI() {

        et_phone_number = (EditText) findViewById(R.id.et_phone_number);
        bt_select_number = (Button) findViewById(R.id.bt_select_number);
        bt_select_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),ContactListActivity.class);
                startActivityForResult(intent,0);
            }
        });
    }

    @Override
    public void showNextPage() {

        SPUtil.putString(getApplicationContext(), Constants.CONTACT_PHONE,et_phone_number.getText().toString());
        startActivity(new Intent(this,Setup4Activity.class));

        finish();
        overridePendingTransition(R.anim.next_in_anim,R.anim.next_out_anim);
    }

    @Override
    public void showPrePage() {


        startActivity(new Intent(this,Setup2Activity.class));

        finish();
        overridePendingTransition(R.anim.pre_in_anim,R.anim.pre_out_anim);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
       if(data!=null){
           String phone = data.getStringExtra("phone");
           phone.replace(" ","").replace("-","").trim();
           et_phone_number.setText(phone);

       }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
