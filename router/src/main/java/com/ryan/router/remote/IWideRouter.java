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
public interface IWideRouter extends IInterface {

    int CODE_WIDE_ROUTER = IBinder.FIRST_CALL_TRANSACTION + 2;

    RouteResponse wideRoute(RouteRequest request) throws RemoteException;

}
