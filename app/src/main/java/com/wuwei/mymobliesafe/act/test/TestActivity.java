package com.wuwei.mymobliesafe.act.test;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.wuwei.mymobliesafe.R;
import com.wuwei.mymobliesafe.call.AsyncCallBack;
import com.wuwei.mymobliesafe.utils.AsyncTaskUtil;
import com.wuwei.mymobliesafe.view.LoadingView;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test2);

        final LoadingView lv_londing = (LoadingView) findViewById(R.id.lv_loading);
        lv_londing.load();


        AsyncTaskUtil.doAsync(new AsyncCallBack() {
            @Override
            public void doInBackground() {
                try {
                    Thread.sleep(3000);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onPostExecute() {

                lv_londing.loadSuccess();
            }
        });

    }

}
