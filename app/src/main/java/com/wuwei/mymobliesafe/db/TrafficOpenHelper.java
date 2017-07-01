package com.wuwei.mymobliesafe.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by 无为 on 2017/6/16.
 */

public class TrafficOpenHelper extends SQLiteOpenHelper {
    private static final String name = "traffic.db";

    public TrafficOpenHelper(Context context) {
        super(context, name, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table traffic" +
                "(_id integer primary key autoincrement," +
                "servicename varchar(50));");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
