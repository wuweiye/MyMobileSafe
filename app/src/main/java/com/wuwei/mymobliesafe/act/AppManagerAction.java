package com.wuwei.mymobliesafe.act;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StatFs;
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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.wuwei.mymobliesafe.R;
import com.wuwei.mymobliesafe.bean.AppInfo;
import com.wuwei.mymobliesafe.utils.AppInfoUtil;
import com.wuwei.mymobliesafe.utils.ToastUtil;
import com.wuwei.mymobliesafe.view.LoadingView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AppManagerAction extends AppCompatActivity {

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {


            lv_londing.loadSuccess();
            fl_app.setVisibility(View.VISIBLE);
            adapter = new MyAdapter();
            lv_app_list.setAdapter(adapter);
            if (tv_des!=null && mCustomerList!=null){
                tv_des.setText("用户应用("+mCustomerList.size()+")");
            }


        }
    };
    private List<AppInfo> appInfos;
    private List<AppInfo> mSystemList;
    private List<AppInfo> mCustomerList;
    private ListView lv_app_list;
    private static int i = 0;
    private TextView tv_des;
    private AppInfo mAppInfo;
    private PopupWindow popup;
    private MyAdapter adapter;
    private LoadingView lv_londing;
    private FrameLayout fl_app;
    private Date curDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_manager);

        
        initTitle();
        initLoading();
        initList();
    }

    private void initLoading() {
        fl_app = (FrameLayout) findViewById(R.id.fl_app_manager);
        lv_londing = (LoadingView) findViewById(R.id.lv_loading);
        curDate = new Date(System.currentTimeMillis());
        lv_londing.load();
        fl_app.setVisibility(View.GONE);
    }

    private void initList() {
        lv_app_list = (ListView) findViewById(R.id.lv_app_list);
        tv_des = (TextView) findViewById(R.id.tv_des);
        new Thread(){
            @Override
            public void run() {
                appInfos = AppInfoUtil.getAppInfoList(getApplicationContext());

                mCustomerList = new ArrayList<AppInfo>();
                mSystemList = new ArrayList<AppInfo>();
                for(AppInfo app:appInfos){
                    if(app.isSystem){
                        mSystemList.add(app);
                    }else {
                        mCustomerList.add(app);
                    }
                }

                Date endDate = new Date(System.currentTimeMillis());
                long diff = endDate.getTime() - curDate.getTime();

                if(diff < 2000){
                    try {
                        long sleep = 2000 - diff;
                        sleep(sleep);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }


                handler.sendEmptyMessage(0);
            }
        }.start();

        lv_app_list.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                if(popup!= null){
                    popup.dismiss();
                }
                if(mCustomerList!= null && mSystemList!= null)
                {
                    if(firstVisibleItem >= mCustomerList.size()+1){
                        tv_des.setText("系统应用("+mSystemList.size()+")");
                    }else {
                        tv_des.setText("用户应用("+mCustomerList.size()+")");
                    }
                }

            }
        });

        lv_app_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0 || position == mCustomerList.size()+1){
                    return;
                }else {
                    if(position < mCustomerList.size()+1){
                        mAppInfo = mCustomerList.get(position -1);
                    }else {
                        mAppInfo = mSystemList.get(position - mCustomerList.size() -2);
                    }

                    if(popup!= null){
                        popup.dismiss();
                    }
                    showPupupWindow(view);
                }
            }
        });
    }

    private void showPupupWindow(View view) {


        View v = View.inflate(getApplicationContext(),R.layout.popupwindow_layout,null);
        TextView tv_unistall = (TextView) v.findViewById(R.id.tv_unistall);
        TextView tv_start = (TextView) v.findViewById(R.id.tv_start);
        TextView tv_share = (TextView) v.findViewById(R.id.tv_share);

        tv_unistall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mAppInfo.isSystem){
                    ToastUtil.show(getApplicationContext(),"系统应用");
                }else {
                    Intent intent = new Intent("android.intent.action.DELETE");
                    intent.addCategory("android.intent.category.DEFAULT");
                    intent.setData(Uri.parse("package:"+mAppInfo.getPackageName()));
                    startActivity(intent);
                    mCustomerList.remove(mAppInfo);

                    adapter.notifyDataSetChanged();


                }
                popup.dismiss();

            }
        });

        tv_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PackageManager pm = getPackageManager();
                Intent launchIntentForPackage = pm.getLaunchIntentForPackage(mAppInfo.getPackageName());
                if(launchIntentForPackage != null){
                    startActivity(launchIntentForPackage);
                }else {
                    ToastUtil.show(getApplicationContext(),"此应用不能开启");
                }

                popup.dismiss();
            }
        });
        tv_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ToastUtil.show(getApplicationContext(),"share");


            }
        });

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

    private void initTitle() {
        TextView tv_memory = (TextView) findViewById(R.id.tv_memory);
        TextView tv_sd_memory = (TextView) findViewById(R.id.tv_sd_memory);
        String path = Environment.getDataDirectory().getAbsolutePath();
        String sdPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        String memoryAvailSpace = Formatter.formatFileSize(this,getAvailSpace(path));
        String sdMemoryAvailSpace = Formatter.formatFileSize(this,getAvailSpace(sdPath));

        tv_memory.setText("磁盘可用:"+memoryAvailSpace);
        tv_sd_memory.setText("SD可用:"+sdMemoryAvailSpace);




    }

    private long getAvailSpace(String path) {
        StatFs ststFs = new StatFs(path);
        int count = ststFs.getBlockCount();
        int size = ststFs.getBlockSize();

        return count*size;
    }

    class MyAdapter extends BaseAdapter{

        @Override
        public int getViewTypeCount() {
            return super.getViewTypeCount()+1;
        }

        @Override
        public int getItemViewType(int position) {
            if (position == 0 || position == mCustomerList.size()+1) {
                return  0;
            } else{
                return 1;
            }

        }

        @Override
        public int getCount() {
            return mCustomerList.size()+mSystemList.size()+2;
        }

        @Override
        public AppInfo getItem(int position) {




            if(position == 0 || position ==mCustomerList.size()+1){
                return  null;
            }else {
                if(position < mCustomerList.size()+1){
                    return mCustomerList.get(position - 1);
                }else {

                    return mSystemList.get(position - mCustomerList.size()-2);

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
                    i = 0;
                    holder.tv_title.setText("用户应用("+mCustomerList.size()+") ");
                }else {
                    holder.tv_title.setText("系统应用("+mSystemList.size()+") ");
                }

                return convertView;
            }else {
                ViewHolder viewHolder = null;
                if(convertView == null){
                    convertView = View.inflate(getApplicationContext(),R.layout.listview_app_item,null);
                    viewHolder = new ViewHolder();
                    viewHolder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
                    viewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
                    viewHolder.tv_path = (TextView) convertView.findViewById(R.id.tv_path);
                    convertView.setTag(viewHolder);

                }else {
                    viewHolder = (ViewHolder) convertView.getTag();
                }

                viewHolder.iv_icon.setBackgroundDrawable(getItem(position).icon);
                viewHolder.tv_name.setText(getItem(position).name);
                if(getItem(position).isSdCard){
                    viewHolder.tv_path.setText("sd卡应用 ");
                }else {
                    viewHolder.tv_path.setText("手机应用");
                }

                return convertView;
            }


        }

        class ViewHolder{
            ImageView iv_icon;
            TextView tv_name;
            TextView tv_path;
        }
        class ViewTitleHolder{
            TextView tv_title;
        }
    }
}
