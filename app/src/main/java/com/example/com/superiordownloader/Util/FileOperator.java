package com.example.com.superiordownloader.Util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.com.superiordownloader.Information.FileInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 59771 on 2017/10/7.
 */

public class FileOperator {
    private DBHelper dbHelper=null;

    public FileOperator(Context context){
        super();
        this.dbHelper=DBHelper.getInstance(context);
    }
    //插入数据
    public synchronized void insertFile(FileInfo info){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        ContentValues values = new ContentValues();

        values.put("id", info.getId());
        values.put("filename", info.getFileName());
        values.put("finished", info.getFinished());
        values.put("isstop",info.getIsStop());
        values.put("length", info.getLength());
        values.put("speed",info.getSpeed());
        values.put("url", info.getUrl());

        db.insert("file_info", null, values);

    }
    //删除文件
    public synchronized void deleteFileInfo(String url) {
        SQLiteDatabase db=dbHelper.getReadableDatabase();
        db.delete("file_info","url = ?",new String[]{url});
    }
    public synchronized void deleteAll(){
        SQLiteDatabase db=dbHelper.getReadableDatabase();
        db.delete("file_info",null,null);
    }
    //更新文件
    public synchronized void updateFileInfo(String url,int finish){
        SQLiteDatabase db=dbHelper.getReadableDatabase();
        db.execSQL("update file_info set finished = ? where url = ?",new Object[]{finish,url});
    }
    public synchronized void updateFileInfoLength(String url,int length){
        SQLiteDatabase db=dbHelper.getReadableDatabase();
        db.execSQL("update file_info set length = ? where url = ?",new Object[]{length,url});
    }
    public synchronized void updateFileInfo(String url){
        FileInfo fileInfo=new FileInfo();
        List<FileInfo> infolist= queryFiles(url);
        SQLiteDatabase db=dbHelper.getReadableDatabase();
        Log.d("FileOperter", "onClick: "+infolist.get(0).getIsStop());
        db.execSQL("update file_info set isstop = ? where url = ?",new Object[]{infolist.get(0).getIsStop()+1,url});
    }
    //查询文件
    public synchronized List<FileInfo> findAll() {
        SQLiteDatabase db=dbHelper.getReadableDatabase();

        List<FileInfo> list = new ArrayList<FileInfo>();

        Cursor cursor=db.query("file_info",null,null,null,null,null,null);
        while (cursor.moveToNext()){
            FileInfo fileInfo=new FileInfo();
            fileInfo.setId(cursor.getInt(cursor.getColumnIndex("id")));
            fileInfo.setFileName(cursor.getString(cursor.getColumnIndex("filename")));
            fileInfo.setFinished(cursor.getInt(cursor.getColumnIndex("finished")));
            fileInfo.setIsStop(cursor.getInt(cursor.getColumnIndex("isstop")));
            fileInfo.setLength(cursor.getInt(cursor.getColumnIndex("length")));
            fileInfo.setUrl(cursor.getString(cursor.getColumnIndex("url")));
            fileInfo.setSpeed(cursor.getDouble(cursor.getColumnIndex("speed")));
            list.add(fileInfo);
        }
        cursor.close();
        return  list;
    }
    public synchronized List<FileInfo> queryFiles(String url) {
        SQLiteDatabase db=dbHelper.getReadableDatabase();

        List<FileInfo> list = new ArrayList<FileInfo>();

        Cursor cursor=db.query("file_info",null,"url = ?",new String[]{url},null,null,null);
        while (cursor.moveToNext()){
            FileInfo fileInfo=new FileInfo();
            fileInfo.setId(cursor.getInt(cursor.getColumnIndex("id")));
            fileInfo.setFileName(cursor.getString(cursor.getColumnIndex("filename")));
            fileInfo.setFinished(cursor.getInt(cursor.getColumnIndex("finished")));
            fileInfo.setIsStop(cursor.getInt(cursor.getColumnIndex("isstop")));
            fileInfo.setLength(cursor.getInt(cursor.getColumnIndex("length")));
            fileInfo.setUrl(cursor.getString(cursor.getColumnIndex("url")));
            fileInfo.setSpeed(cursor.getDouble(cursor.getColumnIndex("speed")));
            list.add(fileInfo);
        }
        cursor.close();
        return  list;
    }
    public synchronized List<FileInfo> queryFilesNOTCompleted() {
        SQLiteDatabase db=dbHelper.getReadableDatabase();

        List<FileInfo> list = new ArrayList<FileInfo>();

        Cursor cursor=db.query("file_info",null,"finished != 100",null,null,null,null);
        while (cursor.moveToNext()){
            FileInfo fileInfo=new FileInfo();
            fileInfo.setId(cursor.getInt(cursor.getColumnIndex("id")));
            fileInfo.setFileName(cursor.getString(cursor.getColumnIndex("filename")));
            fileInfo.setFinished(cursor.getInt(cursor.getColumnIndex("finished")));
            fileInfo.setIsStop(cursor.getInt(cursor.getColumnIndex("isstop")));
            fileInfo.setLength(cursor.getInt(cursor.getColumnIndex("length")));
            fileInfo.setUrl(cursor.getString(cursor.getColumnIndex("url")));
            fileInfo.setSpeed(cursor.getDouble(cursor.getColumnIndex("speed")));
            list.add(fileInfo);
        }
        cursor.close();
        return  list;
    }
    public synchronized List<FileInfo> queryFilesCompleted() {
        SQLiteDatabase db=dbHelper.getReadableDatabase();

        List<FileInfo> list = new ArrayList<FileInfo>();

        Cursor cursor=db.query("file_info",null,"finished = 100",null,null,null,null);
        while (cursor.moveToNext()){
            FileInfo fileInfo=new FileInfo();
            fileInfo.setId(cursor.getInt(cursor.getColumnIndex("id")));
            fileInfo.setFileName(cursor.getString(cursor.getColumnIndex("filename")));
            fileInfo.setFinished(cursor.getInt(cursor.getColumnIndex("finished")));
            fileInfo.setIsStop(cursor.getInt(cursor.getColumnIndex("isstop")));
            fileInfo.setLength(cursor.getInt(cursor.getColumnIndex("length")));
            fileInfo.setUrl(cursor.getString(cursor.getColumnIndex("url")));
            fileInfo.setSpeed(cursor.getDouble(cursor.getColumnIndex("speed")));
            list.add(fileInfo);
        }
        cursor.close();
        return  list;
    }
    //查询id最大值
    public int getMaxId(){
        SQLiteDatabase db=dbHelper.getReadableDatabase();
        Cursor c=db.query("file_info",new String[]{"MAX(id)"},null,null,null,null,null);
        String max_id="0";
        if(c.getCount()>0){
            c.moveToFirst();
            max_id=c.getString(0);
        }
        c.close();
        int max=0;
        try{
            max=Integer.parseInt(max_id);
        }catch (Exception e){
            e.printStackTrace();
        }

        return max;
    }
}
