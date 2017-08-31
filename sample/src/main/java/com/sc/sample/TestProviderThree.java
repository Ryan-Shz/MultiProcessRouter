package com.sc.sample;

import com.sc.framework.annotation.Provider;
import com.sc.framework.router.RouterProvider;

/**
 * @author ShamsChu
 * @Date 17/8/30 上午9:11
 */
@Provider(process = "com.sc.sample:second")
public class TestProviderThree extends RouterProvider {

    @Override
    public String getName() {
        return "TestProviderThree";
    }
}
