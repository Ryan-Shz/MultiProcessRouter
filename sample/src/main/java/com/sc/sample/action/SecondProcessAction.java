package com.sc.sample.action;

import android.content.Context;
import android.text.TextUtils;

import com.sc.framework.annotation.Action;
import com.sc.framework.router.RouterAction;
import com.sc.framework.router.RouterRequest;
import com.sc.framework.router.RouterResponse;
import com.sc.sample.bean.TestResult;
import com.sc.sample.provider.SecondProcessProvider;

/**
 * @author ShamsChu
 * @Date 17/8/30 上午9:10
 */
@Action(provider = SecondProcessProvider.class)
public class SecondProcessAction extends RouterAction<String, TestResult> {

    @Override
    public RouterResponse<TestResult> invoke(Context context, RouterRequest<String> request) {
        String data = TextUtils.isEmpty(request.getParameter()) ? "request success! no request parameters"
            : "request success! receive a request parameter: " + request.getParameter();
        TestResult result = new TestResult();
        result.setRtnMessage(data);
        return new RouterResponse.Builder<TestResult>()
            .result(result)
            .build();
    }
}
