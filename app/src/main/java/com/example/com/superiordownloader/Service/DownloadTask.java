package com.example.com.superiordownloader.Service;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.com.superiordownloader.Information.FileInfo;
import com.example.com.superiordownloader.Information.ThreadInfo;
import com.example.com.superiordownloader.UpdateReceiver;
import com.example.com.superiordownloader.Util.DbOperator;

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

import static android.content.ContentValues.TAG;

/**
 * Created by 59771 on 2017/10/1.
 */

public class DownloadTask {
    private List<ThreadInfo> list;
    private long mFinished =0;
    private Context mContext=null;
    private FileInfo mFileInfo=null;
    private int mThreadCount =1;
    private long lastFinshed=0;//用于计算下载速度，上一次更新UI的完成度
    private long lastloopTime;
    private long costTime=1000;//默认循环一次时间为一秒
    private int looptimes=0;//用于记录执行1s循环的次数，等于3就更新速度
    private Double speed=0.0;//下载速度
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
        list = DataSupport.where("url = ?",mFileInfo.getUrl()).find(ThreadInfo.class);
        /*
       如果是 第一次下载
         */
        boolean IsFirst=(list.size()==0);
        if(IsFirst){
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
                Log.d(TAG, "At Start:"+threadInfo.getStart()+",AND At End:"+threadInfo.getEnd());
                list.add(threadInfo);
            }
        }
        mThreadlist=new ArrayList<>();
        for (ThreadInfo info:list) {
            DownloadThread thread =new DownloadThread(info);
            Log.d(TAG, "At LOOP threadInfo.getId()"+Integer.toString(info.getId()));
            /*
            防止重复下载
             */
            if(!DbOperator.isOneExists(info)){
                DbOperator.insertThread(info);
            }
            //启动线程池管理线程
            DownloadTask.mExecutorService.execute(thread);
            mThreadlist.add(thread);

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
        DbOperator.updateFileInfo(mFileInfo.getUrl(),100);
        DbOperator.deleteThread(mFileInfo.getUrl());

        Intent stop_notify=new Intent(mContext,UpdateReceiver.class);
        stop_notify.putExtra("Action",DownloadService.ACTION_FINISHED_NOTIFY);
        stop_notify.putExtra("fileInfo", mFileInfo);
        mContext.sendBroadcast(stop_notify);

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
                conn.setRequestProperty("Range","bytes="+start+"-"+threadInfo.getEnd());
                Log.d(TAG, "run: start at"+start+",end at"+threadInfo.getEnd());
                File file=new File(DownloadService.DownloadPath, mFileInfo.getFileName());
                raf=new RandomAccessFile(file,"rwd");
                raf.seek(start);

                mFinished+=threadInfo.getFinished();
                Log.d(TAG, "run: get Start Length: "+mFinished);
                lastFinshed=mFinished;

                int code=conn.getResponseCode();
                Log.d(TAG,Integer.toString(code));
                if(code==206){
                    Log.d(TAG, "run: Join in the loop");
                    is=conn.getInputStream();
                    byte[] bt=new byte[1024];
                    int len=-1;
                    //定义UI刷新时间
                    lastloopTime=System.currentTimeMillis();

                    while ((len=is.read(bt))!=-1){
                        raf.write(bt,0,len);
                        //累积进度
                       /* mFinished+=len;*/
                        threadInfo.setFinished(threadInfo.getFinished()+len);

                        mFinished =0;
                        for (ThreadInfo info:list
                             ) {
                            mFinished+=info.getFinished();
                        }
                        //不频繁地更新UI
                        if(System.currentTimeMillis()-lastloopTime>1000){
                            Log.d(TAG, "run: Downloading");
                            looptimes++;

                            Intent intent=new Intent();
                            intent.setAction(DownloadService.ACTION_UPDATE);

                            long finished=(mFinished)*100/(mFileInfo.getLength());
                            intent.putExtra("finished",finished);//完成度
                            intent.putExtra("length",mFileInfo.getLength());
                            intent.putExtra("fileinfo_id",mFileInfo.getId());
                            if(looptimes>=3){
                                costTime=System.currentTimeMillis()-lastloopTime;
                                speed=(double)((mFinished-lastFinshed)/costTime);
                                lastloopTime=System.currentTimeMillis();
                                lastFinshed=mFinished;
                                looptimes=0;
                            }
                            intent.putExtra("speed",speed);
                            DbOperator.updateFileInfo(mFileInfo.getUrl(),(int)finished);

                            Intent update_notify=new Intent(mContext,UpdateReceiver.class);
                            update_notify.putExtra("Action",DownloadService.ACTION_UPDATE_NOTIFY);
                            update_notify.putExtra("finished",finished);//完成度
                            update_notify.putExtra("length",mFileInfo.getLength());
                            update_notify.putExtra("fileinfo_id",mFileInfo.getId());
                            update_notify.putExtra("speed",speed);


                            mContext.sendBroadcast(intent);
                            mContext.sendBroadcast(update_notify);
                            Log.d(TAG, "run: Update Send");

                        }
                        if (mIsDelete){
                            Log.d("DownloadTask", "run: Delete");
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
                            Log.d(TAG, "run: Pause");
                            DbOperator.updateThread(threadInfo.getUrl(), threadInfo.getId(), threadInfo.getFinished());
                            return;
                        }
                    }
                }else {
                    //url无效的情况下

                    Toast.makeText(mContext, "This URL is invali", Toast.LENGTH_SHORT).show();
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
