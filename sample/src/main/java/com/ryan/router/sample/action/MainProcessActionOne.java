package com.ryan.router.sample.action;

import android.content.Context;

import com.ryan.router.annotation.Action;
import com.ryan.router.RouteResponse;
import com.ryan.router.RouteAction;
import com.ryan.router.RouteRequest;
import com.ryan.router.sample.provider.MainProcessProvider;

/**
 * @author Ryan
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
