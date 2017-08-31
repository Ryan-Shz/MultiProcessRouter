package com.sc.framework.router;

import android.content.Context;
import android.support.annotation.NonNull;

import com.sc.framework.router.cache.RouterCache;

/**
 * @author ShamsChu
 * @Date 17/8/26 下午6:45
 */
public class Router {

    private static final RouterCache CACHE = new RouterCache();

    private Router() {
        throw new RuntimeException("Can't call the constructor of the Router");
    }

    public static <V> RouterResponse<V> route(@NonNull Context context, @NonNull RouterRequest request) {
        RouterResponse<V> response = getMemoryCache(request);
        if (response != null) {
            return response;
        }
        RouterResponse<V> routerResponse = RouterManager.getInstance().request(context, request);
        CACHE.putCache(request, routerResponse);
        return routerResponse;
    }

    public synchronized static void register(@NonNull Context context, @NonNull IRouterServiceRegister serviceRegister) {
        RouterManager.getInstance().initialize(context, serviceRegister);
    }

    public synchronized static void unRegister(@NonNull Context context) {
        RouterManager.getInstance().unbindWideRouterService(context.getApplicationContext());
    }

    private static <T> RouterResponse<T> getMemoryCache(RouterRequest request) {
        return CACHE.getCache(request);
    }

}
