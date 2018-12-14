package com.sc.framework.router.remote;

import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;

import com.sc.framework.router.RouteRequest;
import com.sc.framework.router.RouteResponse;

/**
 * @author shamschu
 * @date 2018/10/11
 */
public interface ILocalRouter extends IInterface {

    int CODE_LOCAL_ROUTER = IBinder.FIRST_CALL_TRANSACTION + 1;

    RouteResponse localRoute(RouteRequest request) throws RemoteException;

}
