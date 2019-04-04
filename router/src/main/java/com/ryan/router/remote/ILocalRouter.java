package com.ryan.router.remote;

import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;

import com.ryan.router.RouteRequest;
import com.ryan.router.RouteResponse;

/**
 * @author Ryan
 * create by 2018/10/11
 */
public interface ILocalRouter extends IInterface {

    int CODE_LOCAL_ROUTER = IBinder.FIRST_CALL_TRANSACTION + 1;

    RouteResponse localRoute(RouteRequest request) throws RemoteException;

}
