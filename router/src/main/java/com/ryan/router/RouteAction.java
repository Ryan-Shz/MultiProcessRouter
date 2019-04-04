package com.ryan.router;

import android.content.Context;

/**
 * @author Ryan
 * @Date 17/5/10 下午3:54
 */
public abstract class RouteAction<T, V> {

    /**
     * to perform an action function
     * @param context Context
     * @param request RouteRequest
     * @return RouteResponse
     */
    public abstract RouteResponse<V> invoke(Context context, RouteRequest<T> request);

    /**
     * action name, default is class simple name
     * @return action name
     */
    public String getName() {
        return getClass().getSimpleName();
    }

}
