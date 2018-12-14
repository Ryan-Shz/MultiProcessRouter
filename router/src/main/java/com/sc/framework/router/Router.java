package com.sc.framework.router;

import android.content.Context;
import android.os.Process;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * @author shamschu
 * @Date 17/8/26 下午6:45
 */
public class Router {

    private static final String TARGET_CLASS = "com.sc.framework.router.RouteRegister";
    private static final String TAG = Router.class.getName();
    private static RouteCache CACHE = new DefaultRouteCache();
    private static boolean sInitialized = false;

    private Router() {
        throw new RuntimeException("Can't call the constructor of the Router");
    }

    @SuppressWarnings("unchecked")
    public static <V> RouteResponse<V> route(@NonNull Context context, @NonNull RouteRequest request) {
        checkInitialized();
        if (!isRequestValid(request)) {
            throw new IllegalArgumentException();
        }
        RouteResponse<V> response = getMemoryCache(request);
        if (response != null) {
            Log.v(TAG, "find the specified request cache, return");
            return response;
        }
        return RouteManager.getInstance().request(context, request);
    }

    public static synchronized void init(Context context, Map<String, Class<? extends LocalRouteService>> services) {
        try {
            if (ProcessUtils.runningInRouterProcess(context)) {
                WideRouteManager.getInstance().registerServices(context, services);
            } else {
                registerProviders(context);
                WideRouteService.start(context);
            }
            sInitialized = true;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }

    private static synchronized void registerProviders(Context context) throws
            ClassNotFoundException, IllegalAccessException, InstantiationException {
        String process = ProcessUtils.getCurrentProcessName(context);
        Class<?> targetClass = Class.forName(TARGET_CLASS);
        IRouterRegister register = (IRouterRegister) targetClass.newInstance();
        register.register(process);
    }

    public synchronized static void killCurrProcess() {
        Process.killProcess(Process.myPid());
        System.exit(0);
    }

    private static boolean isRequestValid(RouteRequest request) {
        if (request.getProcess() == null) {
            throw new IllegalArgumentException();
        }
        if (request.getProvider() == null) {
            throw new IllegalArgumentException();
        }
        if (request.getAction() == null) {
            throw new IllegalArgumentException();
        }
        return true;
    }

    @SuppressWarnings("unchecked")
    private static <T> RouteResponse<T> getMemoryCache(RouteRequest request) {
        return CACHE.getCache(request);
    }

    private static void checkInitialized() {
        if (!sInitialized) {
            throw new IllegalStateException("router not initialized!");
        }
    }
}
