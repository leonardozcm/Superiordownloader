package com.example.com.superiordownloader;

import android.util.Log;

import com.example.com.superiordownloader.Information.FileInfo;
import com.example.com.superiordownloader.Information.ThreadInfo;

import org.litepal.crud.DataSupport;

import java.util.List;

import static org.litepal.crud.DataSupport.findAll;

/**
 * Created by 59771 on 2017/10/1.
 */

public class DbOperator {
    final static String TAG="DbOperator";
//添加线程
static public void insertFile(FileInfo fileInfo){

    fileInfo.save();
}
   static public void insertThread(ThreadInfo info) {
       info.save();
   }
//删除线程/事件
static public void  deleteFileInfo(String url) {
    DataSupport.deleteAll(FileInfo.class,"url = ?",url);
}
    static public void  deleteThread(String url) {
        DataSupport.deleteAll(ThreadInfo.class,"url = ?",url);
    }
//更新线程
    static public void updateFileInfo(String url,int finish){
        FileInfo fileInfo=new FileInfo();
        fileInfo.setFinished(finish);
        fileInfo.setSpeed(0);
        fileInfo.updateAll("url = ?",url);
        int num=fileInfo.updateAll("url = ?",url);
        Log.d(TAG, "updateThread: "+num);
    }
    static public void updateFileInfo(String url){
        FileInfo fileInfo=new FileInfo();
        List<FileInfo> infolist=DataSupport.where("url = ?",url).find(FileInfo.class);
        fileInfo.setIsStop(infolist.get(0).getIsStop()+1);
        fileInfo.setSpeed(0);
        fileInfo.updateAll("url = ?",url);
    }

static public void  updateThread(String url, int id, int finished) {
    ThreadInfo threadInfo=new ThreadInfo();
    threadInfo.setFinished(finished);
    threadInfo.updateAll("url = ? and id = ?",url,Integer.toString(id));
    FileInfo fileInfo=new FileInfo();
    fileInfo.setFinished(finished);
}
    static public void  updateThread(String url,boolean isStop) {
        ThreadInfo threadInfo=new ThreadInfo();
        threadInfo.setStop(isStop);
        threadInfo.updateAll("url = ?",url);
    }
//查询线程
    static public List<ThreadInfo> FindALL(){
        List<ThreadInfo> list= findAll(ThreadInfo.class);
        return  list;
    }
    static public List<ThreadInfo> queryThreads(String url) {
    List<ThreadInfo> list =DataSupport.where("url = ?",url).find(ThreadInfo.class);
    return list;
}
//检查线程池是否为空
    static public boolean isAnyExists(){
    List<ThreadInfo> list = findAll(ThreadInfo.class);
    return !list.isEmpty();
}
//查询是否存在某一线程
    static public boolean isOneExists(ThreadInfo info){
        List<ThreadInfo> list =DataSupport.where("url = ? and id = ?",info.getUrl(),Integer.toString(info.getId())).find(ThreadInfo.class);
        if(list.size()==0)return false;
        else return true;
    }
}
