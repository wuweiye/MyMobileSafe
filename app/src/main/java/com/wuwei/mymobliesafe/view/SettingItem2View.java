package com.wuwei.mymobliesafe.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wuwei.mymobliesafe.R;

/**
 * Created by 无为 on 2017/5/30.
 */

public class SettingItem2View extends RelativeLayout{


    private final static String FIRST = "first";
    private final static String MIDDLE = "middle";
    private final static String LAST = "last";
    private ImageView iv_toggle;
    private boolean isToggle = false;

    public SettingItem2View(Context context, AttributeSet attrs) {
        this(context, attrs,0);

    }

    public SettingItem2View(Context context) {
        this(context,null);
    }

    public SettingItem2View(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);

    }

    private void init(Context context, AttributeSet attrs) {
        //Log.d("TAG","-----init------");
        View view = View.inflate(context, R.layout.item_setting_2_view,this);
        TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
        iv_toggle = (ImageView) view.findViewById(R.id.iv_toggle);
        TypedArray ta = context.obtainStyledAttributes(attrs,R.styleable.com_wuwei_mymobliesafe_view_SettingItem2View);
        String title = ta.getString(R.styleable.com_wuwei_mymobliesafe_view_SettingItem2View_title);
        String background = ta.getString(R.styleable.com_wuwei_mymobliesafe_view_SettingItem2View_wbackground);

        ta.recycle();
        tv_title.setText(title);
        switch (background){
            case FIRST:
                //Log.d("TAG","one");
                view.setBackgroundResource(R.drawable.first_selected);
                break;
            case MIDDLE:
                //Log.d("TAG","two");
                view.setBackgroundResource(R.drawable.middle_selected);
                break;
            case LAST:
                //Log.d("TAG","onthree");
                view.setBackgroundResource(R.drawable.last_selected);
                break;
            default:
                //Log.d("TAG","-----");
                break;

        }


        setCheck(isToggle);

        //iv_toggle.setImageResource(R.drawable.off);
    }

    public boolean isTaggle(){
        return  isToggle;
    }
    public void setCheck(boolean check){
        isToggle = check;
        if(check){
            iv_toggle.setImageResource(R.drawable.open);
        }else {
            iv_toggle.setImageResource(R.drawable.off);
        }
    }
}
