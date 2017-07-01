package com.wuwei.mymobliesafe.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 无为 on 2017/5/29.
 */

public class VirusDao {
    public static String path = "data/data/com.wuwei.mymobliesafe/files/antivirus.db";

    public static List<String> getVirusList(){
        SQLiteDatabase database = SQLiteDatabase.openDatabase(path,null,SQLiteDatabase.OPEN_READONLY);
        Cursor cursor = database.query("datable",new String[]{"md5"},null,null,null,null,null);
        List<String> virusList = new ArrayList<>();
        while (cursor.moveToNext()){
            virusList.add(cursor.getString(0));
        }

        cursor.close();
        database.close();
        return virusList;
    }
}
