package com.sc.framework.router.remote;

import android.content.Context;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteException;

import com.sc.framework.router.RouteRequest;
import com.sc.framework.router.RouteResponse;
import com.sc.framework.router.WideRouteManager;

/**
 * @author shamschu
 * @date 2018/10/11
 */
public class WideRouterNative extends Binder implements IWideRouter {

    private static final String DESCRIPTOR = WideRouterNative.class.getName();
    private Context mContext;

    public WideRouterNative(Context context) {
        mContext = context;
        attachInterface(this, DESCRIPTOR);
    }

    @Override
    protected boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
        if (code == CODE_WIDE_ROUTER) {
            data.enforceInterface(DESCRIPTOR);
            data.readException();
            RouteRequest request = RouteRequest.CREATOR.createFromParcel(data);
            RouteResponse response = wideRoute(request);
            reply.writeNoException();
            response.writeToParcel(reply, Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
            return true;
        }
        return super.onTransact(code, data, reply, flags);
    }

    @Override
    public RouteResponse wideRoute(RouteRequest request) {
        RouteResponse response;
        try {
            response = WideRouteManager.getInstance().route(mContext, request);
        } catch (Exception e) {
            e.printStackTrace();
            response = RouteResponse.error(e.getMessage());
        }
        return response;
    }

    @Override
    public IBinder asBinder() {
        return this;
    }

    public static IWideRouter asInterface(IBinder binder) {
        IInterface iInterface = binder.queryLocalInterface(DESCRIPTOR);
        if (iInterface != null) {
            return (IWideRouter) iInterface;
        }
        return new WideRouterProxy(binder);
    }

    private static class WideRouterProxy implements IWideRouter {

        private IBinder mRemote;

        WideRouterProxy(IBinder remote) {
            mRemote = remote;
        }

        @Override
        public RouteResponse wideRoute(RouteRequest request) throws RemoteException {
            RouteResponse response;
            Parcel data = Parcel.obtain();
            Parcel reply = Parcel.obtain();
            try {
                data.writeInterfaceToken(DESCRIPTOR);
                data.writeNoException();
                request.writeToParcel(data, 0);
                mRemote.transact(CODE_WIDE_ROUTER, data, reply, 0);
                reply.readException();
                response = RouteResponse.CREATOR.createFromParcel(reply);
            } finally {
                data.recycle();
                reply.recycle();
            }
            return response;
        }

        @Override
        public IBinder asBinder() {
            return mRemote;
        }
    }
}
