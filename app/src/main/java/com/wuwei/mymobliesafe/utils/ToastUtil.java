package com.wuwei.mymobliesafe.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by 无为 on 2017/5/9.
 */

public class ToastUtil {

    public  static final  void show(Context context,String value){
        Toast.makeText(context,value,Toast.LENGTH_SHORT).show();
    }
}
