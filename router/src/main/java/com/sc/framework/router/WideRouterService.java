package com.sc.framework.router;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;

/**
 * @author ShamsChu
 * @Date 17/7/14 上午11:34
 */
public class WideRouterService extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_NOT_STICKY;
    }

    private IWideRouterAIDL.Stub stub = new IWideRouterAIDL.Stub() {
        @Override
        public RouterResponse route(RouterRequest routerRequest) throws RemoteException {
            RouterResponse response;
            try {
                response = WideRouterManager.getInstance().route(WideRouterService.this, routerRequest);
            } catch (Exception e) {
                e.printStackTrace();
                response = new RouterResponse.Builder<>()
                    .error(e.getMessage())
                    .code(RouterResponse.CODE_ERROR)
                    .build();
            }
            return response;
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return stub;
    }
}
