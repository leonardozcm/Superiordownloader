package com.example.com.superiordownloader.Util;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * 用于检测URL的合法性
 * Created by 59771 on 2017/10/7.
 */

public class UrlChecker {

    public static boolean isValidUrl(String urlstr){
        URI uri=null;
        try {
            uri =new URI(urlstr);
        }catch (URISyntaxException e){
            e.printStackTrace();
            return false;
        }

        if(uri.getHost()==null){
            return false;
        }

        if (uri.getScheme().equalsIgnoreCase("http")||uri.getScheme().equalsIgnoreCase("https")){
            return true;
        }

        return false;
    }
}
