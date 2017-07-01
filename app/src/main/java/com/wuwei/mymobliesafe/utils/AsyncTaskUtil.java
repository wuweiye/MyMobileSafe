package com.wuwei.mymobliesafe.utils;

import android.os.AsyncTask;

import com.wuwei.mymobliesafe.call.AsyncCallBack;

/**
 * Created by 无为 on 2017/6/1.
 */

public class AsyncTaskUtil  {

    public static void doAsync(final AsyncCallBack callBack){
        if(callBack == null){
            return;
        }
        new AsyncTask<Void,Void,Void>(){


            @Override
            protected Void doInBackground(Void... params) {
                callBack.doInBackground();
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                callBack.onPostExecute();
            }

            @Override
            protected void onPreExecute() {
                callBack.onPreExecute();
            }
        }.execute();
    }




}
