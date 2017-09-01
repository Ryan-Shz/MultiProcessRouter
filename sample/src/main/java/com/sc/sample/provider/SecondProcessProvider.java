package com.sc.sample.provider;

import com.sc.framework.annotation.Provider;
import com.sc.framework.router.RouterProvider;

/**
 * @author ShamsChu
 * @Date 17/8/30 上午9:11
 */
@Provider(process = "com.sc.sample:second")
public class SecondProcessProvider extends RouterProvider {

    @Override
    public String getName() {
        return "SecondProcessProvider";
    }
}
