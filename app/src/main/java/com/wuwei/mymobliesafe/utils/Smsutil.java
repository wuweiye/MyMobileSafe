package com.wuwei.mymobliesafe.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Xml;

import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by 无为 on 2017/5/16.
 */

public class Smsutil {
    private static int index = 0;

    public static void backup(Context context, String path, CallBack callBack){

        FileOutputStream fos = null;

        Cursor cursor = null;
        try {
            File file = new File(path);
            Uri uri = Uri.parse("content://sms/");
            cursor= context.getContentResolver().query(uri,new String[]{
                    "address","date","type","body"
            },null,null,null);
            fos = new FileOutputStream(file);
            XmlSerializer xmlSerializer = Xml.newSerializer();
            xmlSerializer.setOutput(fos,"utf-8");
            xmlSerializer.startDocument("utf-8",true);
            xmlSerializer.startTag(null,"smss");

            /**
             * 短信总数制定
             */
        if(callBack != null){
            callBack.setMax(cursor.getCount());
        }
            while (cursor.moveToNext()){
                xmlSerializer.startTag(null,"sms");

                xmlSerializer.startTag(null,"address");
                xmlSerializer.text(cursor.getString(0));
                xmlSerializer.endTag(null,"address");

                xmlSerializer.startTag(null,"date");
                xmlSerializer.text(cursor.getString(1));
                xmlSerializer.endTag(null,"date");

                xmlSerializer.startTag(null,"type");
                xmlSerializer.text(cursor.getString(2));
                xmlSerializer.endTag(null,"type");

                xmlSerializer.startTag(null,"body");
                xmlSerializer.text(cursor.getString(3));
                xmlSerializer.endTag(null,"body");

                xmlSerializer.endTag(null,"sms");

                index++;
                Thread.sleep(500);
                if(callBack != null){
                    callBack.setProgress(index);
                }

            }


            xmlSerializer.endTag(null,"smss");
            xmlSerializer.endDocument();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(cursor!= null && fos != null){

                try {
                    cursor.close();
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 定义接口
     * 定义接口中未实现的业务逻辑
     * 传递一个实现持接口的对象
     * 获取传进来的对象，再合适的地方调用
     */

    public interface CallBack{
        //
        public void setMax(int max);

        public void setProgress(int index);
    }
}
