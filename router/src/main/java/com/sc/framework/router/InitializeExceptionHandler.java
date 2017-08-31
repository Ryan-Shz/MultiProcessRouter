package com.sc.framework.router;

import android.content.Context;

/**
 * @author ShamsChu
 * @Date 17/8/30 下午2:58
 */
class InitializeExceptionHandler {

    private IRouterServiceRegister mRegister;

    InitializeExceptionHandler(IRouterServiceRegister register) {
        mRegister = register;
    }

    synchronized boolean tryReInitialize(Context context) {
        RouterInitializer initializer = new RouterInitializer();
        try {
            initializer.init(context, mRegister);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

}
