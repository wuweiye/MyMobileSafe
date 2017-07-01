package com.wuwei.mymobliesafe.act.base;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by 无为 on 2017/5/10.
 */

public abstract class BaseSetupActivity extends Activity {

    private GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gestureDetector = new GestureDetector(this,new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

                if (e1.getX() - e2.getX() > 0){
                    //下一页
                    showNextPage();
                }else{
                    //上一页
                    showPrePage();
                }

                return super.onFling(e1, e2, velocityX, velocityY);
            }
        });

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(gestureDetector!=null)
        gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    public abstract void showNextPage();
    public abstract void showPrePage();

    public void nextPage(View view){
        showNextPage();
    }

    public void prePage(View v){
       showPrePage();
    }
}
