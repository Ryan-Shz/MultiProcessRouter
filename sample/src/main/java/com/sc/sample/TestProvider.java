package com.sc.sample;

import com.sc.framework.annotation.Provider;
import com.sc.framework.router.RouterProvider;

/**
 * @author ShamsChu
 * @Date 17/8/28 上午9:27
 */
@Provider(process = "com.sc.sample")
public class TestProvider extends RouterProvider {

    @Override
    public String getName() {
        return "TestProvider";
    }
}
