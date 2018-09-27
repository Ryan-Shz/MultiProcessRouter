package com.sc.framework.router;

import android.content.Context;
import android.os.Process;
import android.support.annotation.NonNull;
import android.util.Log;

import com.sc.framework.router.cache.DefaultRouterCache;
import com.sc.framework.router.cache.MemoryCacheStrategy;
import com.sc.framework.router.cache.RouterCache;
import com.sc.framework.router.constants.RouterConstants;
import com.sc.framework.router.utils.ProcessUtils;

/**
 * @author ShamsChu
 * @Date 17/8/26 下午6:45
 */
public class Router {

    private static final String TAG = Router.class.getName();
    private static RouterCache CACHE = new DefaultRouterCache();

    private Router() {
        throw new RuntimeException("Can't call the constructor of the Router");
    }

    public static <V> RouterResponse<V> route(@NonNull Context context, @NonNull RouterRequest request) {
        if (!isRequestValid(request)) {
            return new RouterResponse.Builder<V>()
                    .code(RouterResponse.CODE_ERROR)
                    .error("RouterRequest is not valid!")
                    .build();
        }
        RouterResponse<V> response = getMemoryCache(request);
        if (response != null) {
            Log.v(TAG, "find the specified request cache, return");
            return response;
        }
        RouterResponse<V> routerResponse = RouterManager.getInstance().request(context, request);
        CACHE.putCache(request, routerResponse);
        return routerResponse;
    }

    public synchronized static void register(@NonNull Context context, @NonNull IRouterServiceRegister serviceRegister) {
        RouterManager.getInstance().initialize(context, serviceRegister);
    }

    public synchronized static void killCurrProcess(Context context) {
        unRegister(context);
        Process.killProcess(Process.myPid());
        System.exit(0);
    }

    public synchronized static void unRegister(@NonNull Context context) {
        RouterManager.getInstance().unbindWideRouterService(context);
    }

    private static boolean isRequestValid(RouterRequest request) {
        if (request.getProcess() == null) {
            Log.e(TAG, "RouterRequest process can not be null! don't do anything");
            return false;
        }
        if (request.getProvider() == null) {
            Log.e(TAG, "RouterRequest provider can not be null! don't do anything");
            return false;
        }
        if (request.getAction() == null) {
            Log.e(TAG, "RouterRequest action can not be null! don't do anything");
            return false;
        }
        return true;
    }

    /**
     * check if router service is initialized
     *
     * @param context Context
     * @return if router service is initialized return true or false
     */
    public static boolean checkRouterServiceInitCompleted(Context context) {
        RouterRequest request = new RouterRequest.Builder()
                .process(ProcessUtils.getRouterProcess(context))
                .provider(RouterConstants.PROVIDER_ROUTER_INTERNAL)
                .action(RouterConstants.ACTION_SERVICE_CONNECT_STATUS_CHECK)
                .cacheStrategy(MemoryCacheStrategy.FIXED)
                .build();
        RouterResponse<Boolean> response = RouterManager.getInstance().request(context, request);
        return response != null && response.isSuccess() && response.getResult() != null && response.getResult();
    }

    /**
     * get Router service memory cache
     *
     * @return RouterCache
     */
    public static RouterCache getRouterCache() {
        return CACHE;
    }

    /**
     * Custom Router Cache
     *
     * @param routerCache your custom router cache
     */
    public synchronized static void setRouterCache(RouterCache routerCache) {
        CACHE = routerCache;
    }

    @SuppressWarnings("unchecked")
    private static <T> RouterResponse<T> getMemoryCache(RouterRequest request) {
        return CACHE.getCache(request);
    }

}
