package com.wuwei.mymobliesafe.act;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.wuwei.mymobliesafe.R;
import com.wuwei.mymobliesafe.bean.AppInfo;
import com.wuwei.mymobliesafe.dao.AppLockDao;
import com.wuwei.mymobliesafe.utils.AppInfoUtil;

import java.util.ArrayList;
import java.util.List;

public class AppLockAction extends AppCompatActivity {

    private Button bt_unlock;
    private Button bt_lock;
    private LinearLayout ll_unlock;
    private LinearLayout ll_lock;
    private TextView tv_unlock;
    private TextView tv_lock;
    private ListView lv_unlock;
    private ListView lv_lock;
    private AppLockDao mDao;
    private List<AppInfo> mAppInfoList;
    private List<AppInfo> mLockList;
    private List<AppInfo> mUnLockList;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            mLockAdapter = new MyAdapter(true);
            lv_lock.setAdapter(mLockAdapter);

            mUnLockAdapter = new MyAdapter(false);
            lv_unlock.setAdapter(mUnLockAdapter);

        }
    };
    private MyAdapter mLockAdapter,mUnLockAdapter;
    private TranslateAnimation mTranslateAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_lock_action);
        initUI();
        initData();
        initAnimation();

    }

    private void initAnimation() {
        mTranslateAnimation = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF,0,
                Animation.RELATIVE_TO_SELF,1,
                Animation.RELATIVE_TO_SELF,0,
                Animation.RELATIVE_TO_SELF,0
        );
        mTranslateAnimation.setDuration(1000);
    }

    private void initData() {
        new Thread(){
            @Override
            public void run() {

                mAppInfoList = AppInfoUtil.getAppInfoList(getApplicationContext());
                mLockList = new ArrayList<AppInfo>();
                mUnLockList = new ArrayList<AppInfo>();
                mDao = AppLockDao.getInstance(getApplicationContext());
                List<String> lockPackageList = mDao.findAll();
                for(AppInfo appInfo : mAppInfoList){
                    if(lockPackageList.contains(appInfo.packageName)){
                        mLockList.add(appInfo);
                    }else {
                        mUnLockList.add(appInfo);
                    }
                }

                handler.sendEmptyMessage(0);
            }
        }.start();
    }

    private void initUI() {
        bt_unlock = (Button) findViewById(R.id.bt_unlock);
        bt_lock = (Button) findViewById(R.id.bt_lock);

        ll_unlock = (LinearLayout) findViewById(R.id.ll_unlock);
        ll_lock = (LinearLayout) findViewById(R.id.ll_lock);

        tv_unlock = (TextView) findViewById(R.id.tv_unlock);
        tv_lock = (TextView) findViewById(R.id.tv_lock);

        lv_unlock = (ListView) findViewById(R.id.lv_unlock);
        lv_lock = (ListView) findViewById(R.id.lv_lock);

        bt_lock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_lock.setVisibility(View.VISIBLE);
                ll_unlock.setVisibility(View.GONE);
                bt_unlock.setBackgroundResource(R.color.colorLockUnChick);
                bt_lock.setBackgroundResource(R.color.colorLockChick);
            }
        });
        bt_unlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ll_lock.setVisibility(View.GONE);
                ll_unlock.setVisibility(View.VISIBLE);
                bt_unlock.setBackgroundResource(R.color.colorLockChick);
                bt_lock.setBackgroundResource(R.color.colorLockUnChick);
            }
        });
    }

    class MyAdapter extends BaseAdapter{

        private boolean isLock;
        public MyAdapter (boolean isLock){
            this.isLock = isLock;
        }
        @Override
        public int getCount() {
            if(isLock){
                tv_lock.setText("已加锁"+mLockList.size());
                return mLockList.size();
            }else {
                tv_unlock.setText("未加锁"+mUnLockList.size());
                return mUnLockList.size();
            }
        }

        @Override
        public AppInfo getItem(int position) {
           if(isLock){
               return mLockList.get(position);
           }else {
               return mUnLockList.get(position);
           }
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Viewholder viewholder = null;
            if(convertView == null){
                convertView = View.inflate(getApplicationContext(),R.layout.listview_islock,null);
                viewholder = new Viewholder();
                viewholder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
                viewholder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
                viewholder.iv_lock = (ImageView) convertView.findViewById(R.id.iv_lock);
                convertView.setTag(viewholder);
            }else {
                viewholder = (Viewholder) convertView.getTag();
            }

            final AppInfo appInfo = getItem(position);
            viewholder.iv_icon.setBackgroundDrawable(appInfo.icon);
            viewholder.tv_name.setText(appInfo.name);
            if(isLock){
                viewholder.iv_lock.setImageResource(R.drawable.lock);
            }else {
                viewholder.iv_lock.setImageResource(R.drawable.unlock);
            }


            final View finalConvertView = convertView;
            viewholder.iv_lock.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finalConvertView.startAnimation(mTranslateAnimation);
                    mTranslateAnimation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {

                            if(isLock){

                                Log.d("TAG","remove:"+appInfo.name+"-----");
                                mUnLockList.add(appInfo);
                                mLockList.remove(appInfo);

                                mDao.delete(appInfo.packageName);

                                mLockAdapter.notifyDataSetChanged();
                            }else{

                                mLockList.add(appInfo);
                                mUnLockList.remove(appInfo);
                                mDao.insert(appInfo.packageName);
                                mUnLockAdapter.notifyDataSetChanged();

                            }

                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });

                }
            });
            return convertView;
        }

        class Viewholder{
            ImageView iv_icon;
            TextView tv_name;
            ImageView iv_lock;
        }
    }
}
