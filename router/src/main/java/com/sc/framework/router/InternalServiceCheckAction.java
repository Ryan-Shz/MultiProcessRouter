package com.sc.framework.router;

import android.content.Context;

import com.sc.framework.router.constants.RouterConstants;

/**
 * @author ShamsChu
 * @Date 17/9/6 10:28
 */
class InternalServiceCheckAction extends RouterAction<Void, Boolean> {

    @Override
    public RouterResponse<Boolean> invoke(Context context, RouterRequest<Void> request) {
        boolean isInitCompleted = WideRouterManager.getInstance().isInitCompleted();
        return new RouterResponse.Builder<Boolean>()
                .result(isInitCompleted)
                .build();
    }

    @Override
    public String getName() {
        return RouterConstants.ACTION_SERVICE_CONNECT_STATUS_CHECK;
    }
}
