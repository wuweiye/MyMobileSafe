package com.wuwei.mymobliesafe.act;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.wuwei.mymobliesafe.R;
import com.wuwei.mymobliesafe.utils.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ContactListActivity extends Activity {

    private ListView lv_contact;
    private ContentResolver contentResolver;
    private List<HashMap<String,String>> contactList = new ArrayList<>();
    private MyAdapter adapter;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            adapter = new MyAdapter();
            lv_contact.setAdapter(adapter);
        }
    } ;

    private List<String> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);
        initUI();
        initData();
    }

    class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return contactList.size();
        }

        @Override
        public HashMap<String, String> getItem(int position) {
            return contactList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View view = View.inflate(getApplicationContext(),R.layout.listview_contact_item,null);
            TextView tv_name = (TextView) view.findViewById(R.id.tv_name);
            TextView tv_phone = (TextView) view.findViewById(R.id.tv_phone);

            tv_name.setText(getItem(position).get("name"));
            tv_phone.setText(getItem(position).get("phone"));

            return view;
        }
    }
    private void initData() {

        new Thread(){
            @Override
            public void run() {

                contentResolver = getContentResolver();
                //Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
                Cursor cursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,null,null,null);
                contactList.clear();
                while (cursor.moveToNext()){

                    String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    String phone = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    HashMap<String,String > hashMap = new HashMap<String, String>();
                    hashMap.put("phone",phone);
                    hashMap.put("name",name);
                   // String id = cursor.getString(0);

                    /*Cursor indexCursor = contentResolver.query(Uri.parse("content://com.android.contacts/data"),
                            new String[]{"data1","mimetype"},"raw_contact_id = ?",
                            new String[]{id},null);


                    while (indexCursor.moveToNext()){
                        String data = indexCursor.getString(0);
                        String type = indexCursor.getString(1);

                        if(type.equals("vnd.android.cursor.item/phone_v2")){
                            if(!TextUtils.isEmpty(data))

                        }else if(type.equals("vnd.android.cursor.item/name")){
                            hashMap.put("name",data);
                        }
                    }
                    indexCursor.close();
                    contactList.add(hashMap);
                    */
                    contactList.add(hashMap);

                }
                //

                cursor.close();
                mHandler.sendEmptyMessage(0);
            }
        }.start();


    }

    private void initUI() {
        lv_contact = (ListView) findViewById(R.id.lv_contact);

        lv_contact.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(adapter != null){
                    HashMap<String,String> hashMap = adapter.getItem(position);
                    String phone = hashMap.get("phone");
                    Intent intent = new Intent();
                    intent.putExtra("phone",phone);
                    setResult(0,intent);

                    finish();
                }
            }
        });
    }
}
