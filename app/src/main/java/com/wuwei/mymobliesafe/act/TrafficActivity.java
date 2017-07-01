package com.wuwei.mymobliesafe.act;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.TrafficStats;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.wuwei.mymobliesafe.R;
import com.wuwei.mymobliesafe.bean.TrafficInfo;
import com.wuwei.mymobliesafe.view.LoadingView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TrafficActivity extends AppCompatActivity {

    private ListView lv_traffic;
    private PackageManager mPackageManager;
    private List<TrafficInfo> list;
    private LoadingView lv_londing;
    private Date curDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traffic);

        initLoading();
        initTraffic();

        /*//下载流量(不含WIFE)
        long mobileRxBytes = TrafficStats.getMobileRxBytes();

        //总流量(不含WIFE)
        long mobileTxBytes = TrafficStats.getMobileTxBytes();

        long TotalRxBytes = TrafficStats.getTotalRxBytes();

        long TotalTxBytes = TrafficStats.getTotalTxBytes();
*/
    }

    private void initLoading() {

        lv_londing = (LoadingView) findViewById(R.id.lv_loading);
        curDate = new Date(System.currentTimeMillis());
        lv_londing.load();

    }

    private void initTraffic() {
        lv_traffic = (ListView) findViewById(R.id.lv_traffic);
        mPackageManager = getPackageManager();
        ScanTask task =  new ScanTask();
        task.execute();

    }

    private class ScanTask extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... params) {
            List<PackageInfo> installPackage = mPackageManager.getInstalledPackages(0);
            list = new ArrayList<TrafficInfo>();
            for(PackageInfo info :installPackage){
                TrafficInfo trafficInfo = new TrafficInfo();
                trafficInfo.appName = info.applicationInfo.loadLabel(mPackageManager).toString();
                trafficInfo.icon = info.applicationInfo.loadIcon(mPackageManager);
                trafficInfo.rcv= 0;
                int uid = info.applicationInfo.uid;
                long uidRxbytes = etRcv(uid);
                long uidtxBytes = getSnd(uid);

                //Log.d("TAG",uid+"----");
                if(uidRxbytes != 0 && uidtxBytes != 0){
                    trafficInfo.rcv = uidRxbytes;
                    trafficInfo.snd = uidtxBytes;
                    list.add(trafficInfo);
                }
            }

            Date endDate = new Date(System.currentTimeMillis());
            long date = endDate.getTime() - curDate.getTime();
            if(date > 300 && date < 2000 ){
                try {
                    Thread.sleep(2000 - date);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);


            lv_londing.loadSuccess();
            TrafficManagerAdapter adapter = new TrafficManagerAdapter();
            lv_traffic.setAdapter(adapter);
        }
    }

    private long etRcv(int uid) {
        File file = new File("/proc/uid_stat/"+uid+"/tcp_rcv");
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String readLine  = reader.readLine();
            return Long.parseLong(readLine);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    private long getSnd(int uid) {

        File file = new File("/proc/uid_stat/"+uid+"/tcp_snd");
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String readLine  = reader.readLine();
            return Long.parseLong(readLine);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    class TrafficManagerAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return list == null ? 0 : list.size();
        }

        @Override
        public TrafficInfo getItem(int position) {
            if(list != null){
                return list.get(position);
            }
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if(convertView == null){
                convertView = convertView.inflate(getApplicationContext(),R.layout.item_traffic,null);
                holder = new ViewHolder();
                holder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
                holder.tv_app_name = (TextView) convertView.findViewById(R.id.tv_app_name);
                holder.tv_app_rcv = (TextView) convertView.findViewById(R.id.tv_app_rcv);
                holder.tv_app_snd = (TextView) convertView.findViewById(R.id.tv_app_snd);
                holder.tv_total = (TextView) convertView.findViewById(R.id.tv_total);
                convertView.setTag(holder);
            }else {
                holder = (ViewHolder) convertView.getTag();
            }


            TrafficInfo info = getItem(position);
            holder.iv_icon.setImageDrawable(info.icon);
            holder.tv_app_name.setText(info.appName);
            holder.tv_app_snd.setText("上传:"+ Formatter.formatFileSize(getApplicationContext(),info.snd));
            holder.tv_app_rcv.setText("下载:"+Formatter.formatFileSize(getApplicationContext(),info.rcv));
            holder.tv_total.setText("总流量:"+Formatter.formatFileSize(getApplicationContext(),info.rcv+info.snd));

            return convertView;
        }

         class ViewHolder{
            ImageView iv_icon;
             TextView tv_app_name;
             TextView tv_app_snd;
             TextView tv_app_rcv;
             TextView tv_total;
        }
    }
}
