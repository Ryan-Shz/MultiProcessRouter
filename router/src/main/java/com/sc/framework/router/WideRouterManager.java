package com.sc.framework.router;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.sc.framework.router.utils.ProcessUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 多进程路由管理器
 *
 * @author ShamsChu
 * @Date 17/7/14 上午10:56
 */
class WideRouterManager {

    private static final String TAG = WideRouterManager.class.getName();
    private final Map<String, Class<? extends LocalRouterService>> mRegisterServicesMap;
    private final Map<String, IWideRouterAIDL> mWideRouterAIDLMaps;
    private boolean mInitCompleted = false;

    private WideRouterManager() {
        mRegisterServicesMap = new HashMap<>();
        mWideRouterAIDLMaps = new HashMap<>();
    }

    static WideRouterManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        private static final WideRouterManager INSTANCE = new WideRouterManager();
    }

    // 注册模块相关AIDL服务
    synchronized void registerServices(Context context, Map<String, Class<? extends LocalRouterService>> services) {
        mRegisterServicesMap.putAll(services);
        for (String key : services.keySet()) {
            Class<? extends LocalRouterService> service = services.get(key);
            registerService(context, key, service);
        }
    }

    // 注册模块相关AIDL服务
    synchronized void registerService(Context context, String process, Class<? extends LocalRouterService> targetClass) {
        mRegisterServicesMap.put(process, targetClass);
        initializeService(context, process);
    }

    private void initializeService(Context context, String process) {
        Log.v(TAG, "router service connecting to process: " + process);
        bindService(context, process);
    }

    RouterResponse route(Context context, RouterRequest request) throws RouterException, RemoteException {
        String process = request.getProcess();
        IWideRouterAIDL wideRouterAIDL = mWideRouterAIDLMaps.get(process);
        if (wideRouterAIDL == null) {
            if (!bindService(context, process)) {
                return new RouterResponse.Builder<>()
                        .code(RouterResponse.CODE_ERROR)
                        .error("Unable to connect to the process: " + process)
                        .result(null)
                        .build();
            }
        }
        RouterResponse response = null;
        if (wideRouterAIDL != null) {
            response = wideRouterAIDL.route(request);
        }
        return response;
    }

    private boolean bindService(Context context, String process) {
        if (!mRegisterServicesMap.containsKey(process)) {
            return false;
        }
        Class<? extends LocalRouterService> serviceClass = mRegisterServicesMap.get(process);
        if (serviceClass == null) {
            return false;
        }
        Context appContext = context.getApplicationContext();
        Intent intent = new Intent(appContext, serviceClass);
        appContext.bindService(intent, new RouterServiceConnection(context, process), Context.BIND_AUTO_CREATE);
        return true;
    }

    private class RouterServiceConnection implements ServiceConnection {

        private Context mContext;
        private String mProcessName;

        RouterServiceConnection(Context context, String processName) {
            mContext = context.getApplicationContext();
            mProcessName = processName;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            IWideRouterAIDL wideRouterAIDL = IWideRouterAIDL.Stub.asInterface(service);
            mWideRouterAIDLMaps.put(mProcessName, wideRouterAIDL);
            Log.v(TAG, "router connect process: " + mProcessName + " success");
            if (mWideRouterAIDLMaps.size() == mRegisterServicesMap.size()) {
                mInitCompleted = true;
                InitializeCompleteReceiver.send(mContext);
                Log.v(TAG, "Router Service initialize completed!");
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mWideRouterAIDLMaps.remove(mProcessName);
        }
    }

    boolean isInitCompleted() {
        return mInitCompleted;
    }
}
