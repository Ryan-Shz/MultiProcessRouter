package com.ryan.router.sample.provider;

import com.ryan.router.annotation.Provider;
import com.ryan.router.RouteProvider;

/**
 * @author Ryan
 * @Date 17/8/30 上午9:11
 */
@Provider(process = "com.ryan.router.sample:second")
public class SecondProcessProvider extends RouteProvider {

    @Override
    public String getName() {
        return "SecondProcessProvider";
    }
}
