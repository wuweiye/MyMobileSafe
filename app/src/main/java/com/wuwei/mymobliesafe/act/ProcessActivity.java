package com.wuwei.mymobliesafe.act;

import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.wuwei.mymobliesafe.R;
import com.wuwei.mymobliesafe.app.Constants;
import com.wuwei.mymobliesafe.bean.ProcessInfo;
import com.wuwei.mymobliesafe.utils.ProcessInfoUtil;
import com.wuwei.mymobliesafe.utils.SPUtil;
import com.wuwei.mymobliesafe.utils.ToastUtil;
import com.wuwei.mymobliesafe.view.LoadingView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProcessActivity extends AppCompatActivity {

    private TextView tv_process_count;
    private TextView tv_process_info;
    private TextView tv_des;
    private ListView lv_process_item;
    private Button bt_all;
    private Button bt_reverse;
    private Button bt_clear;
    private Button bt_setting;
    private int mProcessCount;
    private List<ProcessInfo> mProcessInfos;
    private List<ProcessInfo> mSystemList;
    private List<ProcessInfo> mCustomerList;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {

            lv_londing.loadSuccess();
            lv_londing.setVisibility(View.GONE);
            fl_app.setVisibility(View.VISIBLE);
            mAdapter = new MyAdapter();
            lv_process_item.setAdapter(mAdapter);
            if (tv_des!=null && mCustomerList!=null){
                tv_des.setText("用户应用("+mCustomerList.size()+")");
            }
        }
    };
    private ProcessInfo mProcessInfo;
    private MyAdapter mAdapter;
    private long mAvailSpace;
    private String mStrAvailSpace;
    private String mStrTotalSpace;
    private LoadingView lv_londing;
    private FrameLayout fl_app;
    private Date curDate;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process);
        initUI();
        initListener();
        initTitleData();
        initLoading();
        initListData();
    }

    private void initLoading() {
        fl_app = (FrameLayout) findViewById(R.id.fl_app_process);
        lv_londing = (LoadingView) findViewById(R.id.lv_loading);
        curDate = new Date(System.currentTimeMillis());
        lv_londing.load();
        fl_app.setVisibility(View.GONE);
    }

    private void initListener() {

        bt_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectAll();
            }
        });
        bt_reverse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectReverse();
            }
        });
        bt_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearAll();
            }
        });
        bt_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Setting();
            }
        });
        lv_process_item.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

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

        lv_process_item.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0 || position == mCustomerList.size()+1){
                    return;
                }else {
                    if(position < mCustomerList.size()+1){
                        mProcessInfo = mCustomerList.get(position -1);
                    }else {
                        mProcessInfo = mSystemList.get(position - mCustomerList.size() -2);
                    }

                    if(mProcessInfo != null){
                        if(!mProcessInfo.packName.equals(getPackageName())){
                            mProcessInfo.isCheck = !mProcessInfo.isCheck;
                            CheckBox cb_box = (CheckBox) view.findViewById(R.id.cb_box);
                            cb_box.setChecked(mProcessInfo.isCheck);
                        }
                    }

                }
            }
        });
    }

    private void Setting() {
        Intent intent =new Intent(this,ProcessSettingActivity.class);
        startActivityForResult(intent,0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(mAdapter!= null){
            mAdapter.notifyDataSetChanged();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void clearAll() {

        List<ProcessInfo> killProcessList = new ArrayList<>();
        for (ProcessInfo p : mCustomerList){
            if(p.getPackName().equals(getPackageName())){
                continue;
            }
            if(p.isCheck){
                //mCustomerList.remove(p);
                killProcessList.add(p);
            }
        }
        for (ProcessInfo p : mSystemList){
           if (p.isCheck){
               killProcessList.add(p);
           }
        }

        if(killProcessList!= null){
            long totalReleaseSpace = 0;
            for(ProcessInfo processInfo :killProcessList){
                if(mCustomerList.contains(processInfo)){
                    mCustomerList.remove(processInfo);
                }else if(mSystemList.contains(processInfo)){

                    mSystemList.remove(processInfo);
                }
                ProcessInfoUtil.killprocess(this,processInfo);

                totalReleaseSpace += processInfo.memSize;
            }

            if(mAdapter!= null){
                mAdapter.notifyDataSetChanged();
            }

            mProcessCount -=killProcessList.size();
            mAvailSpace += totalReleaseSpace;
            tv_process_count.setText("进程总数:"+ mProcessCount);
            mStrAvailSpace = Formatter.formatFileSize(this,mAvailSpace);
            tv_process_info.setText("剩余/总共"+mStrAvailSpace+"/"+mStrTotalSpace);

            ToastUtil.show(getApplicationContext(),"结束了"+killProcessList.size()+"个进程，释放了"
                    +Formatter.formatFileSize(this,totalReleaseSpace)+"空间");

        }else {
            ToastUtil.show(this,"未选中任何进程");
        }

    }

    private void selectReverse() {
        for (ProcessInfo p : mCustomerList){
            if(p.getPackName().equals(getPackageName())){
                continue;
            }
            p.isCheck = !p.isCheck;
        }
        for (ProcessInfo p : mSystemList){
            p.isCheck = !p.isCheck;
        }
        if(mAdapter != null){
            mAdapter.notifyDataSetChanged();
        }

    }

    private void selectAll() {
        for (ProcessInfo p : mCustomerList){
            if(p.getPackName().equals(getPackageName())){
                continue;
            }
            p.isCheck = true;
        }
        for (ProcessInfo p : mSystemList){
            p.isCheck = true;
        }
        if(mAdapter != null){
            mAdapter.notifyDataSetChanged();
        }

    }


    private void initListData() {
        getData();
    }

    protected void getData(){
        new Thread(){

            @Override
            public void run() {

                mProcessInfos = ProcessInfoUtil.getProcessInfo(getApplicationContext());
                mSystemList = new ArrayList<ProcessInfo>();
                mCustomerList = new ArrayList<ProcessInfo>();

                for(ProcessInfo processInfo :mProcessInfos){
                    if(processInfo.isSystem){
                        mSystemList.add(processInfo);
                    }else {
                        mCustomerList.add(processInfo);
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
    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void initTitleData() {

        mProcessCount = ProcessInfoUtil.getProcessCount(this);
        tv_process_count.setText("进程总数:"+ mProcessCount);

        mAvailSpace = ProcessInfoUtil.getAvailSpace(this);
        mStrAvailSpace = Formatter.formatFileSize(this,mAvailSpace);

        long totalSpace = ProcessInfoUtil.getTotalSpace(this);
        mStrTotalSpace = Formatter.formatFileSize(this,totalSpace);

        tv_process_info.setText("剩余/总共"+ mStrAvailSpace +"/"+ mStrTotalSpace);
    }

    private void initUI() {

        tv_des = (TextView) findViewById(R.id.tv_des);
        tv_process_count = (TextView) findViewById(R.id.tv_process_count);
        tv_process_info = (TextView) findViewById(R.id.tv_process_info);
        lv_process_item = (ListView) findViewById(R.id.lv_process_item);

        bt_all = (Button) findViewById(R.id.bt_all);
        bt_reverse = (Button) findViewById(R.id.bt_reverse);
        bt_clear = (Button) findViewById(R.id.bt_clear);
        bt_setting = (Button) findViewById(R.id.bt_setting);

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
            if(SPUtil.getBoolean(getApplicationContext(), Constants.SHOW_SYSTEM,false)){
                return mCustomerList.size()+mSystemList.size()+2;
            }else{
                return mCustomerList.size()+1;
            }

        }

        @Override
        public ProcessInfo getItem(int position) {




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

                    holder.tv_title.setText("用户应用("+mCustomerList.size()+") ");
                }else {
                    holder.tv_title.setText("系统应用("+mSystemList.size()+") ");
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
                String strSize = Formatter.formatFileSize(getApplicationContext(),getItem(position).memSize);
                viewHolder.tv_memory_info.setText("占用内存:"+ strSize);

                if(getItem(position).packName.equals(getPackageName())){
                    viewHolder.cb_box.setVisibility(View.GONE);
                }else {
                    viewHolder.cb_box.setVisibility(View.VISIBLE);
                }
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
