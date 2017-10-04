package com.example.com.superiordownloader.ServiceHolder;

import android.app.Service;

/**
 * Created by 59771 on 2017/10/4.
 */

public class ServiceCollecetor {
    public static Service mService=null;

    public static void addService(Service service){
        mService=service;
    }

    public static void removeService(){
        mService=null;
    }

    public static Service getService(){
        return mService;
    }
}
