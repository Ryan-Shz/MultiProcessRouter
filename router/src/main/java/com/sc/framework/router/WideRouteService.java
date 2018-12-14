package com.sc.framework.router;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.sc.framework.router.remote.WideRouterNative;

/**
 * @author shamschu
 * @Date 17/7/14 上午11:34
 */
public class WideRouteService extends Service {

    public static void start(Context context) {
        Intent intent = new Intent(context, WideRouteService.class);
        context.startService(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new WideRouterNative(this);
    }
}
