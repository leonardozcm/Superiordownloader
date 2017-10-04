package com.example.com.superiordownloader;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.com.superiordownloader.Information.FileInfo;
import com.example.com.superiordownloader.Util.NotificationUtil;

import static android.content.ContentValues.TAG;
import static com.example.com.superiordownloader.Service.DownloadService.ACTION_DELETE_NOTIFY;
import static com.example.com.superiordownloader.Service.DownloadService.ACTION_FINISHED_NOTIFY;
import static com.example.com.superiordownloader.Service.DownloadService.ACTION_START_NOTIFY;
import static com.example.com.superiordownloader.Service.DownloadService.ACTION_UPDATE;
import static com.example.com.superiordownloader.Service.DownloadService.ACTION_UPDATE_NOTIFY;

public class UpdateReceiver extends BroadcastReceiver {
NotificationUtil notificationUtil;
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive: UpdateReceiver");
        notificationUtil=new NotificationUtil(context);

        if(ACTION_START_NOTIFY.equals(intent.getStringExtra("Action"))){
            FileInfo fileInfo=(FileInfo)intent.getSerializableExtra("fileInfo");
            notificationUtil.show(fileInfo);
        }else if(ACTION_UPDATE_NOTIFY.equals(intent.getStringExtra("Action"))){
            Log.d(TAG, "UpdateReceiver: receive "+ACTION_UPDATE);
            int finished = (int)intent.getLongExtra("finished", 0);
            double speed = intent.getDoubleExtra("speed", 0.0);
            int fileinfo_id = intent.getIntExtra("fileinfo_id", 0);
            int length=intent.getIntExtra("length",0);
            notificationUtil.updateNotification(fileinfo_id,finished,speed,length);
        }else if (ACTION_FINISHED_NOTIFY.equals(intent.getStringExtra("Action"))) {
            // 下载结束的时候
            FileInfo fileInfo = (FileInfo) intent.getSerializableExtra("fileInfo");
            int fileinfo_id = fileInfo.getId();
            Log.d("Service", "onReceive: "+fileinfo_id);
            int length= fileInfo.getLength();
            notificationUtil.updateNotification(fileinfo_id, 100, 0,length);
        }else if(ACTION_DELETE_NOTIFY.equals(intent.getStringExtra("Action"))){
            //删除任务同时删除通知
            FileInfo fileInfo=(FileInfo)intent.getSerializableExtra("fileInfo");
            notificationUtil.cancelNotification(fileInfo.getId());

        }
    }

    }

