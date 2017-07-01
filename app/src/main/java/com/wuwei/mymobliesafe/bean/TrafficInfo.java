package com.wuwei.mymobliesafe.bean;

import android.graphics.drawable.Drawable;

/**
 * Created by 无为 on 2017/5/31.
 */

public class TrafficInfo {

    public Drawable icon;

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public long getRcv() {
        return rcv;
    }

    public void setRcv(long rcv) {
        this.rcv = rcv;
    }

    public long getSnd() {
        return snd;
    }

    public void setSnd(long snd) {
        this.snd = snd;
    }

    public String appName;
    public long rcv;
    public long snd;


}
