package com.example.com.superiordownloader.Service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class ListenerService extends Service {
    public ListenerService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }
}
