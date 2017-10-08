package com.example.com.superiordownloader.Util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by 59771 on 2017/10/7.
 */

public class DBHelper extends SQLiteOpenHelper {
    public static final String DB_NAME="download.db";
    private static final int VERSION =1;
    private static final String THREADINFO_SQL_CREATE="CREATE TABLE thread_info (id integer primary key autoincrement," +
            "end integer, " +
            "finished integer, start integer, url text)";
    private static final String FILEINFO_SQL_CREATE="CREATE TABLE file_info (id integer primary key autoincrement," +
            "filename text, finished integer, isstop integer, length integer, speed real, url text)";
    private static final String THREADINFO_SQL_DROP="drop table if exists thread_info";
    private static final String FILEINFO_SQL_DROP="drop table if exists file_info";
    private static DBHelper mHelper=null;

    private DBHelper(Context context){
        super(context,DB_NAME,null,VERSION);
    }

    public static DBHelper getInstance(Context context){
        if(mHelper==null){
            mHelper=new DBHelper(context);
        }
        return mHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(THREADINFO_SQL_CREATE);
        db.execSQL(FILEINFO_SQL_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(THREADINFO_SQL_DROP);
        db.execSQL(FILEINFO_SQL_DROP);
    }
}
