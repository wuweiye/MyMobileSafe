package com.wuwei.mymobliesafe.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.wuwei.mymobliesafe.db.AppLockOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 无为 on 2017/5/29.
 */

public class AppLockDao {
    private static final String table ="applock";
    private Context context;
    private AppLockOpenHelper appLockOpenHelper;
    private AppLockDao(Context context){
        this.context = context;
        appLockOpenHelper = new AppLockOpenHelper(context);
    }
    private static AppLockDao appLockDao = null;

    public static AppLockDao getInstance(Context context){
        if(appLockDao == null){
            appLockDao = new AppLockDao(context);
        }

        return appLockDao;
    }

    public void insert (String packageName){
        SQLiteDatabase database = appLockOpenHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("packagename",packageName);
        database.insert(table,null,values);
        database.close();
        context.getContentResolver().notifyChange(Uri.parse("content://applock/change"),null);
    }

    public void delete(String packageName){
        SQLiteDatabase database = appLockOpenHelper.getWritableDatabase();
        database.delete(table,"packagename = ?",new String[]{packageName});
        database.close();
        context.getContentResolver().notifyChange(Uri.parse("content://applock/change"),null);
    }

    public List<String> findAll(){
        SQLiteDatabase database = appLockOpenHelper.getReadableDatabase();
        Cursor cursor = database.query(table,new String[]{"packagename"},null,null,null,null,null);
        List<String> lockPackageList = new ArrayList<>();
        while (cursor.moveToNext()){

            lockPackageList.add(cursor.getString(0));
        }

        cursor.close();
        database.close();
        return lockPackageList;
    }
}
