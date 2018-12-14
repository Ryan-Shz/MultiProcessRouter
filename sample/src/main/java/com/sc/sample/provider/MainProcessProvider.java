package com.sc.sample.provider;

import com.sc.framework.annotation.Provider;
import com.sc.framework.router.RouteProvider;

/**
 * @author shamschu
 * @Date 17/8/28 上午9:27
 */
@Provider(process = "com.sc.sample")
public class MainProcessProvider extends RouteProvider {

    @Override
    public String getName() {
        return "MainProcessProvider";
    }
}
