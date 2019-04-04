package com.ryan.router;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;

import com.ryan.router.remote.ILocalRouter;
import com.ryan.router.remote.LocalRouterNative;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 多进程路由管理器
 *
 * @author Ryan
 * @Date 17/7/14 上午10:56
 */
public class WideRouteManager {

    private Map<String, Class<? extends LocalRouteService>> mRegisterServicesMap;
    private BinderPool mBinderPool;

    private Lock mWaitLock;
    private Condition mCondition;

    private WideRouteManager() {
        mWaitLock = new ReentrantLock();
        mCondition = mWaitLock.newCondition();
        mBinderPool = new BinderPool();
    }

    public static WideRouteManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        private static final WideRouteManager INSTANCE = new WideRouteManager();
    }

    // 注册模块相关AIDL服务
    void registerServices(Context context, Map<String, Class<? extends LocalRouteService>> services) {
        checkCaller(context);
        mRegisterServicesMap = Collections.unmodifiableMap(services);
    }

    public RouteResponse route(Context context, RouteRequest request) throws RouteException, RemoteException {
        String process = request.getProcess();
        ILocalRouter remoteLocalRouter = findRemoteBinder(request);
        if (remoteLocalRouter != null) {
            return remoteLocalRouter.localRoute(request);
        }
        if (connectService(context, process)) {
            remoteLocalRouter = findRemoteBinder(request);
            if (remoteLocalRouter != null) {
                return remoteLocalRouter.localRoute(request);
            }
        }
        return RouteResponse.error("unable connect to process: " + process);
    }

    private ILocalRouter findRemoteBinder(RouteRequest request) {
        return mBinderPool.findRemoteBinder(request);
    }

    private boolean connectService(Context context, String process) {
        checkCaller(context);
        if (!mRegisterServicesMap.containsKey(process)) {
            throw new IllegalArgumentException();
        }
        Class<? extends LocalRouteService> serviceClass = mRegisterServicesMap.get(process);
        if (serviceClass == null) {
            throw new IllegalArgumentException();
        }
        Context appContext = context.getApplicationContext();
        Intent intent = new Intent(appContext, serviceClass);
        appContext.bindService(intent, new RouterServiceConnection(process), Context.BIND_AUTO_CREATE);
        try {
            mWaitLock.lock();
            mCondition.await(RouteConnector.CONNECT_TIME_OUT, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            mWaitLock.unlock();
        }
        return mBinderPool.findRemoteBinder(process) != null;
    }

    private void checkCaller(Context context) {
        if (!ProcessUtils.runningInRouterProcess(context)) {
            throw new IllegalStateException();
        }
    }

    private class RouterServiceConnection implements ServiceConnection {

        private String process;

        RouterServiceConnection(String processName) {
            process = processName;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            boolean success = false;
            try {
                if (registerDeath(service, process)) {
                    success = true;
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            if (success) {
                mBinderPool.put(process, service);
            }
            signalAll();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBinderPool.remove(process);
        }
    }

    private boolean registerDeath(IBinder binder, final String process) throws RemoteException {
        if (binder.isBinderAlive()) {
            binder.linkToDeath(new IBinder.DeathRecipient() {
                @Override
                public void binderDied() {
                    mBinderPool.remove(process);
                }
            }, 0);
            return true;
        }
        return false;
    }

    private void signalAll() {
        try {
            mWaitLock.lock();
            mCondition.signalAll();
        } finally {
            mWaitLock.unlock();
        }
    }

    public static class BinderPool {
        private final Map<String, ILocalRouter> mWideRouterAIDLMaps;

        BinderPool() {
            mWideRouterAIDLMaps = new ConcurrentHashMap<>();
        }

        void put(String process, IBinder binder) {
            ILocalRouter iInterface = LocalRouterNative.asInterface(binder);
            mWideRouterAIDLMaps.put(process, iInterface);
        }

        void remove(String process) {
            mWideRouterAIDLMaps.remove(process);
        }

        private ILocalRouter findRemoteBinder(String process) {
            return mWideRouterAIDLMaps.get(process);
        }

        private ILocalRouter findRemoteBinder(RouteRequest request) {
            if (request == null) {
                return null;
            }
            String process = request.getProcess();
            return findRemoteBinder(process);
        }
    }

}
