package com.sc.sample;

import android.content.Context;

import com.sc.framework.annotation.Action;
import com.sc.framework.router.RouterAction;
import com.sc.framework.router.RouterRequest;
import com.sc.framework.router.RouterResponse;

/**
 * @author ShamsChu
 * @Date 17/8/30 上午9:10
 */
@Action(provider = TestProviderThree.class)
public class TestActionThree extends RouterAction<TestResult> {
    @Override
    public RouterResponse<TestResult> invoke(Context context, RouterRequest request) {
        TestResult result = new TestResult();
        result.setRtnMessage("this is three router test");
        return new RouterResponse.Builder<TestResult>()
            .result(result)
            .build();
    }
}
