package com.sc.sample;

import android.app.Application;
import android.util.Log;

import com.sc.framework.router.LocalRouteService;
import com.sc.framework.router.Router;
import com.sc.framework.router.ProcessUtils;
import com.sc.sample.service.MainProcessService;
import com.sc.sample.service.SecondProcessService;

import java.util.HashMap;
import java.util.Map;

/**
 * @author shamschu
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
