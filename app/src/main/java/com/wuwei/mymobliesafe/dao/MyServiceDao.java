package com.wuwei.mymobliesafe.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.wuwei.mymobliesafe.db.MyServiceOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 无为 on 2017/6/15.
 */

public class MyServiceDao {

    private static final String table ="servicelock";
    private Context context;
    MyServiceOpenHelper openHelper;

    private  MyServiceDao(Context context){
        this.context = context;
        openHelper = new MyServiceOpenHelper(context);
    }

    private static MyServiceDao myServiceDao = null;

    public static MyServiceDao getInstance(Context context){
        if (myServiceDao == null){
            myServiceDao = new MyServiceDao(context);
        }

        return myServiceDao;
    }

    public void insert (String servicename){
        SQLiteDatabase database = openHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("servicename",servicename);
        database.insert(table,null,values);
        database.close();
        //context.getContentResolver().notifyChange(Uri.parse("content://applock/change"),null);
    }

    public void delete(String servicename){
        SQLiteDatabase database = openHelper.getWritableDatabase();
        database.delete(table,"servicename = ?",new String[]{servicename});
        database.close();
        //context.getContentResolver().notifyChange(Uri.parse("content://applock/change"),null);
    }

    public List<String> findAll(){
        SQLiteDatabase database = openHelper.getReadableDatabase();
        Cursor cursor = database.query(table,new String[]{"servicename"},null,null,null,null,null);
        List<String> lockPackageList = new ArrayList<>();
        while (cursor.moveToNext()){

            lockPackageList.add(cursor.getString(0));
        }

        cursor.close();
        database.close();
        return lockPackageList;
    }



}
