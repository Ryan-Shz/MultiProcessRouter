package com.ryan.router.sample.action;

import android.content.Context;

import com.ryan.router.annotation.Action;
import com.ryan.router.RouteResponse;
import com.ryan.router.RouteAction;
import com.ryan.router.RouteRequest;
import com.ryan.router.sample.bean.TestResult;
import com.ryan.router.sample.provider.MainProcessProvider;

/**
 * @author Ryan
 * @Date 17/8/29 下午3:00
 */
@Action(provider = MainProcessProvider.class)
public class MainProcessActionTwo extends RouteAction<Void, TestResult> {

    @Override
    public RouteResponse<TestResult> invoke(Context context, RouteRequest request) {
        TestResult result = new TestResult();
        result.setRtnMessage("this is second router test!");
        return new RouteResponse.Builder<TestResult>()
            .result(result)
            .build();
    }

    @Override
    public String getName() {
        return "TestActionTwo";
    }
}
