package com.wuwei.mymobliesafe.act;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.wuwei.mymobliesafe.R;
import com.wuwei.mymobliesafe.app.Constants;
import com.wuwei.mymobliesafe.utils.PackageUtils;
import com.wuwei.mymobliesafe.utils.SPUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;

public class SplashActivity extends AppCompatActivity {


    private TextView mTv_splash_version;
    private int mLocalVersionCode;
    private String mDownloadurl;
    private String mDesc;
    private Context content = SplashActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT)!= 0){
            finish();
            return;
        }
        setContentView(R.layout.activity_splash);
        bindViews();
        mTv_splash_version.setText("版本号:"+ PackageUtils.getVersionName(this));
        mLocalVersionCode = PackageUtils.getVersionCode(this);
        checkVersion();
        initData();

       /* if(!SPUtil.getBoolean(this,Constants.HAS_SHORTCUT,false)){
            initShortCut();
        }*/


    }

    private void initData() {
        initVirusDB("antivirus.db");
    }

    private void initVirusDB(String dbName) {
        File files = getFilesDir();
        File file = new File(files,dbName);
        if(file.exists()){
            return;
        }
        InputStream is = null;
        FileOutputStream fos = null;
        try {
            is = getAssets().open(dbName);
            fos = new FileOutputStream(file);
            byte[] bs = new byte[1024];
            int temp = -1;
            while ((temp = is.read(bs))!= -1){
                fos.write(bs,0,temp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(is!= null&& fos!= null){
                try {
                    is.close();
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void initShortCut() {
        Intent intent = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
        intent.putExtra(Intent.EXTRA_SHORTCUT_ICON,
                BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher));
        intent.putExtra(Intent.EXTRA_SHORTCUT_NAME,"SSS");

        Intent shortCutIntent = new Intent("android.intent.action.HOME");
        shortCutIntent.addCategory("android.intent.category.DEFAULT");
        intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT,shortCutIntent);
        sendBroadcast(intent);
        SPUtil.putBoolean(this,Constants.HAS_SHORTCUT,true);
    }

    private void checkVersion() {

        String url = Constants.BASE_URL;
        OkHttpUtils.get()
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        loadMainUI();
                    }
                    @Override
                    public void onResponse(String response, int id) {

                        Log.d("TAG","----success------");
                        processData(response);
                        loadMainUI();
                    }
                });

    }

    /**
     * 解析Json
     * @param response
     */
    private void processData(String response) {
        try {
            JSONObject obj = new JSONObject(response);
            mDownloadurl = obj.getString("downloadurl");
            int netVersion = obj.getInt("version");
            mDesc = obj.getString("desc");

            Log.d("TAG",netVersion+"--"+mDesc+"---"+mDownloadurl);
            if(netVersion == mLocalVersionCode){
                Log.d("TAG","netVersion == mLocalVersionCode");
            }else {
                Log.d("TAG","netVersion != mLocalVersionCode");
                showDialog();
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("TAG","JSONException:"+e);
            loadMainUI();
        }
    }

    private void showDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("message");
        builder.setMessage(mDesc);
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                loadMainUI();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                loadMainUI();
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("升级", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                downLoadApk(mDownloadurl);
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void downLoadApk(String mDownloadurl) {
        Log.d("TAG","download....");


        loadMainUI();
    }

    /**
     * 进入主界面
     */
    private void loadMainUI() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(content,MainActivity.class);
                startActivity(intent);
                finish();
            }
        },2000);
    }

    private void bindViews() {
        mTv_splash_version = (TextView) findViewById(R.id.tv_splash_version);
    }

}
