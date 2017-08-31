package com.sc.sample;

import android.app.Application;
import android.util.Log;

import com.sc.framework.router.IRouterServiceRegister;
import com.sc.framework.router.LocalRouterService;
import com.sc.framework.router.Router;
import com.sc.framework.router.utils.ProcessUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ShamsChu
 * @Date 17/8/28 下午2:13
 */
public class TestApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // 路由框架初始化
        Router.register(this, new IRouterServiceRegister() {
            @Override
            public Map<String, Class<? extends LocalRouterService>> getServices() {
                Map<String, Class<? extends LocalRouterService>> services = new HashMap<>();
                services.put(ProcessUtils.getMainProcess(TestApplication.this), MainProcessService.class);
                services.put(ProcessUtils.getMainProcess(TestApplication.this) + ProcessUtils.COLON + "second", ProcessServicetTwo.class);
                return services;
            }
        });
    }

    @Override
    public void onTerminate() {
        Log.v("TestApplication", "onTerminate");
        super.onTerminate();
    }

}
