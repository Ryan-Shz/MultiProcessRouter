package com.ryan.router.sample.action;

import android.content.Context;
import android.text.TextUtils;

import com.ryan.router.annotation.Action;
import com.ryan.router.RouteRequest;
import com.ryan.router.RouteResponse;
import com.ryan.router.RouteAction;
import com.ryan.router.sample.bean.TestResult;
import com.ryan.router.sample.provider.SecondProcessProvider;

/**
 * @author Ryan
 * @Date 17/8/30 上午9:10
 */
@Action(provider = SecondProcessProvider.class)
public class SecondProcessAction extends RouteAction<String, TestResult> {

    @Override
    public RouteResponse<TestResult> invoke(Context context, RouteRequest<String> request) {
        String data = TextUtils.isEmpty(request.getParameter()) ? "request success! no request parameters"
            : "request success! receive a request parameter: " + request.getParameter();
        TestResult result = new TestResult();
        result.setRtnMessage(data);
        return new RouteResponse.Builder<TestResult>()
            .result(result)
            .build();
    }
}
