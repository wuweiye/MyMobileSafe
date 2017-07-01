package com.wuwei.mymobliesafe.bean;

import android.graphics.drawable.Drawable;

/**
 * Created by 无为 on 2017/6/15.
 */

public class MyServiceInfo {
    public String name;
    public Drawable icon;
    public long memSize;
    public boolean isCheck;
    public boolean isSystem;
    public String packName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public long getMemSize() {
        return memSize;
    }

    public void setMemSize(long memSize) {
        this.memSize = memSize;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public boolean isSystem() {
        return isSystem;
    }

    public void setSystem(boolean system) {
        isSystem = system;
    }

    public String getPackName() {
        return packName;
    }

    public void setPackName(String packName) {
        this.packName = packName;
    }

    @Override
    public String toString() {
        return "MyServiceInfo{" +
                "name='" + name + '\'' +
                ", icon=" + icon +
                ", memSize=" + memSize +
                ", isCheck=" + isCheck +
                ", isSystem=" + isSystem +
                ", packName='" + packName + '\'' +
                '}';
    }
}
