package com.example.zjl.musicplayer;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.Settings;

/**
 * Created by ZJL on 2018/4/6.
 */

public class MyOpenHelper extends SQLiteOpenHelper {
    public String createTableSQL="create table if not exists music _tb"+
            "(_id integer primary key autoincrement,title,artist,album,album_id,time,url)";
    public MyOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,int version){
        super (context,name ,factory,version);
    }
    public void onCreate(SQLiteDatabase db){
        db.execSQL(createTableSQL);
    }
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion){
        System.out.println("版本变化:"+oldVersion+"------->"+newVersion);
    }
}
