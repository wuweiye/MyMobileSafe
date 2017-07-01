package com.wuwei.mymobliesafe.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by 无为 on 2017/5/29.
 */

public class AppLockOpenHelper extends SQLiteOpenHelper {
    private static final String name ="applock.db";
    public AppLockOpenHelper(Context context) {
        super(context, name, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table applock" +
                "(_id integer primary key autoincrement," +
                "packagename varchar(50));");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
