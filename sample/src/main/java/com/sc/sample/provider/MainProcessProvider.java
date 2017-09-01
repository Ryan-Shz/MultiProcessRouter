package com.sc.sample.provider;

import com.sc.framework.annotation.Provider;
import com.sc.framework.router.RouterProvider;

/**
 * @author ShamsChu
 * @Date 17/8/28 上午9:27
 */
@Provider(process = "com.sc.sample")
public class MainProcessProvider extends RouterProvider {

    @Override
    public String getName() {
        return "MainProcessProvider";
    }
}
