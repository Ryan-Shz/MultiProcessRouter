package com.sc.sample.action;

import android.content.Context;

import com.sc.framework.annotation.Action;
import com.sc.framework.router.RouterAction;
import com.sc.framework.router.RouterRequest;
import com.sc.framework.router.RouterResponse;
import com.sc.sample.bean.TestResult;
import com.sc.sample.provider.MainProcessProvider;

/**
 * @author ShamsChu
 * @Date 17/8/29 下午3:00
 */
@Action(provider = MainProcessProvider.class)
public class MainProcessActionTwo extends RouterAction<Void, TestResult> {

    @Override
    public RouterResponse<TestResult> invoke(Context context, RouterRequest request) {
        TestResult result = new TestResult();
        result.setRtnMessage("this is second router test!");
        return new RouterResponse.Builder<TestResult>()
            .result(result)
            .build();
    }

    @Override
    public String getName() {
        return "TestActionTwo";
    }
}
