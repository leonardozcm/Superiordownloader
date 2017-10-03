package com.example.com.superiordownloader.Service;

import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.com.superiordownloader.Information.FileInfo;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by 59771 on 2017/10/1.
 */

public class DownloadService extends Service {
    private static final String TAG="DownloadService";

    public static final String ACTION_START = "ACTION_START";
    public static final String ACTION_STOP = "ACTION_STOP";
    public static final String ACTION_DELETE = "ACTION_DELETE";
    public static final String ACTION_UPDATE = "ACTION_UPDATE";
    public static final String ACTION_FINISHED = "ACTION_FINISHED";
    //文件保存路径
    public static final String DownloadPath= Environment.getExternalStorageDirectory().getAbsolutePath()
            +"/download/";
    public static final int MSG_INIT=0;

    private Map<Integer,DownloadTask> mTasks=new LinkedHashMap<Integer, DownloadTask>();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent,int flags, int startId) {
      if(ACTION_START.equals(intent.getAction())){
          FileInfo fileInfo=(FileInfo)intent.getSerializableExtra("fileInfo");
          Log.d(TAG, "onStartCommand: add Intent received");
          Log.i(TAG, "START" + fileInfo.toString());
         InitThread initThread=new InitThread(fileInfo);
          Log.d(TAG, "onStartCommand: InitTread Build success");
          DownloadTask.mExecutorService.execute(initThread);
          Log.d(TAG, "onStartCommand: mExecutorService start success");
      }else if(ACTION_STOP.equals(intent.getAction())) {
          FileInfo fileInfo=(FileInfo)intent.getSerializableExtra("fileInfo");
          DownloadTask task=mTasks.get(fileInfo.getId());
          if (task!=null) {
              //停止下载任务
              task.mIsPause = true;
          }
      }else if(ACTION_DELETE.equals(intent.getAction())){
          FileInfo fileInfo=(FileInfo)intent.getSerializableExtra("fileInfo");
          DownloadTask task=mTasks.get(fileInfo.getId());
          if (task!=null) {
              //停止下载任务
              task.mIsDelete= true;
          }
      }
      return  super.onStartCommand(intent, flags, startId);
    }


//获取FileInfo，开启下载项目
    Handler mHandler=new Handler(){
    public void handleMessage(Message msg){
        switch (msg.what){
            case MSG_INIT:
                Log.d(TAG, "handleMessage: recevied from InitTread");
                FileInfo fileInfo=(FileInfo)msg.obj;
                Log.i(TAG, "INIT:"+fileInfo.toString());
                //开始下载任务
                DownloadTask task=new DownloadTask(DownloadService.this,fileInfo,3);
                task.download();

                mTasks.put(fileInfo.getId(),task);
                //发送通知
              /*  Intent intent=new Intent(ACTION_START);
                intent.putExtra("fileInfo",fileInfo);
                sendBroadcast(intent);*/
                break;
            default:break;
        }
    }
};
    //初始化下载线程，导入数据
    class InitThread extends Thread{
        private FileInfo mFileInfo=null;

        public InitThread(FileInfo mFileInfo){
            super();
            this.mFileInfo=mFileInfo;
        }

        @Override
        public void run() {
            HttpURLConnection conn=null;
            RandomAccessFile raf=null;
            try {
                URL url=new URL(mFileInfo.getUrl());
                conn=(HttpURLConnection)url.openConnection();
                conn.setConnectTimeout(5000);
                conn.setRequestMethod("GET");
                int code=conn.getResponseCode();//获取链接状态
                int length=-1;
                if(code==HttpURLConnection.HTTP_OK){
                    length=conn.getContentLength();
                }
                //获取length<=0,获取失败
                if(length<=0){
                    Log.d(TAG, "run: length get fail");
                    return;
                }
                Log.d(TAG, "run: length get success");
                //判断path有效性，没有就创建
                File dir=new File(DownloadPath);
                if(!dir.exists()){
                    Log.d(TAG, "run: dir not exist");
                    dir.mkdir();
                }
                //创建文件
                File file=new File(dir,mFileInfo.getFileName());
                raf=new RandomAccessFile(file,"rw");
                raf.setLength(length);
                Log.d(TAG, "run: file build success");
                //设置文件长度
                mFileInfo.setLength(length);
                mFileInfo.updateAll("url = ?",mFileInfo.getUrl());
                //handler发送Fileinfo对象
                Message msg=Message.obtain();
                msg.obj=mFileInfo;
                msg.what=MSG_INIT;
                mHandler.sendMessage(msg);
                msg.setTarget(mHandler);
                Log.d(TAG, "run: msg send");
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                if (conn != null) {
                    conn.disconnect();
                }
                try {
                    if (raf != null) {
                        raf.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            super.run();
            }
        }

    }

