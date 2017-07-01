package com.wuwei.mymobliesafe.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

/**
 * Created by 无为 on 2017/3/15.
 */

public class FocusableTextView extends TextView {
    public FocusableTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Log.d("TAG","2个参数");
        setEllipsize(TextUtils.TruncateAt.MARQUEE);
        setSingleLine();
        setFocusable(true);
        setFocusableInTouchMode(true);
        setMarqueeRepeatLimit(-1);
    }

    @Override
    public boolean isFocused() {
        return true;
    }

    public FocusableTextView(Context context) {
        super(context);
    }
}
