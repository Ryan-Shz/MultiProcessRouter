package com.sc.sample.action;

import android.content.Context;
import android.text.TextUtils;

import com.sc.framework.annotation.Action;
import com.sc.framework.router.RouteRequest;
import com.sc.framework.router.RouteResponse;
import com.sc.framework.router.RouteAction;
import com.sc.sample.bean.TestResult;
import com.sc.sample.provider.SecondProcessProvider;

/**
 * @author shamschu
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
