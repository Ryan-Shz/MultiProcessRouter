package com.sc.sample.action;

import android.content.Context;

import com.sc.framework.annotation.Action;
import com.sc.framework.router.RouteResponse;
import com.sc.framework.router.RouteAction;
import com.sc.framework.router.RouteRequest;
import com.sc.sample.bean.TestResult;
import com.sc.sample.provider.MainProcessProvider;

/**
 * @author shamschu
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
