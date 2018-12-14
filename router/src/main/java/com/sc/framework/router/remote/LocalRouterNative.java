package com.sc.framework.router.remote;

import android.content.Context;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteException;

import com.sc.framework.router.RouteManager;
import com.sc.framework.router.RouteRequest;
import com.sc.framework.router.RouteResponse;

/**
 * @author shamschu
 * @date 2018/10/11
 */
public class LocalRouterNative extends Binder implements ILocalRouter {

    private static final String DESCRIPTOR = LocalRouterNative.class.getName();
    private Context mContext;

    public LocalRouterNative(Context context) {
        mContext = context;
        attachInterface(this, DESCRIPTOR);
    }

    @Override
    protected boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
        if (code == CODE_LOCAL_ROUTER) {
            data.enforceInterface(DESCRIPTOR);
            data.readException();
            RouteRequest request = RouteRequest.CREATOR.createFromParcel(data);
            request.setInJustRouteLocal(true);
            RouteResponse response = localRoute(request);
            reply.writeNoException();
            response.writeToParcel(reply, Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
            return true;
        }
        return super.onTransact(code, data, reply, flags);
    }

    @Override
    public IBinder asBinder() {
        return this;
    }

    public static ILocalRouter asInterface(IBinder binder) {
        IInterface iInterface = binder.queryLocalInterface(DESCRIPTOR);
        if (iInterface != null) {
            return (ILocalRouter) iInterface;
        }
        return new LocalRouterProxy(binder);
    }

    @Override
    public RouteResponse localRoute(RouteRequest request) throws RemoteException {
        RouteResponse response;
        try {
            response = RouteManager.getInstance().request(mContext, request);
        } catch (Exception e) {
            e.printStackTrace();
            response = RouteResponse.error(e.getMessage());
        }
        return response;
    }

    private static class LocalRouterProxy implements ILocalRouter {

        private IBinder mRemote;

        LocalRouterProxy(IBinder remote) {
            mRemote = remote;
        }

        @Override
        public RouteResponse localRoute(RouteRequest request) throws RemoteException {
            RouteResponse response;
            Parcel data = Parcel.obtain();
            Parcel reply = Parcel.obtain();
            try {
                data.writeInterfaceToken(DESCRIPTOR);
                data.writeNoException();
                request.writeToParcel(data, 0);
                mRemote.transact(CODE_LOCAL_ROUTER, data, reply, 0);
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
