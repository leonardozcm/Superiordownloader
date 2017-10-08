package com.example.com.superiordownloader.Util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.util.Log;

import java.io.File;

import static android.content.ContentValues.TAG;

/**
 * 用于打开不同类型的文件
 * Created by 59771 on 2017/10/5.
 */

public class OpenFileUtil {
    private Context mContext;
    private Uri uri=null;
    public OpenFileUtil(Context context){
        mContext=context;
    }

    public Intent openFile(String mfilePath) {
        File file = new File(mfilePath);
        Log.d(TAG, "openFile: "+mfilePath);
        if (!file.exists()) return null;
       // String filePath=mfilePath;
      /*  if(Build.VERSION.SDK_INT>=24){
            uri= FileProvider.getUriForFile(mContext,"com.example.com.superiordownloader.fileprovider",file);
            filePath=uri.toString();
        }*/

        String end = file.getName().substring(file.getName().lastIndexOf(".")+ 1);//拓展名

        if (end.equals("m4a") || end.equals("mp3") || end.equals("mid") || end.equals("xmf") || end.equals("ogg") || end.equals("wav")) {
            return getAudioFileIntent(file);
        } else if (end.equals("3gp") || end.equals("mp4")) {
            return getVideoFileIntent(file);
        } else if (end.equals("jpg") || end.equals("gif") || end.equals("png") ||
                end.equals("jpeg") || end.equals("bmp")) {
            return getImageFileIntent(file);
        } else if (end.equals("apk")) {
            return getApkFileIntent(file);
        } else if (end.equals("ppt")) {
            return getPPTFileIntent(file);
        } else if (end.equals("xls")) {
            return getExcelFileIntent(file);
        } else if (end.equals("doc")) {
            return getWordFileIntent(file);
        } else if (end.equals("pdf")) {
            return getPDFFileIntent(file);
        } else if (end.equals("chm")) {
            return getChmFileIntent(file);
        } else if (end.equals("txt")) {
            return getTextFileIntent(file);
        } else {
            return getAllIntent(file);
        }
    }

    public Intent getAllIntent(File file) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        if(Build.VERSION.SDK_INT>=24) {
            uri = FileProvider.getUriForFile(mContext, "com.example.com.superiordownloader.fileprovider", file);
        }else {
            uri = Uri.fromFile(file);
        }
        intent.setDataAndType(uri, "*/*");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        return intent;
    }

    public  Intent getApkFileIntent(File file) {
        Intent intent = new Intent();

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        if(Build.VERSION.SDK_INT>=24) {
            uri = FileProvider.getUriForFile(mContext, "com.example.com.superiordownloader.fileprovider", file);
        }else {
            uri = Uri.fromFile(file);
        }
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        return intent;
    }

    public Intent getVideoFileIntent(File file) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        if(Build.VERSION.SDK_INT>=24) {
            uri = FileProvider.getUriForFile(mContext, "com.example.com.superiordownloader.fileprovider", file);
        }else {
            uri = Uri.fromFile(file);
        }
        intent.putExtra("oneshot", 0);
        intent.putExtra("configchange", 0);
        intent.setDataAndType(uri, "video/*");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        return intent;
    }

    public Intent getAudioFileIntent(File file) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        if(Build.VERSION.SDK_INT>=24) {
            uri = FileProvider.getUriForFile(mContext, "com.example.com.superiordownloader.fileprovider", file);
        }else {
            uri = Uri.fromFile(file);
        }
        intent.putExtra("oneshot", 0);
        intent.putExtra("configchange", 0);
        intent.setDataAndType(uri, "audio/*");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        return intent;
    }

   /* public Intent getHtmlFileIntent(String param) {
        Uri uri = Uri.parse(param).buildUpon().encodedAuthority("com.android.htmlfileprovider").scheme("content").encodedPath(param).build();
        if(Build.VERSION.SDK_INT>=24) {
            uri = FileProvider.getUriForFile(mContext, "com.example.com.superiordownloader.fileprovider", new File(uri.getPath()));
        }else {
            uri = Uri.fromFile(new File(param));
        }
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setDataAndType(uri, "text/html");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        return intent;
    }*/

    public Intent getImageFileIntent(File file) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if(Build.VERSION.SDK_INT>=24) {
            uri = FileProvider.getUriForFile(mContext, "com.example.com.superiordownloader.fileprovider", file);
        }else {
            uri = Uri.fromFile(file);
        }
        intent.setDataAndType(uri, "image/*");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        return intent;
    }

    public Intent getPPTFileIntent(File file) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if(Build.VERSION.SDK_INT>=24) {
            uri = FileProvider.getUriForFile(mContext, "com.example.com.superiordownloader.fileprovider", file);
        }else {
            uri = Uri.fromFile(file);
        }
        intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        return intent;
    }

    public Intent getExcelFileIntent(File file) {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if(Build.VERSION.SDK_INT>=24) {
            uri = FileProvider.getUriForFile(mContext, "com.example.com.superiordownloader.fileprovider", file);
        }else {
            uri = Uri.fromFile(file);
        }
        intent.setDataAndType(uri, "application/vnd.ms-excel");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        return intent;
    }

    public Intent getWordFileIntent(File file) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if(Build.VERSION.SDK_INT>=24) {
            uri = FileProvider.getUriForFile(mContext, "com.example.com.superiordownloader.fileprovider", file);
        }else {
            uri = Uri.fromFile(file);
        }
        intent.setDataAndType(uri, "application/msword");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        return intent;
    }

    public Intent getChmFileIntent(File file) {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if(Build.VERSION.SDK_INT>=24) {
            uri = FileProvider.getUriForFile(mContext, "com.example.com.superiordownloader.fileprovider", file);
        }else {
            uri = Uri.fromFile(file);
        }
        intent.setDataAndType(uri, "application/x-chm");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        return intent;
    }

    public Intent getTextFileIntent(File file) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if(Build.VERSION.SDK_INT>=24) {
                uri = FileProvider.getUriForFile(mContext, "com.example.com.superiordownloader.fileprovider",file);
            }else {
                uri = Uri.fromFile(file);
            }
            intent.setDataAndType(uri, "text/plain");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        return intent;
    }

    public Intent getPDFFileIntent(File file) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if(Build.VERSION.SDK_INT>=24) {
            uri = FileProvider.getUriForFile(mContext, "com.example.com.superiordownloader.fileprovider", file);
        }else {
            uri = Uri.fromFile(file);
        }
        intent.setDataAndType(uri, "application/pdf");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        return intent;
    }
}
