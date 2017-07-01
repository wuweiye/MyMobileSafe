package com.wuwei.mymobliesafe.call;

/**
 * Created by 无为 on 2017/6/1.
 */

public abstract class AsyncCallBack {

    public void onPreExecute(){}

    public abstract void doInBackground();
    public abstract void onPostExecute();
}
