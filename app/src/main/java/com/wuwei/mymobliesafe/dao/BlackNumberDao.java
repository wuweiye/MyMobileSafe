package com.wuwei.mymobliesafe.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.wuwei.mymobliesafe.bean.BlackNumberInfo;
import com.wuwei.mymobliesafe.db.BlackNumberOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 无为 on 2017/5/15.
 */

public class BlackNumberDao {
    private BlackNumberOpenHelper numberOpenHelper;
    private static final String table = "blacknumber";
    private BlackNumberDao(Context context){

        numberOpenHelper =  new BlackNumberOpenHelper(context);
    }
    private static BlackNumberDao blackNumberDao = null;

    public static BlackNumberDao getInstance(Context context){
        if(blackNumberDao == null){
            blackNumberDao = new BlackNumberDao(context);
        }
        return blackNumberDao;
    }


    public void insert (String phone,String mode){
        SQLiteDatabase database = numberOpenHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("phone",phone);
        values.put("mode",mode);
        database.insert(table,null,values);
        database.close();
    }

    public void delete(String phone){
        SQLiteDatabase database = numberOpenHelper.getWritableDatabase();
        database.delete(table,"phone = ?",new String[]{phone});
        database.close();
    }

    public void update(String phone ,String mode){
        SQLiteDatabase database = numberOpenHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("mode",mode);
        database.update(table,values,"phone = ?",new String[]{phone});
        database.close();
    }

    public List<BlackNumberInfo> findAll(){
        List<BlackNumberInfo> blackNumberInfos = new ArrayList<>();
        SQLiteDatabase database = numberOpenHelper.getReadableDatabase();
        Cursor cursor = database.query(table,new String[]{"phone","mode"},null,null,null,null,"_id desc");
        while (cursor.moveToNext()){
            BlackNumberInfo blackNumberInfo = new BlackNumberInfo();
            blackNumberInfo.phone = cursor.getString(0);
            blackNumberInfo.mode = cursor.getString(1);
            blackNumberInfos.add(blackNumberInfo);

        }
        cursor.close();
        database.close();

        return blackNumberInfos;

    }

    public List<BlackNumberInfo> find(int index){
        List<BlackNumberInfo> blackNumberInfos = new ArrayList<>();
        SQLiteDatabase database = numberOpenHelper.getReadableDatabase();
        Cursor cursor = database.rawQuery("select phone,mode from "+table+" order by _id desc limit ? ,20;",new String[]{index+""});

       // Cursor cursor = database.query(table,new String[]{"phone","mode"},null,null,null,null,"_id desc");
        while (cursor.moveToNext()){
            BlackNumberInfo blackNumberInfo = new BlackNumberInfo();
            blackNumberInfo.phone = cursor.getString(0);
            blackNumberInfo.mode = cursor.getString(1);
            blackNumberInfos.add(blackNumberInfo);

        }
        cursor.close();
        database.close();

        return blackNumberInfos;

    }
    public int getCount(){
        SQLiteDatabase database = numberOpenHelper.getReadableDatabase();
        int count = 0;
        Cursor cursor = database.rawQuery("select count(*) from "+table+";",null);
        if(cursor.moveToNext()){
            count = cursor.getInt(0);
            //Log.d("TAG","cursor:"+count);
        }
        cursor.close();
        database.close();
        return count;
    }

    public int getMode(String phone){
        SQLiteDatabase database = numberOpenHelper.getReadableDatabase();
        int mode = 0;
        Cursor cursor = database.query(table,new String[]{"mode"},"phone = ?"
                ,new String[]{phone},null,null,null);
        if(cursor.moveToNext()){
            mode = cursor.getInt(0);
            //Log.d("TAG","cursor:"+count);
        }
        cursor.close();
        database.close();
        return mode;
    }
}
