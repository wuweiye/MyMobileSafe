package com.wuwei.mymobliesafe.act;

import android.content.Intent;
import android.content.pm.IPackageDataObserver;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.wuwei.mymobliesafe.R;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Formattable;
import java.util.Formatter;
import java.util.List;

public class CacheClearAction extends AppCompatActivity {

    private Button bt_clear;
    private ProgressBar pv_bar;
    private TextView tv_name_UI;
    private LinearLayout ll_add_text;
    private PackageManager pm;
    private static final int UPDATA_CACHE_APP = 100;
    private static final int CHECK_CACHE_APP = 101;
    private static final int CHECK_FINISH = 102;
    private static final int CLEAR_CACHE = 103;
    private int index =0;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case UPDATA_CACHE_APP:
                    View view = View.inflate(getApplicationContext(),R.layout.linearlayout_cache_item,null);

                    ImageView iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
                    TextView tv_name = (TextView) view.findViewById(R.id.tv_name);
                    TextView tv_memory_info = (TextView) view.findViewById(R.id.tv_memory_info);
                    Button bt_delete = (Button) view.findViewById(R.id.bt_delete);


                    final CacheInfo cacheInfo = (CacheInfo) msg.obj;
                    Log.d("TAG","name:"+cacheInfo.name);
                    iv_icon.setImageDrawable(cacheInfo.icon);
                    tv_name.setText(cacheInfo.name);
                    String cache_size = android.text.format.Formatter.formatFileSize(getApplicationContext(),cacheInfo.cacheSize);

                    tv_memory_info.setText(cache_size);
                    ll_add_text.addView(view,0);
                    tv_app_name.setText(cacheInfo.name);
                    tv_cache_size.setText(cache_size);
                    mIv_icon.setImageDrawable(cacheInfo.icon);

                    bt_delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
                            intent.setData(Uri.parse("package:"+cacheInfo.packageName));
                            startActivity(intent);
                            /*
                            //需要Root
                            try {
                                Class<?> clazz = Class.forName("android.content.pm.PackageManager");
                                Method method = clazz.getMethod("deleteApplicationCacheFiles",String.class,IPackageDataObserver.class);
                                method.invoke(pm,cacheInfo.packageName,new IPackageDataObserver.Stub(){

                                    @Override
                                    public void onRemoveCompleted(String packageName, boolean succeeded) throws RemoteException {

                                        Message message = Message.obtain();
                                        message.what = CLEAR_CACHE;
                                        mHandler.sendMessage(message);
                                    }
                                });
                            } catch (Exception e) {
                                tv_name_UI.setText("清除缓存失败。。。");
                                e.printStackTrace();
                            }*/
                        }
                    });
                    break;
                case CHECK_CACHE_APP:
                    tv_name_UI.setText((String) msg.obj);
                    break;
                case CHECK_FINISH:
                    tv_name_UI.setText("扫描完成");
                    break;
                case CLEAR_CACHE:
                    tv_name_UI.setText("清除缓存完成");
                    break;
            }
        }
    };
    private TextView tv_app_name;
    private TextView tv_cache_size;
    private ImageView mIv_icon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cache_clear_action);
        initUI();
        initData();
    }

    private void initData() {

        new Thread(){
            @Override
            public void run() {
                pm = getPackageManager();
                List<PackageInfo> packageInfoList = pm.getInstalledPackages(0);
                pv_bar.setMax(packageInfoList.size());
               // Log.d("TAG","size:"+packageInfoList.size());
                for(PackageInfo packageInfo :packageInfoList){
                    String packageName = packageInfo.packageName;
                    getPackageCache(packageName);

                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Message message = Message.obtain();
                    String name = null;
                    try {
                        name = pm.getApplicationInfo(packageInfo.packageName,0).loadLabel(pm).toString();
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }
                    message.obj= name;
                    message.what = CHECK_CACHE_APP;
                    mHandler.sendMessage(message);
                }

                Message message = Message.obtain();
                message.what = CHECK_FINISH;
                mHandler.sendMessage(message);

            }
        }.start();

    }

    class CacheInfo{
        public String name;
        public Drawable icon;
        public String packageName;
        public long cacheSize;
    }

    private void getPackageCache(String packageName) {
        IPackageStatsObserver.Stub mStatsObserver = new IPackageStatsObserver.Stub(){
            @Override
            public void onGetStatsCompleted(PackageStats pStats, boolean succeeded) throws RemoteException {

                long cacheSize = pStats.cacheSize;
                //Log.d("TAG","cacheSize:"+ android.text.format.Formatter.formatFileSize(getApplicationContext(),cacheSize));
                if(cacheSize>0){
                    Message message = Message.obtain();

                    message.what = UPDATA_CACHE_APP;
                    CacheInfo cacheInfo = new CacheInfo();
                    cacheInfo.cacheSize = cacheSize;
                    cacheInfo.packageName = pStats.packageName;
                    try {
                        cacheInfo.name = pm.getApplicationInfo(pStats.packageName,0).loadLabel(pm).toString();

                        cacheInfo.icon = pm.getApplicationInfo(pStats.packageName,0).loadIcon(pm);
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }
                    message.obj = cacheInfo;
                    mHandler.sendMessage(message);

                }

                index++;
                pv_bar.setProgress(index);

            }
        };

        try {
            Class<?> clazz = Class.forName("android.content.pm.PackageManager");
            Method method = clazz.getMethod("getPackageSizeInfo",String.class,IPackageStatsObserver.class);
            method.invoke(pm,packageName,mStatsObserver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    private void initUI() {

        bt_clear = (Button) findViewById(R.id.bt_clear);
        pv_bar = (ProgressBar) findViewById(R.id.pb_bar);
        tv_name_UI = (TextView) findViewById(R.id.tv_name);
        ll_add_text = (LinearLayout) findViewById(R.id.ll_add);
        tv_app_name = (TextView) findViewById(R.id.tv_app_name);
        tv_cache_size = (TextView) findViewById(R.id.tv_cache_size);
        mIv_icon = (ImageView) findViewById(R.id.iv_icon);
        bt_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_name_UI.setText("清除缓存中。。。");
                try {
                    Class<?> clazz = Class.forName("android.content.pm.PackageManager");
                    Method method = clazz.getMethod("freeStorageAndNotify",long.class,IPackageDataObserver.class);
                    method.invoke(pm,Long.MAX_VALUE,new IPackageDataObserver.Stub(){

                        @Override
                        public void onRemoveCompleted(String packageName, boolean succeeded) throws RemoteException {

                            Message message = Message.obtain();
                            message.what = CLEAR_CACHE;
                            mHandler.sendMessage(message);
                        }
                    });
                } catch (Exception e) {
                    tv_name_UI.setText("清除缓存失败。。。");
                    e.printStackTrace();
                }
            }
        });
    }
}
