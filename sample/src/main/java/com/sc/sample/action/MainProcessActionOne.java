package com.sc.sample.action;

import android.content.Context;

import com.sc.framework.annotation.Action;
import com.sc.framework.router.RouterAction;
import com.sc.framework.router.RouterRequest;
import com.sc.framework.router.RouterResponse;
import com.sc.sample.provider.MainProcessProvider;

/**
 * @author ShamsChu
 * @Date 17/8/28 上午9:27
 */
@Action(provider = MainProcessProvider.class)
public class MainProcessActionOne extends RouterAction<Void, String> {

    @Override
    public RouterResponse<String> invoke(Context context, RouterRequest request) {
        return new RouterResponse.Builder<String>()
            .result("this is a router test!")
            .build();
    }

}
