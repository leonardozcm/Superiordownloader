package com.example.com.superiordownloader.Util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.com.superiordownloader.Information.FileInfo;
import com.example.com.superiordownloader.MainActivity;
import com.example.com.superiordownloader.R;

import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

/**
 * Created by 59771 on 2017/10/3.
 */

public class NotificationUtil {

    private Context mContext;
   private NotificationManager mNotificationManager = null;
   static private Map<Integer, Notification> mNotifications = new HashMap<Integer, Notification>();

    public NotificationUtil(Context context){
        Log.d("Notify", "NotificationUtil: Started");
        this.mContext=context;
if(mNotificationManager==null){
    mNotificationManager=(NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
}
    }

    public void show(FileInfo fileInfo){
        if(!mNotifications.containsKey(fileInfo.getId())){


            Intent intent=new Intent(mContext, MainActivity.class);
            PendingIntent pi=PendingIntent.getActivity(mContext,0,intent,0);


            RemoteViews remoteViews=new RemoteViews(mContext.getPackageName(),R.layout.notification_item);
            remoteViews.setTextViewText(R.id.notify_name,fileInfo.getFileName());
            remoteViews.setTextViewText(R.id.notify_name, fileInfo.getFileName());

            Notification notification=new NotificationCompat.Builder(mContext)
                    .setTicker(fileInfo.getFileName()+"开始下载")
                    .setContentTitle(fileInfo.getFileName()+"开始下载")
                    .setContent(remoteViews)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.drawable.ic_download)
                    .setContentIntent(pi)
                    .setAutoCancel(true)
                    .build();
            notification.flags=Notification.FLAG_AUTO_CANCEL;

            mNotificationManager.notify(
                    fileInfo.getId(),
                    notification);
            Log.d(TAG, "show: id = "+fileInfo.getId());

            mNotifications.put(fileInfo.getId(), notification);
            Log.d(TAG, "show: Contains id ? "+(mNotifications.containsKey(fileInfo.getId())));

        }
    }

    public void cancelNotification(int id){
        Log.d(TAG, "cancelNotification: "+id);
        mNotificationManager.cancel(id);
        mNotifications.remove(id);
    }
    public void updateNotification(int id,int progress,double speed,int length){
        Notification notification=mNotifications.get(id);
        Log.d(TAG, "updateNotification: Update "+id+" progress: "+ progress);
        Log.d(TAG, "updateNotification:  Contains id ? "+(mNotifications.containsKey(id)));
        if(notification!=null){
            notification.contentView.setProgressBar(R.id.notify_progreess,100,progress,false);
            notification.contentView.setTextViewText(R.id.notify_downloadsize,DataTransFormer.ToString(((progress/100.0)*length)/(1024.0*1024.0))+"MB/"+DataTransFormer.ToString(length/(1024.0*1024.0))+"MB");
            notification.contentView.setTextViewText(R.id.notify_downloadspeed,speed+"kB/s");
            mNotificationManager.notify(id,notification);

        }

    }
}

