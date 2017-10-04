package com.example.com.superiordownloader.ServiceHolder;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by 59771 on 2017/10/4.
 */

public class BaseService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ServiceCollecetor.addService(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ServiceCollecetor.removeService();
    }
}
