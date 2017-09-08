package com.sc.framework.router;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.sc.framework.router.utils.ProcessUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author ShamsChu
 * @Date 17/5/10 下午3:49
 */
final class RouterManager {

    private static final String TAG = RouterManager.class.getName();
    private Map<String, RouterProvider> mProviders;
    private IWideRouterAIDL mWideRouterAIDL;
    private WideRouterServiceConnection mWideRouterServiceConnection;
    private static final ExecutorService mExecutorService = Executors.newCachedThreadPool();
    private final byte[] mWideLock = new byte[0];
    private volatile boolean mInitialize = false;
    private boolean mAsyncConnecting = false;
    private InitializeExceptionHandler mExceptionHandler;

    private RouterManager() {
        mProviders = new HashMap<>();
    }

    private static class SingletonHolder {
        private static final RouterManager INSTANCE = new RouterManager();
    }

    static RouterManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    <V> RouterResponse<V> request(@NonNull Context context, @NonNull RouterRequest request) {
        if (!mInitialize) {
            mInitialize = mExceptionHandler.tryReInitialize(context);
            if (!mInitialize) {
                return new RouterResponse.Builder<V>()
                        .error("Router service is not registered! are you sure call the Router.register(Context Context, IRouterServiceRegister serviceRegister)?")
                        .code(RouterResponse.CODE_ERROR)
                        .result(null)
                        .build();
            }
        }
        String process = request.getProcess();
        String action = request.getAction();
        String providerName = request.getProvider();
        String currentProcess = ProcessUtils.getCurrentProcessName(context);
        if (currentProcess == null) {
            return new RouterResponse.Builder<V>()
                    .result(null)
                    .code(RouterResponse.CODE_PROCESS_NO_RUNNING)
                    .error("Unable to connect to the process, because it is not running!")
                    .build();
        }
        if (process == null || process.equals(currentProcess)) {
            RouterProvider provider = findProvider(providerName);
            if (provider == null) {
                return new RouterResponse.Builder<V>()
                        .result(null)
                        .code(RouterResponse.CODE_PROVIDER_NO_FOUND)
                        .error("Cannot find the specified Provider: " + providerName + ", make sure you have already registered it.")
                        .build();
            }
            RouterAction routerAction = provider.findAction(action);
            if (routerAction == null) {
                return new RouterResponse.Builder<V>()
                        .result(null)
                        .code(RouterResponse.CODE_ACTION_NO_FOUND)
                        .error("Cannot find the specified Action: " + action + ", make sure you have already registered it.")
                        .build();
            }
            return routerAction.invoke(context, request);
        }
        return wideRequest(context, request);
    }

    private RouterResponse wideRequest(Context context, RouterRequest request) {
        if (mWideRouterAIDL == null) {
            Log.v(TAG, "Wide Router Service has not been connected, try to connect...");
            Future<Object> future = mExecutorService.submit(new WideRouterRequestAsyncTask(context, request));
            return new RouterResponse.Builder<>()
                    .code(RouterResponse.CODE_SUCCESS)
                    .future(future)
                    .build();
        }
        return wideRequest(request);
    }

    private RouterResponse wideRequest(RouterRequest request) {
        try {
            return mWideRouterAIDL.route(request);
        } catch (RemoteException e) {
            e.printStackTrace();
            return new RouterResponse.Builder<>()
                    .error(e.getMessage())
                    .code(RouterResponse.CODE_ERROR)
                    .result(null)
                    .build();
        }
    }

    void registerProvider(RouterProvider provider) {
        if (provider == null || TextUtils.isEmpty(provider.getName())) {
            return;
        }
        mProviders.put(provider.getName(), provider);
    }

    private RouterProvider findProvider(String providerName) {
        if (TextUtils.isEmpty(providerName)) {
            Log.v(TAG, "provider name is null or empty, please use the correct provider name.");
            return null;
        }
        if (!mProviders.containsKey(providerName)) {
            Log.v(TAG, "Cannot find the specified provider: " + providerName + "! please check the provider name and make sure that you use the @Provider in correct way.");
            return null;
        }
        return mProviders.get(providerName);
    }

    void bindWideRouterService(Context context) {
        context = context.getApplicationContext();
        Intent intent = new Intent(context, WideRouterService.class);
        mWideRouterServiceConnection = new WideRouterServiceConnection();
        context.bindService(intent, mWideRouterServiceConnection, Context.BIND_AUTO_CREATE);
    }

    void unbindWideRouterService(Context context) {
        context = context.getApplicationContext();
        context.unbindService(mWideRouterServiceConnection);
    }

    private class WideRouterServiceConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mWideRouterAIDL = IWideRouterAIDL.Stub.asInterface(service);
            Log.v(TAG, "Wide Router Service connect success!");
            if (mAsyncConnecting) {
                synchronized (mWideLock) {
                    mWideLock.notifyAll();
                }
                mAsyncConnecting = false;
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mWideRouterAIDL = null;
            mAsyncConnecting = false;
        }
    }

    private class WideRouterRequestAsyncTask implements Callable<Object> {

        private Context mAppContext;
        private RouterRequest mRouterRequest;

        WideRouterRequestAsyncTask(Context context, RouterRequest request) {
            mAppContext = context.getApplicationContext();
            mRouterRequest = request;
        }

        @Override
        public Object call() throws Exception {
            if (mWideRouterAIDL == null) {
                mAsyncConnecting = true;
                bindWideRouterService(mAppContext);
                if (mWideRouterAIDL == null) {
                    synchronized (mWideLock) {
                        mWideLock.wait();
                    }
                }
            }
            RouterResponse response = wideRequest(mRouterRequest);
            return response != null ? response.getResult() : null;
        }
    }

    void initialize(Context context, IRouterServiceRegister serviceRegister) {
        RouterInitializer initializer = new RouterInitializer();
        try {
            mInitialize = initializer.init(context, serviceRegister);
        } catch (Exception e) {
            e.printStackTrace();
            mExceptionHandler = new InitializeExceptionHandler(serviceRegister);
        }
    }

}
