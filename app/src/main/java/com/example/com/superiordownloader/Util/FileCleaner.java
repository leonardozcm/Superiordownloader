package com.example.com.superiordownloader.Util;

import android.util.Log;

import java.io.File;

import static android.content.ContentValues.TAG;

/**
 * 删除文件的工具
 * Created by 59771 on 2017/10/2.
 */

public class FileCleaner {
     static public boolean deleteDir(File dir) {
         if (dir.exists()) {
             File[] files = dir.listFiles();
             if (files == null) {
                 Log.d(TAG, "deleteDir: "+Boolean.toString(true));
                 return true;
             }
             for (int i = 0; i < files.length; i++) {
                 if (files[i].isDirectory()) {
                     deleteDir(files[i]);
                 } else {
                     files[i].delete();
                 }
             }
         }
        return dir.delete();
/*
        if (dir.isDirectory()) {
            String[] children = dir.list();
            Log.d(TAG, "deleteDir: "+Boolean.toString(dir.exists()));
            Log.d(TAG, "deleteDir: "+Boolean.toString(children==null));
            if(!(children==null)){
                for (int i=0; i<children.length; i++) {
                    boolean success = deleteDir(new File(dir, children[i]));
                    if (!success) {
                        return false;
                    }
                }
            }else {
                Log.d("DATABASE", "There is no file.");
            }

        }
         File[] files = dir.listFiles();
         if(files !=null&&files.length > 0){
             Log.d(TAG, "deleteDir: NOT NULL");
         } else{
             Log.d(TAG, "deleteDir: NULL");
         }

        return true;*/
    }
}
