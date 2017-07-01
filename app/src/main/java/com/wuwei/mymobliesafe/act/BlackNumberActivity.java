package com.wuwei.mymobliesafe.act;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.wuwei.mymobliesafe.R;
import com.wuwei.mymobliesafe.bean.BlackNumberInfo;
import com.wuwei.mymobliesafe.dao.BlackNumberDao;
import com.wuwei.mymobliesafe.utils.ToastUtil;

import java.util.List;

public class BlackNumberActivity extends Activity {

    private BlackNumberDao mBlackNumberDao;
    private List<BlackNumberInfo> mBlackNumberInfos;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {

            mIsLoad = false;
            if (myAdapter == null){
                myAdapter = new MyAdapter();
                lv_blacknumber.setAdapter(myAdapter);
            }else {
                myAdapter.notifyDataSetChanged();
            }

        }
    };
    private Button bt_add;
    private ListView lv_blacknumber;
    private int mode = 1;
    private MyAdapter myAdapter;
    private boolean mIsLoad = false;
    private int mCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_black_number);

        initUI();
        //insert();
        initDate();


    }

    private void insert() {
        new Thread(){

            @Override
            public void run() {
                mBlackNumberDao = BlackNumberDao.getInstance(getApplicationContext());

                for(int i= 10; i < 100; i++ ){
                    mBlackNumberDao.insert("1867069"+i,1+"");
                }
            }
        }.start();
    }

    private void initDate() {

        new Thread(){
            @Override
            public void run() {

                mBlackNumberDao = BlackNumberDao.getInstance(getApplicationContext());
                mBlackNumberInfos = mBlackNumberDao.find(0);
                mCount = mBlackNumberDao.getCount();
                //Log.d("TAG","mCount init:"+mCount);
                handler.sendEmptyMessage(0);
            }
        }.start();
    }

    private void initUI() {
        bt_add = (Button) findViewById(R.id.bt_add);
        lv_blacknumber = (ListView) findViewById(R.id.lv_blacknumber);


        bt_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

        lv_blacknumber.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

                //Log.d("TAG","onScrollStateChanged");
                if(scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
                        && lv_blacknumber.getLastVisiblePosition() >= mBlackNumberInfos.size() - 1
                        && !mIsLoad){

                    //Log.d("TAG","one"+mCount);
                    if(mCount > mBlackNumberInfos.size()){
                        mIsLoad = true;
                        new Thread(){
                            @Override
                            public void run() {
                               // Log.d("TAG","two");
                                mBlackNumberDao = BlackNumberDao.getInstance(getApplicationContext());
                                List<BlackNumberInfo> moreData = mBlackNumberDao.find(mBlackNumberInfos.size());
                                mBlackNumberInfos.addAll(moreData);
                                handler.sendEmptyMessage(0);

                            }
                        }.start();
                    }

                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }

    protected void showDialog(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        View view = View.inflate(getApplicationContext(),R.layout.dialog_add_blacknumber,null);
        dialog.setView(view);

        dialog.show();
        final EditText et_phone = (EditText) view.findViewById(R.id.et_phone);
        RadioGroup group = (RadioGroup) view.findViewById(R.id.rg_group);

        Button bt_submit = (Button) view.findViewById(R.id.bt_submit);
        Button bt_edit = (Button) view.findViewById(R.id.bt_exit);

        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rb_sms:
                         mode = 1;
                        break;
                    case R.id.rb_phone:
                        mode = 2;
                        break;
                    case R.id.rb_all:
                        mode = 3;
                        break;
                }
            }
        });

        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = et_phone.getText().toString();
                if(!TextUtils.isEmpty(phone)){
                    mBlackNumberDao.insert(phone,mode+"");
                    BlackNumberInfo blackNumberInfo = new BlackNumberInfo();
                    blackNumberInfo.mode = mode+"";
                    blackNumberInfo.phone = phone;
                    mBlackNumberInfos.add(blackNumberInfo);
                    myAdapter.notifyDataSetChanged();
                    dialog.dismiss();
                }else {
                    ToastUtil.show(getApplicationContext(),"请输入拦截号码");
                }
            }
        });

        bt_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });



    }
    class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return mBlackNumberInfos.size();
        }

        @Override
        public BlackNumberInfo getItem(int position) {
            return mBlackNumberInfos.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            ViewHolder viewHolder = null;
            if(convertView == null){
                convertView = View.inflate(getApplicationContext(),R.layout.listview_blacknumber,null);
                viewHolder = new ViewHolder();
                viewHolder.iv_delete = (ImageView) convertView.findViewById(R.id.iv_delete);
                viewHolder.tv_mode = (TextView) convertView.findViewById(R.id.tv_mode);
                viewHolder.tv_phone  = (TextView) convertView.findViewById(R.id.tv_phone);
                convertView.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.iv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mBlackNumberDao.delete(mBlackNumberInfos.get(position).phone);
                    mBlackNumberInfos.remove(position);
                    myAdapter.notifyDataSetChanged();
                }
            });
            viewHolder.tv_phone.setText(getItem(position).getPhone());
            int mode = Integer.parseInt(getItem(position).mode);
            switch (mode){
                case 1:
                    viewHolder.tv_mode.setText("短信");
                    break;
                case 2:
                    viewHolder.tv_mode.setText("电话");
                    break;
                case 3:
                    viewHolder.tv_mode.setText("所有");
                    break;

            }
            return convertView;
        }

        class ViewHolder{
            TextView tv_phone;
            TextView tv_mode;
            ImageView iv_delete;

        }

    }
}
