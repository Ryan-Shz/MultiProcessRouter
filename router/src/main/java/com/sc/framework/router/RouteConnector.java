package com.sc.framework.router;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.sc.framework.router.remote.IWideRouter;
import com.sc.framework.router.remote.WideRouterNative;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author shamschu
 */
class RouteConnector {

    public static final int CONNECT_TIME_OUT = 1000;
    private IWideRouter mWideRouterBinder;
    private Lock mSyncLock;
    private Condition mSyncCondition;

    RouteConnector() {
        mSyncLock = new ReentrantLock();
        mSyncCondition = mSyncLock.newCondition();
    }

    boolean connect(Context context) {
        try {
            mSyncLock.lock();
            context = context.getApplicationContext();
            Intent intent = new Intent(context, WideRouteService.class);
            context.bindService(intent, new WideRouterServiceConnection(context), Context.BIND_AUTO_CREATE);
            mSyncCondition.await(CONNECT_TIME_OUT, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            mSyncLock.unlock();
        }
        return mWideRouterBinder != null;
    }

    private void signalAll() {
        try {
            mSyncLock.lock();
            mSyncCondition.signalAll();
        } finally {
            mSyncLock.unlock();
        }
    }

    private boolean attachDeathRecipient(final Context context, IBinder binder) throws RemoteException {
        if (!binder.isBinderAlive()) {
            return false;
        }
        binder.linkToDeath(new IBinder.DeathRecipient() {
            @Override
            public void binderDied() {
                mWideRouterBinder = null;
                connect(context);
            }
        }, 0);
        return true;
    }

    private class WideRouterServiceConnection implements ServiceConnection {

        private Context context;

        WideRouterServiceConnection(Context context) {
            this.context = context.getApplicationContext();
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.v("HHHH", "onServiceConnected");
            boolean attached = false;
            try {
                attached = attachDeathRecipient(context, service);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            if (attached) {
                mWideRouterBinder = WideRouterNative.asInterface(service);
            }
            signalAll();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mWideRouterBinder = null;
        }
    }

    boolean isConnectionAlive() {
        return mWideRouterBinder != null && mWideRouterBinder.asBinder().isBinderAlive();
    }

    IWideRouter getRouteConnection() {
        return mWideRouterBinder;
    }
}
