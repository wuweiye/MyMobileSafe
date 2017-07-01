package com.wuwei.mymobliesafe.act;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.wuwei.mymobliesafe.R;
import com.wuwei.mymobliesafe.app.Constants;
import com.wuwei.mymobliesafe.bean.MyServiceInfo;
import com.wuwei.mymobliesafe.dao.MyServiceDao;
import com.wuwei.mymobliesafe.utils.SPUtil;
import com.wuwei.mymobliesafe.utils.ServiceUtil;
import com.wuwei.mymobliesafe.utils.ToastUtil;
import com.wuwei.mymobliesafe.view.LoadingView;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ServiceClearAction extends AppCompatActivity {

    private TextView tv_service_count;
    private ListView lv_service;
    private LoadingView lv_londing;

    private PopupWindow popup;
    private MyServiceInfo myServiceInfo;
    private List<MyServiceInfo> myServiceInfos;
    private List<MyServiceInfo> mSystemServiceList;
    private List<MyServiceInfo> mCurtomerServiceList;

    private MyServiceDao myServiceDao;
    private List<MyServiceInfo> mLockSystemServiceList;
    private List<MyServiceInfo> mLockCurtomerServiceList;
    private long curDate;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            lv_londing.loadSuccess();
            lv_londing.setVisibility(View.GONE);
            lv_service.setVisibility(View.VISIBLE);

            MyAdapter myAdapter = new MyAdapter();
            lv_service.setAdapter(myAdapter);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sdcache_clear_action);
        initUI();
        initLoading();
        initData();
    }

    private void initData() {

        int count = ServiceUtil.getServiceCount(getApplicationContext());
        tv_service_count.setText("正在运行服务:"+count);

        curDate = System.currentTimeMillis();
        getData();



    }

    private void getData() {
        new Thread(){
            @Override
            public void run() {
                myServiceInfos = new ArrayList<MyServiceInfo>();
                mSystemServiceList = new ArrayList<MyServiceInfo>();
                mCurtomerServiceList = new ArrayList<MyServiceInfo>();

                myServiceInfos = ServiceUtil.getService(getApplicationContext());

                //myServiceDao = MyServiceDao.getInstance(getApplicationContext());
               // List<String> LockServiceList = myServiceDao.findAll();
                Log.d("TAG",myServiceInfos.size()+"---1---");
                for(MyServiceInfo info :myServiceInfos){


                    if(info.isSystem){
                        mSystemServiceList.add(info);
                    }else {
                        mCurtomerServiceList.add(info);
                    }
                }
                Log.d("TAG",mSystemServiceList.size()+"---System---");
                Log.d("TAG",mCurtomerServiceList.size()+"---Curtomer---");
                long endTime = System.currentTimeMillis();
                long time = curDate - endTime;
                if(time <2000){
                    long sleep = 2000 -time;
                    try {
                        sleep(sleep);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                handler.sendEmptyMessage(0);
            }
        }.start();
    }

    private void initUI() {
        tv_service_count = (TextView) findViewById(R.id.tv_service_count);
        lv_service = (ListView) findViewById(R.id.lv_service);

        lv_service.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(position == 0 || position == mCurtomerServiceList.size()+1){
                    return;
                }else {
                    if(position < mCurtomerServiceList.size()+1){
                        myServiceInfo = mCurtomerServiceList.get(position -1);
                    }else {
                        myServiceInfo = mSystemServiceList.get(position - mCurtomerServiceList.size() -2);
                    }

                    if(myServiceInfo != null){
                       /* if(!myServiceInfo.packName.equals(getPackageName())){
                            myServiceInfo.isCheck = !myServiceInfo.isCheck;
                            CheckBox cb_box = (CheckBox) view.findViewById(R.id.cb_box);
                            cb_box.setChecked(myServiceInfo.isCheck);
                        }*/

                        showPupupWindow(view);



                    }

                }
            }
        });


        lv_service.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

                if(popup!=null){
                    popup.dismiss();
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }

    private void showPupupWindow(View view) {
        View v = View.inflate(getApplicationContext(),R.layout.popupwindow_layout,null);
        TextView tv_unistall = (TextView) v.findViewById(R.id.tv_unistall);
        TextView tv_start = (TextView) v.findViewById(R.id.tv_start);
        TextView tv_share = (TextView) v.findViewById(R.id.tv_share);

        tv_start.setText("永久关闭");
        tv_unistall.setText("关闭此服务");
        tv_start.setText("预留位");

        tv_unistall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(myServiceInfo.isSystem){
                    ToastUtil.show(getApplicationContext(),"系统服务不建议关闭");


                }else {

                    //int i = execRootCmdSilent("echo test");

                   kill(myServiceInfo.getPackName());

                }
                popup.dismiss();

                //ToastUtil.show(getApplicationContext(),"已关闭");

            }
        });

        tv_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*PackageManager pm = getPackageManager();
                Intent launchIntentForPackage = pm.getLaunchIntentForPackage(mAppInfo.getPackageName());
                if(launchIntentForPackage != null){
                    startActivity(launchIntentForPackage);
                }else {
                    ToastUtil.show(getApplicationContext(),"此应用不能开启");
                }*/

                popup.dismiss();
            }
        });
        /*tv_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });*/

        AlphaAnimation alpha = new AlphaAnimation(0,1);
        alpha.setDuration(1000);
        alpha.setFillAfter(true);

        ScaleAnimation scale = new ScaleAnimation(
                0,1,1,0,
                Animation.RELATIVE_TO_SELF,0.5F,
                Animation.RELATIVE_TO_SELF,0.5F
        );
        scale.setDuration(1000);
        scale.setFillAfter(true);

        AnimationSet set = new AnimationSet(true);
        set.addAnimation(alpha);
        set.addAnimation(scale);
        //创建窗体
        popup = new PopupWindow(v, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        //透明背景
        popup.setBackgroundDrawable(new ColorDrawable());
        //指定位置
        popup.showAsDropDown(view,30,-view.getHeight());

        v.startAnimation(set);
    }

    private void kill(String pkgName) {
        Process sh = null;
        DataOutputStream os = null;
        try {
            sh = Runtime.getRuntime().exec("su");
            os = new DataOutputStream(sh.getOutputStream());
            final String Command = "am force-stop "+pkgName+ "\n";
            os.writeBytes(Command);
            os.flush();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

            ToastUtil.show(getApplicationContext(),"无root权限");
            return;
        }

        ToastUtil.show(getApplicationContext(),"已关闭");
        /*try {
            sh.waitFor();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }*/
    }

    private void initLoading() {

        lv_londing = (LoadingView) findViewById(R.id.lv_loading);
        lv_londing.load();
        lv_service.setVisibility(View.GONE);
    }

    class MyAdapter extends BaseAdapter {

        @Override
        public int getViewTypeCount() {
            return super.getViewTypeCount()+1;
        }

        @Override
        public int getItemViewType(int position) {
            if (position == 0 || position == mCurtomerServiceList.size()+1) {
                return  0;
            } else{
                return 1;
            }

        }

        @Override
        public int getCount() {
            return mCurtomerServiceList.size()+mSystemServiceList.size()+2;
        }

        @Override
        public MyServiceInfo getItem(int position) {

            if(position == 0 || position ==mCurtomerServiceList.size()+1){
                return  null;
            }else {
                if(position < mCurtomerServiceList.size()+1){
                    return mCurtomerServiceList.get(position - 1);
                }else {

                    return mSystemServiceList.get(position - mCurtomerServiceList.size()-2);

                }

            }

        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            int type = getItemViewType(position);



            if(type == 0){

                ViewTitleHolder holder = null;
                if (convertView == null){
                    convertView = View.inflate(getApplicationContext(),R.layout.listview_app_item_title,null);
                    holder = new ViewTitleHolder();
                    holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
                    convertView.setTag(holder);
                }else {
                    holder = (ViewTitleHolder) convertView.getTag();
                }
                if(position == 0){

                    holder.tv_title.setText("用户应用("+mCurtomerServiceList.size()+") ");
                }else {
                    holder.tv_title.setText("系统应用("+mSystemServiceList.size()+") ");
                }

                return convertView;
            }else {
               ViewHolder viewHolder = null;
                if(convertView == null){
                    convertView = View.inflate(getApplicationContext(),R.layout.listview_process_item,null);
                    viewHolder = new ViewHolder();
                    viewHolder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
                    viewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
                    viewHolder.tv_memory_info = (TextView) convertView.findViewById(R.id.tv_memory_info);
                    viewHolder.cb_box = (CheckBox) convertView.findViewById(R.id.cb_box);
                    convertView.setTag(viewHolder);

                }else {
                    viewHolder = (ViewHolder) convertView.getTag();
                }

                viewHolder.iv_icon.setBackgroundDrawable(getItem(position).icon);
                viewHolder.tv_name.setText(getItem(position).name);
                //String strSize = Formatter.formatFileSize(getApplicationContext(),getItem(position).memSize);
                viewHolder.tv_memory_info.setText(getItem(position).packName);

                viewHolder.cb_box.setVisibility(View.GONE);
                /*if(getItem(position).packName.equals(getPackageName())){
                    viewHolder.cb_box.setVisibility(View.GONE);
                }else {
                    viewHolder.cb_box.setVisibility(View.VISIBLE);
                }*/
                viewHolder.cb_box.setChecked(getItem(position).isCheck);

                return convertView;
            }


        }

        class ViewHolder{
            ImageView iv_icon;
            TextView tv_name;
            TextView tv_memory_info;
            CheckBox cb_box;
        }
        class ViewTitleHolder{
            TextView tv_title;
        }
    }
}
