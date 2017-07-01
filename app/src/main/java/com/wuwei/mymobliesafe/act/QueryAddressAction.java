package com.wuwei.mymobliesafe.act;

import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.wuwei.mymobliesafe.R;
import com.wuwei.mymobliesafe.bean.AddressDao;
import com.wuwei.mymobliesafe.utils.ToastUtil;

public class QueryAddressAction extends AppCompatActivity {

    private EditText et_phone;
    private TextView tv_query_result;
    private Button bt_result;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
           tv_query_result.setText(mAdress);

        }
    };
    private String mAdress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_address_action);

        initUI();
    }

    private void initUI() {

        et_phone = (EditText) findViewById(R.id.et_phone);
        tv_query_result = (TextView) findViewById(R.id.tv_query_result);
        bt_result = (Button) findViewById(R.id.bt_query);

        bt_result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone  = et_phone.getText().toString();
                if(!TextUtils.isEmpty(phone)){
                    query(phone);
                }else{
                    //抖动效果
                    Animation shake = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.shake);
                    et_phone.startAnimation(shake);
                    Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                    vibrator.vibrate(2000);
                    //震动规律 （不震动，震动），重复
                    vibrator.vibrate(new long[]{2000,5000},2);
                    ToastUtil.show(getApplicationContext(), "phone 不能为空");
                }

            }
        });
        et_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                query(et_phone.getText().toString());
            }
        });


    }

    protected void query(final String phone) {

        new Thread(){
            @Override
            public void run() {
                mAdress = AddressDao.getAddress(phone);
                mHandler.sendEmptyMessage(0);
            }
        }.start();
    }
}
