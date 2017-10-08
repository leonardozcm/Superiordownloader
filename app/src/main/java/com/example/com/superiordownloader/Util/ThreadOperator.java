package com.example.com.superiordownloader.Util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.com.superiordownloader.Information.ThreadInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 59771 on 2017/10/7.
 */

public class ThreadOperator {
    private DBHelper dbHelper=null;

    public ThreadOperator(Context context){
        super();
        this.dbHelper=DBHelper.getInstance(context);
    }
//插入数据
    public synchronized void insertThread(ThreadInfo info) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        ContentValues values = new ContentValues();

        values.put("id", info.getId());
        values.put("url", info.getUrl());
        values.put("start", info.getStart());
        values.put("end", info.getEnd());
        values.put("finished", info.getFinished());
        db.insert("thread_info", null, values);
    }
//删除数据
    public synchronized void deleteThread(String url){
        SQLiteDatabase db=dbHelper.getReadableDatabase();
        db.delete("thread_info","url = ?",new String[]{url});
    }
    public synchronized void deleteAll(){
        SQLiteDatabase db=dbHelper.getReadableDatabase();
        db.delete("thread_info",null,null);
    }
//更新数据
    public synchronized void updateThread(String url, int id, int finished) {
        SQLiteDatabase db=dbHelper.getReadableDatabase();
        db.execSQL("update thread_info set finished = ? where url = ? and id = ?",new Object[]{finished,url,id});
    }
//查询数据
    public synchronized List<ThreadInfo> FindALL(){
        SQLiteDatabase db=dbHelper.getReadableDatabase();

        List<ThreadInfo> list = new ArrayList<ThreadInfo>();

        Cursor cursor=db.query("thread_info",null,null,null,null,null,null);
        while (cursor.moveToNext()){
            ThreadInfo threadInfo=new ThreadInfo();
            threadInfo.setId(cursor.getInt(cursor.getColumnIndex("id")));
            threadInfo.setUrl(cursor.getString(cursor.getColumnIndex("url")));
            threadInfo.setStart(cursor.getInt(cursor.getColumnIndex("start")));
            threadInfo.setEnd(cursor.getInt(cursor.getColumnIndex("end")));
            threadInfo.setFinished(cursor.getInt(cursor.getColumnIndex("finished")));
            list.add(threadInfo);
        }
        cursor.close();
        return  list;
    }

    public synchronized boolean isOneExists(ThreadInfo info){
        SQLiteDatabase db=dbHelper.getReadableDatabase();

        List<ThreadInfo> list = new ArrayList<ThreadInfo>();

        Cursor cursor=db.query("thread_info",null,"url = ? and id = ?",new String[]{info.getUrl(),Integer.toString(info.getId())},null,null,null);
        while (cursor.moveToNext()){
            ThreadInfo threadInfo=new ThreadInfo();
            threadInfo.setId(cursor.getInt(cursor.getColumnIndex("id")));
            threadInfo.setUrl(cursor.getString(cursor.getColumnIndex("url")));
            threadInfo.setStart(cursor.getInt(cursor.getColumnIndex("start")));
            threadInfo.setEnd(cursor.getInt(cursor.getColumnIndex("end")));
            threadInfo.setFinished(cursor.getInt(cursor.getColumnIndex("finished")));
            list.add(threadInfo);
        }

        if(list.size()==0)return false;
        else return true;
    }

    public synchronized List<ThreadInfo> queryThreads(String url){
        SQLiteDatabase db=dbHelper.getReadableDatabase();

        List<ThreadInfo> list = new ArrayList<ThreadInfo>();

        Cursor cursor=db.query("thread_info",null,"url = ?",new String[]{url},null,null,null);
        while (cursor.moveToNext()){
            ThreadInfo threadInfo=new ThreadInfo();
            threadInfo.setId(cursor.getInt(cursor.getColumnIndex("id")));
            threadInfo.setUrl(cursor.getString(cursor.getColumnIndex("url")));
            threadInfo.setStart(cursor.getInt(cursor.getColumnIndex("start")));
            threadInfo.setEnd(cursor.getInt(cursor.getColumnIndex("end")));
            threadInfo.setFinished(cursor.getInt(cursor.getColumnIndex("finished")));
            list.add(threadInfo);
        }
        cursor.close();
        return  list;
    }
//检查线程池是否为空
    public synchronized boolean isAnyExists(){
        List<ThreadInfo> list = FindALL();
        return !list.isEmpty();
    }
}
