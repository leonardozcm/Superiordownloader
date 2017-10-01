package com.example.com.superiordownloader.Service;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.com.superiordownloader.DbOperator;
import com.example.com.superiordownloader.Information.FileInfo;
import com.example.com.superiordownloader.Information.ThreadInfo;

import org.litepal.crud.DataSupport;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by 59771 on 2017/10/1.
 */

public class DownloadTask {
    private Context mContext=null;
    private FileInfo mFileInfo=null;
    private int mFinished =0;
    private int mThreadCount =1;
    private int lastFinshed=0;//用于计算下载速度，上一次更新UI的完成度
    private long costTime=1000;//默认循环一次时间为一秒
    public boolean mIsPause = false;
    public boolean mIsDelete = false;
    private List<DownloadThread> mThreadlist = null;
    public static ExecutorService mExecutorService = Executors.newCachedThreadPool();

    public DownloadTask(Context mContext, FileInfo mFileInfo, int mThreadCount) {
        super();
        this.mContext = mContext;
        this.mFileInfo = mFileInfo;
        this.mThreadCount = mThreadCount;
    }

    public void download(){
        List<ThreadInfo> list = DataSupport.where("url = ?",mFileInfo.getUrl()).find(ThreadInfo.class);
        /*
       如果是 第一次下载
         */
        if(list.size()==0){
            int length=mFileInfo.getLength();
            int block=length/mThreadCount;//单个url的线程数
            /*
            分配线程
             */
            for (int i = 0; i < mThreadCount; i++) {
                int start = i * block;
                int end = (i + 1) * block - 1;
                if (i == mThreadCount - 1) {
                    end = length - 1;
                }
                ThreadInfo threadInfo = new ThreadInfo(i, mFileInfo.getUrl(), start, end, 0);
                list.add(threadInfo);
            }
        }
        mThreadlist=new ArrayList<>();
        for (ThreadInfo info:list) {
            DownloadThread thread =new DownloadThread(info);
            thread.start();

            //启动线程池管理线程
            DownloadTask.mExecutorService.execute(thread);
            mThreadlist.add(thread);
            /*
            防止重复下载
             */
            if(!DbOperator.isOneExists(info)){
                DbOperator.insertThread(info);
            }
        }
    }
    /*
    synchronized关键字锁线程，保证一个一个地检查状态
     */
public synchronized void checkAllFinished(){
    boolean allFinished=true;
    for(DownloadThread thread:mThreadlist){
        if(!thread.isFinished){
            allFinished=false;break;
        }
    }
    if(allFinished){
        DbOperator.deleteThread(mFileInfo.getUrl());

        Intent intent = new Intent(DownloadService.ACTION_FINISHED);
        intent.putExtra("fileInfo", mFileInfo);
        mContext.sendBroadcast(intent);
    }
}

    class DownloadThread extends Thread{
        private ThreadInfo threadInfo=null;
         boolean isFinished=false;
         DownloadThread(ThreadInfo threadInfo) {
            this.threadInfo = threadInfo;
        }

        @Override
        public void run() {
            HttpURLConnection conn=null;
            RandomAccessFile raf=null;
            InputStream is=null;
            try {
                URL url=new URL(mFileInfo.getUrl());
                conn=(HttpURLConnection)url.openConnection();
                conn.setConnectTimeout(5000);
                conn.setRequestMethod("GET");

                int start=threadInfo.getStart()+threadInfo.getFinished();//从上次结束的地方开始
                conn.setRequestProperty("Range","byte="+start+"-"+threadInfo.getEnd());
                File file=new File(DownloadService.DownloadPath, mFileInfo.getFileName());
                raf=new RandomAccessFile(file,"rwd");
                raf.seek(start);
                mFinished+=threadInfo.getFinished();

                lastFinshed=mFinished;

                Intent intent=new Intent();
                intent.setAction(DownloadService.ACTION_UPDATE);

                int code=conn.getResponseCode();
                if(code==HttpURLConnection.HTTP_PARTIAL){
                    is=conn.getInputStream();
                    byte[] bt=new byte[1024];
                    int len=-1;
                    //定义UI刷新时间
                    long time=System.currentTimeMillis();
                    while ((len=is.read(bt))!=-1){
                        raf.write(bt,0,len);
                        //累积进度
                        mFinished+=len;
                        threadInfo.setFinished(threadInfo.getFinished()+len);
                        //不频繁地更新UI
                        if(System.currentTimeMillis()-time>1000){
                            costTime=System.currentTimeMillis()-time;
                            time=System.currentTimeMillis();
                            intent.putExtra("finished",mFinished*100/mFileInfo.getLength());//完成度
                            intent.putExtra("speed",(double)((mFinished-lastFinshed)/(1024*costTime)));
                            intent.putExtra("id",mFileInfo.getId());

                            lastFinshed=mFinished;

                            Log.i("test", mFinished * 100 / mFileInfo.getLength() + "");

                            mContext.sendBroadcast(intent);
                        }
                        if (mIsDelete){
                            DbOperator.deleteThread(threadInfo.getUrl());
                            if(file.isFile()){
                                file.delete();
                                return;
                            }
                            if(file.isDirectory()){
                                File[] childFile = file.listFiles();
                                if (childFile == null || childFile.length == 0) {
                                    file.delete();
                                    return;
                                }
                                for (File f : childFile) {
                                  f.delete();
                                }
                                file.delete();
                            }
                        }
                        if(mIsPause){
                            DbOperator.updateThread(threadInfo.getUrl(), threadInfo.getId(), threadInfo.getFinished());
                            return;

                        }
                    }
                }
                isFinished=true;
                //判断是否所有线程都执行完毕
                checkAllFinished();
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                if(conn!=null){
                    conn.disconnect();
                }
                try{
                    if(is!=null){
                        is.close();
                    }
                    if(raf!=null){
                        raf.close();
                    }
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
            super.run();
            }
        }
    }
