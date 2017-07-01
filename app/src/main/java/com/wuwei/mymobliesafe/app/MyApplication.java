package com.wuwei.mymobliesafe.app;

import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.zhy.http.okhttp.OkHttpUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * Created by 无为 on 2017/3/15.
 */

public class MyApplication extends Application {
    private static Context getContent(){
        return mContent;
    }
    private static Context mContent;
    @Override
    public void onCreate() {
        super.onCreate();
        mContent = this;
        initOKhttpClient();

        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                e.printStackTrace();
                Log.i("TAG","捕获一个异常");

                String path = Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator+"error.log";
                File file = new File(path);
                try {
                    PrintWriter printWriter = new PrintWriter(file);
                    e.printStackTrace(printWriter);
                    printWriter.close();
                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                }

                //上传
                System.exit(0);
            }
        });
    }
    private  void initOKhttpClient() {
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(5000L, TimeUnit.MILLISECONDS)
                .readTimeout(5000L,TimeUnit.MILLISECONDS)
                .build();
        OkHttpUtils.initClient(okHttpClient);
    }
}