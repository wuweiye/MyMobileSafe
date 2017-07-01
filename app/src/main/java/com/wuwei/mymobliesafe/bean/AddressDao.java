package com.wuwei.mymobliesafe.bean;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by 无为 on 2017/5/12.
 */

public class AddressDao {
    public static String path = "data/data/com.wuwei.mymobliesafe/files/address.db";
    private static String mAddress;

    public static String getAddress(String phone){
        mAddress = "未知号码";
        String regex = "^1[3-8]\\d{9}";
        if(phone.matches(regex)){
            phone = phone.substring(0,7);
            SQLiteDatabase db = SQLiteDatabase.openDatabase(path,null,SQLiteDatabase.OPEN_READONLY);
            Cursor cursor = db.query("info",new String[]{"cardtype"},"mobileprefix = ?"
                    ,new String[]{phone},null,null,null);

            if (cursor.moveToNext()) {
                String cardtype = cursor.getString(0);
                Log.i("TAG","cardtype = "+cardtype);
                mAddress = cardtype;
            }
        }else {
            int length = phone.length();
            switch (length){
                case 3:
                    mAddress = "报警电话";
                    break;
                case 4:
                    mAddress = "4";
                    break;
                case 5: //10086
                    mAddress = "10086";
                    break;
                case 7:
                    mAddress = "本地电话";
                    break;
                case 8:
                    mAddress = "本地电话";
                    break;
                case 11:
                    break;
            }
        }

        return mAddress;
    }
}
