package com.sc.framework.router;

import com.sc.framework.router.constants.RouterConstants;

/**
 * check the wide router service initialization state
 *
 * @author ShamsChu
 * @Date 17/9/6 10:23
 */
class InternalServiceCheckProvider extends RouterProvider {

    @Override
    public String getName() {
        return RouterConstants.PROVIDER_ROUTER_INTERNAL;
    }
}
