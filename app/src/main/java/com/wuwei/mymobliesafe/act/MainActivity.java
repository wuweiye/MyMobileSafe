package com.wuwei.mymobliesafe.act;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.jauker.widget.BadgeView;
import com.wuwei.mymobliesafe.R;
import com.wuwei.mymobliesafe.act.base.BaseClearCacheActivity;
import com.wuwei.mymobliesafe.adapter.MainAdapter;
import com.wuwei.mymobliesafe.app.Constants;
import com.wuwei.mymobliesafe.bean.MainInfo;
import com.wuwei.mymobliesafe.utils.SPUtil;
import com.wuwei.mymobliesafe.view.shimmer.Shimmer;
import com.wuwei.mymobliesafe.view.shimmer.ShimmerTextView;

import org.askerov.dynamicgrid.DynamicGridView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private int[] ICONID = new int[]{R.drawable.alpha_01,R.drawable.alpha_03
            ,R.drawable.alpha_04,R.drawable.alpha_06,R.drawable.alpha_07,
            R.drawable.alpha_08,R.drawable.alpha_09,R.drawable.icon_01,
            R.drawable.icon_02};
    private String[] TITLES = new String[]{
            "安全管理","通信管理","软件管理","进程管理","手机杀毒","流量监控","清除缓存","高级工具","设置中心"};

    private List<MainInfo> infos;
    private LinearLayout mActivity_main;
    private ImageView mIv_icon;
    private ShimmerTextView mTv_title;
    private com.wuwei.mymobliesafe.view.FocusableTextView mTv_des;
    private ImageView mIv_setting;
    private DynamicGridView mGv_view;
    Shimmer shimmer;

    private Context content = MainActivity.this;

    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bindViews();
        initData();
        initView();
    }

    private void initView() {

        ObjectAnimator animator = ObjectAnimator.ofFloat(mIv_icon,"rotationY",0,90,270,360);
        animator.setDuration(3000);
        animator.setRepeatCount(animator.INFINITE);
        animator.start();
    }

    private void initData() {
        infos = new ArrayList<>();
        for (int i = 0;i < ICONID.length;i++){
            MainInfo mainInfo = new MainInfo();
            mainInfo.setId(i);
            mainInfo.setIcon(ICONID[i]);
            mainInfo.setTitle(TITLES[i]);
            infos.add(mainInfo);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        MainAdapter adapter = new MainAdapter(MainActivity.this,infos,getResources().getInteger(R.integer.column_count));
        mGv_view.setAdapter(adapter);
        LayoutAnimationController lac = new LayoutAnimationController(
                AnimationUtils.loadAnimation(this,R.anim.main_item_anim));
        lac.setOrder(LayoutAnimationController.ORDER_RANDOM);
        mGv_view.setLayoutAnimation(lac);
        mGv_view.startLayoutAnimation();

        //拖拽监听
        mGv_view.setOnDragListener(new DynamicGridView.OnDragListener() {
            @Override
            public void onDragStarted(int position) {
                Log.d("TAG","drag started at postion:"+position);
            }

            @Override
            public void onDragPositionsChanged(int oldPosition, int newPosition) {

                Log.d("TAG","drag started at oldPosition:"+oldPosition+"to"+newPosition);
            }
        });
        //长摁监听
        mGv_view.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                mGv_view.startEditMode(position);
                return true;
            }
        });

        mGv_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //Toast.makeText(MainActivity.this,parent.getAdapter().getItemId(position)+"---",Toast.LENGTH_SHORT).show();

                MainInfo info = (MainInfo) parent.getAdapter().getItem(position);
                switchToPage(info.getId());
            }
        });
    }

    private void switchToPage(int id) {
        switch (id){
            case 0:
                showBuilder();
                break;
            //
            case 1:
                startActivity(BlackNumberActivity.class);
                break;
            case 2:
                startActivity(AppManagerAction.class);
                break;
            case 3:
                startActivity(ProcessActivity.class);
                break;
            case 4:
                startActivity(AnitVirusActivity.class);
                break;
            case 5:
                startActivity(TrafficActivity.class);
                break;
            case 6:
                startActivity(BaseClearCacheActivity.class);
                break;
            case 7:

                startActivity(AToolAction.class);
                break;
            case 8:
                startActivity(Setting2Activity.class);
                break;
        }
    }

    private void showBuilder() {
        String result = SPUtil.getString(content,
                Constants.SJFD_PWD,"");
        if(TextUtils.isEmpty(result)){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            final AlertDialog dialog = builder.create();
            final View view = View.inflate(content,R.layout.init_safe_pwd,null);
            dialog.setView(view);
            dialog.show();

            Button bt_submit = (Button) view.findViewById(R.id.bt_submit);
            Button bt_canc = (Button) view.findViewById(R.id.bt_canc);
            bt_submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditText et_safe_pwd = (EditText) view.findViewById(R.id.et_safe_pwd);
                    EditText et_safe_re_pwd = (EditText) view.findViewById(R.id.et_safe_re_pwd);

                    String password = et_safe_pwd.getText().toString();
                    String rPassword = et_safe_re_pwd.getText().toString();
                    if(!TextUtils.isEmpty(password)&&!TextUtils.isEmpty(rPassword)){

                        if(password.equals(rPassword)){

                            SPUtil.putString(content,Constants.SJFD_PWD,password);
                            startActivity(SetupOverActivity.class);
                            dialog.dismiss();
                        }else {
                            Toast.makeText(content,"两次密码不一致!",Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(content,"password is Empty!",Toast.LENGTH_SHORT).show();
                    }
                }
            });
            bt_canc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            final AlertDialog dialog = builder.create();
            final View view = View.inflate(content,R.layout.validation_safe_pwd,null);
            dialog.setView(view);
            dialog.show();
            Button bt_submit = (Button) view.findViewById(R.id.bt_submit);
            Button bt_canc = (Button) view.findViewById(R.id.bt_canc);

            bt_submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditText et_safe_pwd = (EditText) view.findViewById(R.id.et_safe_pwd);
                    String password = et_safe_pwd.getText().toString();
                    if(TextUtils.isEmpty(password)){
                        Toast.makeText(content,"password is Empty!",Toast.LENGTH_SHORT).show();

                    }else{
                        String vali_password = SPUtil.getString(content,Constants.SJFD_PWD,"");
                        if(password.equals(vali_password)){
                            startActivity(SetupOverActivity.class);
                            dialog.dismiss();
                        }else{
                            Toast.makeText(content,"password is Error!",Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });

        }


    }

    private void startActivity(Class<?> clazz) {

        Intent intent = new Intent(content,clazz);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if (mGv_view.isEditMode()){
            mGv_view.stopEditMode();
        }else {
            moveTaskToBack(true);
        }

    }

    private void bindViews() {

        mActivity_main = (LinearLayout) findViewById(R.id.activity_main);
        mIv_icon = (ImageView) findViewById(R.id.iv_icon);
        mTv_title = (ShimmerTextView) findViewById(R.id.tv_title);
        mTv_des = (com.wuwei.mymobliesafe.view.FocusableTextView) findViewById(R.id.tv_des);
        mIv_setting = (ImageView) findViewById(R.id.iv_setting);
        mGv_view = (DynamicGridView) findViewById(R.id.gv_view);
        if(shimmer != null && shimmer.isAnimating())
        {
            shimmer.cancel();
        }else {
            shimmer = new Shimmer();
            shimmer.start(mTv_title);
        }
        mTv_title.setText("");
        mIv_icon.setImageResource(R.drawable.icon);
        BadgeView badgeView = new BadgeView(this);
        badgeView.setTargetView(mIv_setting);
        badgeView.setBadgeCount(0);
        mTv_des.setText("音无结弦之时，天使跃动之心。立于浮华之世，奏响天籁之音。");
    }
}
