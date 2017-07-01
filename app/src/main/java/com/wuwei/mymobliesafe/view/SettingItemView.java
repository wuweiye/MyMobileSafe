package com.wuwei.mymobliesafe.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wuwei.mymobliesafe.R;

/**
 * Created by 无为 on 2017/3/17.
 */

public class SettingItemView extends RelativeLayout {

    private static final String NAMESPACE = "http://schemas.android.com/apk/res/com.wuwei.mymobliesafe";
    private  CheckBox cb_setting;
    private TextView tv_setting_des;
    private TextView tv_setting_item;
    private String mDesTitle;
    private String mDesOff;
    private String mDesOn;
    public SettingItemView(Context context, AttributeSet attrs) {
        this(context, attrs,0);

    }

    public SettingItemView(Context context) {
        this(context,null);
    }

    public SettingItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);

    }

    private void init(Context context,AttributeSet attrs) {
        View view = View.inflate(context, R.layout.item_setting_view,this);
        tv_setting_item = (TextView) view.findViewById(R.id.tv_setting_item);
        tv_setting_des = (TextView) view.findViewById(R.id.tv_setting_des);

        cb_setting = (CheckBox) view.findViewById(R.id.cb_setting);
        mDesTitle = attrs.getAttributeValue(NAMESPACE,"destitle");
        mDesOff = attrs.getAttributeValue(NAMESPACE,"desoff");
        mDesOn = attrs.getAttributeValue(NAMESPACE,"deson");

        tv_setting_item.setTextColor(Color.BLACK);
        tv_setting_des.setTextColor(Color.BLACK);

        tv_setting_item.setText(mDesTitle);
        setCheck(isChecked());

    }
    public boolean isChecked(){
        return cb_setting.isChecked();
    }
    public void setCheck(boolean isCheck){
        cb_setting.setChecked(isCheck);
        if(isCheck){
            tv_setting_des.setText(mDesOn);
        }else{
            tv_setting_des.setText(mDesOff);
        }
    }

}
