package com.sc.framework.router.cache;

import android.util.LruCache;

import com.sc.framework.router.RouterRequest;
import com.sc.framework.router.RouterResponse;

/**
 * @author ShamsChu
 * @Date 17/8/29 下午7:07
 */
public class RouterCache {

    private LruCache<RouterRequest, RouterResponse> mCache;

    public RouterCache() {
        mCache = new LruCache<>(5);
    }

    public void putCache(RouterRequest request, RouterResponse response) {
        if (request == null || response == null) {
            return;
        }
        CacheStrategy strategy = request.getCacheStrategy();
        if (strategy == CacheStrategy.NONE) {
            return;
        }
        if (!response.isSuccess()) {
            return;
        }
        mCache.put(request, response);
    }

    public RouterResponse getCache(RouterRequest request) {
        CacheStrategy strategy = request.getCacheStrategy();
        if (strategy == CacheStrategy.NONE) {
            return null;
        }
        return mCache.get(request);
    }

}
