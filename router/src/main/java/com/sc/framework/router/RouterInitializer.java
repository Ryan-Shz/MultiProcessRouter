package com.sc.framework.router;

import android.content.Context;

import com.sc.framework.router.utils.ProcessUtils;

/**
 * @author ShamsChu
 * @Date 17/8/29 下午2:53
 */
class RouterInitializer {

    private static final String TARGET_CLASS = "com.sc.framework.router.RouterRegister";

    synchronized boolean init(Context context, IRouterServiceRegister serviceRegister) throws ClassNotFoundException, IllegalAccessException, InstantiationException, RouterException {
        String process = ProcessUtils.getCurrentProcessName(context);
        if (process == null) {
            throw new RouterException("Unable to get the current process name, Router service initialization failed!");
        }
        registerProviders(process);
        if (process.equals(ProcessUtils.getRouterProcess(context))) {
            RouterProvider provider = new InternalServiceCheckProvider();
            RouterAction<Void, Boolean> action = new InternalServiceCheckAction();
            provider.addAction(action);
            RouterManager.getInstance().registerProvider(provider);
            WideRouterManager.getInstance().registerServices(context, serviceRegister.getServices());
        } else {
            RouterManager.getInstance().bindWideRouterService(context.getApplicationContext());
        }
        return true;
    }

    private synchronized boolean registerProviders(String process) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        Class<?> targetClass = Class.forName(TARGET_CLASS);
        IRouterRegister register = (IRouterRegister) targetClass.newInstance();
        register.register(process);
        return true;
    }

}
