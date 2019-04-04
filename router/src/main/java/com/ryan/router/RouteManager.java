package com.ryan.router;

import android.content.Context;
import android.os.Looper;
import android.os.RemoteException;
import android.util.Log;

import com.ryan.router.remote.IWideRouter;

/**
 * @author Ryan
 * @Date 17/5/10 下午3:49
 */
public final class RouteManager {

    private RouteConnector mConnector;
    private LocalRouteFinder mLocalFinder;
    private static final String TAG = Router.class.getName();
    private static RouteCache CACHE = new DefaultRouteCache();

    private RouteManager() {
        mConnector = new RouteConnector();
        mLocalFinder = new LocalRouteFinder();
    }

    private static class SingletonHolder {
        private static final RouteManager INSTANCE = new RouteManager();
    }

    public static RouteManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    @SuppressWarnings("unchecked")
    public <V> RouteResponse<V> request(Context context, RouteRequest request) {
        if (context == null || request == null) {
            throw new IllegalArgumentException();
        }
        checkThread();
        RouteResponse response = CACHE.getCache(request);
        if (response != null) {
            Log.v(TAG, "find the specified request cache, return");
            return response;
        }
        response = mLocalFinder.findLocal(context, request);
        if (response != null) {
            return response;
        }
        if (request.isInJustRouteLocal()) {
            return null;
        }
        return wideRequest(context, request);
    }

    private RouteResponse wideRequest(final Context context, final RouteRequest request) {
        if (mConnector.isConnectionAlive()) {
            return wideRequest(request);
        }
        if (mConnector.connect(context)) {
            try {
                return mConnector.getRouteConnection().wideRoute(request);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return RouteResponse.error("start service async.");
    }


    private RouteResponse wideRequest(RouteRequest request) {
        try {
            IWideRouter router = mConnector.getRouteConnection();
            return router.wideRoute(request);
        } catch (RemoteException e) {
            e.printStackTrace();
            return new RouteResponse.Builder<>()
                    .error(e.getMessage())
                    .code(RouteResponse.CODE_ERROR)
                    .result(null)
                    .build();
        }
    }

    public void registerProvider(RouteProvider provider) {
        mLocalFinder.registerProvider(provider);
    }

    private void checkThread() {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            throw new IllegalThreadStateException();
        }
    }
}
