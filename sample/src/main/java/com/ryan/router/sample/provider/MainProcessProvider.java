package com.ryan.router.sample.provider;

import com.ryan.router.annotation.Provider;
import com.ryan.router.RouteProvider;

/**
 * @author Ryan
 * @Date 17/8/28 上午9:27
 */
@Provider(process = "com.ryan.router.sample")
public class MainProcessProvider extends RouteProvider {

    @Override
    public String getName() {
        return "MainProcessProvider";
    }
}
