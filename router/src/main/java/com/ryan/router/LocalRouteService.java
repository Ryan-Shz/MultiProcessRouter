package com.ryan.router;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.ryan.router.remote.LocalRouterNative;

/**
 * @author Ryan
 * create by 17/7/14 下午3:49
 */
public class LocalRouteService extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new LocalRouterNative(this);
    }
}
