package com.wuwei.mymobliesafe.act;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.lzyzsd.circleprogress.ArcProgress;
import com.wuwei.mymobliesafe.R;
import com.wuwei.mymobliesafe.dao.VirusDao;
import com.wuwei.mymobliesafe.utils.MD5Util;
import com.wuwei.mymobliesafe.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

public class AnitVirusActivity extends AppCompatActivity {

    private LinearLayout ll_add_text;
    private int index = 0;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what){
                case SCANING:

                    View view = View.inflate(getApplicationContext(),R.layout.linearlayout_anitvirus_item,null);

                    ImageView iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
                    TextView tv_name = (TextView) view.findViewById(R.id.tv_name);
                    TextView tv_isVirus = (TextView) view.findViewById(R.id.tv_memory_info);


                    ScanInfo info  = (ScanInfo) msg.obj;
                    //tv_name.setText(info.name);
                    tv_app_package_name.setText("扫描中:"+info.name);

                    tv_name.setText(info.name);
                    if(info.isVirus){
                        tv_isVirus.setTextColor(Color.RED);
                        tv_isVirus.setText("发现病毒");
                    }else {
                        tv_isVirus.setTextColor(Color.GREEN);
                        tv_isVirus.setText("扫描安全");
                    }
                    iv_icon.setImageDrawable(info.icon);

                    int index = msg.arg1;
                    mAP_progress.setProgress(index);
                    ll_add_text.addView(view,0);
                    break;
                case SCAN_FINISH:
                    mAP_progress.setBottomText("扫描完成");
                    tv_app_package_name.setText("");
                    //tv_name.setText("扫描完成");
                    //iv_scanning.clearAnimation();
                    if(mVirusScanInfoList.size()>0){
                        ToastUtil.show(getApplicationContext(),"发现病毒");
                    }
                    break;

            }
        }
    };
    private static final int SCANING = 100;
    private static final int SCAN_FINISH = 101;
    private List<ScanInfo> mVirusScanInfoList;
    private ArcProgress mAP_progress;
    private TextView tv_app_package_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anit_virus);
        initUI();
        initAnimation();
        checkVirus();
    }

    protected void unInstallVirus(){
        for (ScanInfo scanInfo : mVirusScanInfoList){
            String packageName = scanInfo.packageName;
            Intent intent = new Intent("android.intent.action.DELETE");
            intent.addCategory("android.intent.category.DEFAULT");
            intent.setData(Uri.parse("package:"+packageName));
            startActivity(intent);
        }
    }
    private void checkVirus() {
        new Thread(){
            @Override
            public void run() {
                PackageManager pm = getPackageManager();
                List<PackageInfo> packageInfoList = pm.getInstalledPackages(
                        PackageManager.GET_SIGNATURES+PackageManager.GET_UNINSTALLED_PACKAGES);
                List<String> virusList = VirusDao.getVirusList();
                mVirusScanInfoList = new ArrayList<>();
                List<ScanInfo> scanInfoList = new ArrayList<>();
                int max = packageInfoList.size();
                //pb_bar.setMax(max);
                for(PackageInfo packageInfo : packageInfoList){
                    ScanInfo scanInfo = new ScanInfo();
                    Signature[] sigatures = packageInfo.signatures;
                    Signature sigature = sigatures[0];
                    String string = sigature.toCharsString();
                    String encoder = MD5Util.encrypt(string);
                    if (virusList.contains(encoder)){
                        scanInfo.isVirus = true;
                        mVirusScanInfoList.add(scanInfo);
                    }else {
                        scanInfo.isVirus = false;
                    }
                    scanInfo.packageName = packageInfo.packageName;
                    scanInfo.name = packageInfo.applicationInfo.loadLabel(pm).toString();
                    scanInfo.icon = packageInfo.applicationInfo.loadIcon(pm);
                    scanInfoList.add(scanInfo);
                    index++;
                    int  mCurrentprogress = (int) (index * 100f /max);
                    //mAP_progress.setProgress(mCurrentprogress);

                    //pb_bar.setProgress(index);
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Message message = Message.obtain();
                    message.what = SCANING;
                    message.obj = scanInfo;
                    message.arg1 = mCurrentprogress;
                    handler.sendMessage(message);
                }
                Message message = Message.obtain();
                message.what = SCAN_FINISH;
                handler.sendMessage(message);
            }
        }.start();

    }

    class ScanInfo{
        public Drawable icon;
        public boolean isVirus;
        public String packageName;
        public String name;
    }

    private void initAnimation() {
       /* RotateAnimation rotate = new RotateAnimation(
                0,360,
                Animation.RELATIVE_TO_SELF,0.5f,
                Animation.RELATIVE_TO_SELF,0.5f
        );
        rotate.setDuration(1000);

        rotate.setRepeatCount(Animation.INFINITE);
        rotate.setFillAfter(true);*/
       // iv_scanning.startAnimation(rotate);
    }

    private void initUI() {
        //iv_scanning = (ImageView) findViewById(R.id.iv_scanning);
        //tv_name = (TextView) findViewById(R.id.tv_name);
        //pb_bar = (ProgressBar) findViewById(R.id.pb_bar);
        ll_add_text = (LinearLayout) findViewById(R.id.ll_add_text);
        mAP_progress = (ArcProgress) findViewById(R.id.tv_progress);
        tv_app_package_name = (TextView) findViewById(R.id.tv_app_package_name);
    }



    private class ScanTask extends AsyncTask<Void,ScanInfo,Void>{

        @Override
        protected Void doInBackground(Void... params) {
            return null;
        }
    }



}
