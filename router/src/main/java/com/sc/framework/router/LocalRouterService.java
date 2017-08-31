package com.sc.framework.router;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

/**
 * @author ShamsChu
 * @Date 17/7/14 下午3:49
 */
public class LocalRouterService extends Service {

    private IWideRouterAIDL.Stub stub = new IWideRouterAIDL.Stub() {
        @Override
        public RouterResponse route(RouterRequest routerRequest) throws RemoteException {
            return RouterManager.getInstance().request(LocalRouterService.this, routerRequest);
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return stub;
    }
}
