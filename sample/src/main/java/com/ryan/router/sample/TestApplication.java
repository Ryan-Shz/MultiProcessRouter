package com.ryan.router.sample;

import android.app.Application;
import android.util.Log;

import com.ryan.router.LocalRouteService;
import com.ryan.router.Router;
import com.ryan.router.ProcessUtils;
import com.ryan.router.sample.service.MainProcessService;
import com.ryan.router.sample.service.SecondProcessService;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Ryan
 * @Date 17/8/28 下午2:13
 */
public class TestApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // 路由框架初始化
        Map<String, Class<? extends LocalRouteService>> services = new HashMap<>();
        services.put(ProcessUtils.getMainProcess(TestApplication.this), MainProcessService.class);
        services.put(ProcessUtils.getMainProcess(TestApplication.this) + ProcessUtils.COLON + "second", SecondProcessService.class);
        Router.init(this, services);
    }

    @Override
    public void onTerminate() {
        Log.v("TestApplication", "onTerminate");
        super.onTerminate();
    }

}
