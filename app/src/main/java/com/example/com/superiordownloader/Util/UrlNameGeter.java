package com.example.com.superiordownloader.Util;


/**
 * 优雅地获得文件名
 * Created by 59771 on 2017/10/1.
 */

public class UrlNameGeter {
    static public String get(String url){
        return url.substring(url.lastIndexOf("/") + 1);
    }
}
