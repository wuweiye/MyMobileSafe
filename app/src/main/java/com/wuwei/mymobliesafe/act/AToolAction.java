package com.wuwei.mymobliesafe.act;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.wuwei.mymobliesafe.R;
import com.wuwei.mymobliesafe.utils.Smsutil;
import com.wuwei.mymobliesafe.utils.ToastUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class AToolAction extends AppCompatActivity {


    private TextView tv_query_phone_address,tv_app_lock;
    private ProgressBar pb_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atool_action);
        initDB("address.db");

        initUI();

        initSmsBackup();
        initApplock();
        initService();
        //initSendSms();

    }

    private void initService() {
        TextView tv_service  = (TextView) findViewById(R.id.tv_service);
        tv_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),ServiceClearAction.class);
                startActivity(intent);
            }
        });
    }

    /*private void initSendSms() {


        TextView tv_sms_send = (TextView) findViewById(R.id.tv_sms_send);
        tv_sms_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                send();
                            }
        });

    }*/

    /*private void send(){
        String SENT_SMS_ACTION = "sms_sent";
        String DELIVERED  = "delivered";
        Intent sentIntent = new Intent(SENT_SMS_ACTION);
        Intent deliveredIntent = new Intent(DELIVERED);
        PendingIntent sentPI = PendingIntent.getBroadcast(this,0,sentIntent,0);

        PendingIntent deliveredPI = PendingIntent.getBroadcast(this,0,deliveredIntent,0);

        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (getResultCode()){
                    case Activity.RESULT_OK:
                        ToastUtil.show(context,"Activity.RESULT_OK");
                    break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        ToastUtil.show(context,"RESULT_ERROR_GENERIC_FAILURE");
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        ToastUtil.show(context,"RESULT_ERROR_NO_SERVICE");
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        ToastUtil.show(context,"RESULT_ERROR_NULL_PDU");
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        ToastUtil.show(context,"RESULT_ERROR_RADIO_OFF");
                        break;
                }
            }
        },new IntentFilter(SENT_SMS_ACTION));

        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (getResultCode()){
                    case Activity.RESULT_OK:
                        ToastUtil.show(context,"----Activity.RESULT_OK");
                        break;
                    case Activity.RESULT_CANCELED:
                        ToastUtil.show(context,"----Activity.RESULT_CANCELED");
                        break;
                }
            }
        },new IntentFilter(DELIVERED));

        SmsManager smsManager = SmsManager.getDefault();
        // PendingIntent pi = PendingIntent.getActivities(getApplicationContext(),0)
        String smsContent = "1.-----/n------|"+
                "2.---\n----|"+"3.------";
        String phone = "15670698550";
        smsManager.sendTextMessage(phone,null,smsContent,sentPI,deliveredPI);

    };*/
    private void initApplock() {
        tv_app_lock = (TextView) findViewById(R.id.tv_app_lock);
        tv_app_lock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),AppLockAction.class));
            }
        });
    }

    private void initSmsBackup() {
        TextView tv_sms = (TextView) findViewById(R.id.tv_sms_backup);
        pb_bar = (ProgressBar) findViewById(R.id.pb_bar);

        tv_sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSmsBakupDialog();
            }
        });
    }

    private void showSmsBakupDialog() {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setIcon(R.mipmap.ic_launcher);
        dialog.setTitle("SMS备份");
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog.show();

        new Thread(){
            @Override
            public void run() {
                String path = Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator+"sms.xml";
                Smsutil.backup(getApplicationContext(), path, new Smsutil.CallBack() {
                    @Override
                    public void setMax(int max) {
                        dialog.setMax(max);
                        //pb_bar.setMax(max);
                    }
                    @Override
                    public void setProgress(int index) {

                        dialog.setProgress(index);
                        //pb_bar.setProgress(index);
                    }
                });

                dialog.dismiss();
            }
        }.start();
    }

    private void initUI() {
        tv_query_phone_address = (TextView) findViewById(R.id.tv_query_phone_address);

        tv_query_phone_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(QueryAddressAction.class);
            }
        });
    }

    private void startActivity(Class<?> clazz) {

        startActivity(new Intent(getApplicationContext(),clazz));
    }

    private void initDB(String dbName) {
        File files = getFilesDir();
        File file = new File(files,dbName);
        if(file.exists()){
            return;
        }

        InputStream is = null;
         FileOutputStream fos =null;
        //读取
        try {
            is = getAssets().open(dbName);
            fos = new FileOutputStream(file);
            byte[] bs = new byte[1024];
            int temp = -1;
            while ((temp = is.read(bs))!= -1){
                fos.write(bs,0,temp);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(is!=null && fos!=null){
                try {
                    is.close();
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }

    }

}
