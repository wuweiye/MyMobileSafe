package com.wuwei.mymobliesafe.act.base;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

import com.wuwei.mymobliesafe.R;
import com.wuwei.mymobliesafe.act.CacheClearAction;
import com.wuwei.mymobliesafe.act.ServiceClearAction;

public class BaseClearCacheActivity extends TabActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_cleear_cache);


        TabHost.TabSpec tab1 = getTabHost().newTabSpec("clear_cache").setIndicator("缓存清理");
        TabHost.TabSpec tab2 = getTabHost().newTabSpec("sd_clear_cache").setIndicator("服务清理");


        tab1.setContent(new Intent(this, CacheClearAction.class));
        tab2.setContent(new Intent(this, ServiceClearAction.class));

        getTabHost().addTab(tab1);
        getTabHost().addTab(tab2);
    }
}
