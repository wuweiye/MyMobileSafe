package com.wuwei.mymobliesafe.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wuwei.mymobliesafe.R;
import com.wuwei.mymobliesafe.bean.MainInfo;

import org.askerov.dynamicgrid.BaseDynamicGridAdapter;

import java.util.List;

/**
 * Created by 无为 on 2017/3/15.
 */

public class MainAdapter extends BaseDynamicGridAdapter {

    /*private final Context context;
    private final List<MainInfo> infos;
    private int columnCount ;

    public  MainAdapter(Context context, List<MainInfo> infos){
        this.context = context;
        this.infos = infos;
    }*/
    public  MainAdapter(Context context, List<?> infos,int columnCount){
       super(context,infos,columnCount);
    }

    @Override
    public Object getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if(convertView == null){
            convertView = View.inflate(getContext(),R.layout.item_main,null);
            viewHolder = new ViewHolder();
            viewHolder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
            viewHolder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);

            convertView.setTag(viewHolder);
        }else
            viewHolder = (ViewHolder) convertView.getTag();

        MainInfo info = (MainInfo) getItem(position);
        viewHolder.iv_icon.setImageResource(info.getIcon());
        viewHolder.tv_title.setText(info.getTitle());
        return convertView;
    }
    /*@Override
    public int getCount() {
        return infos.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

    }
    */

    static class ViewHolder{
        ImageView iv_icon;
        TextView tv_title;
    }
}
