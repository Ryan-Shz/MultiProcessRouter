package com.sc.sample.action;

import android.content.Context;

import com.sc.framework.annotation.Action;
import com.sc.framework.router.RouteResponse;
import com.sc.framework.router.RouteAction;
import com.sc.framework.router.RouteRequest;
import com.sc.sample.provider.MainProcessProvider;

/**
 * @author shamschu
 * @Date 17/8/28 上午9:27
 */
@Action(provider = MainProcessProvider.class)
public class MainProcessActionOne extends RouteAction<Void, String> {

    @Override
    public RouteResponse<String> invoke(Context context, RouteRequest request) {
        return new RouteResponse.Builder<String>()
            .result("this is a router test!")
            .build();
    }

}
