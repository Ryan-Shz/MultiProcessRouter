package com.sc.framework.router.cache;

import android.util.LruCache;

import com.sc.framework.router.RouterRequest;
import com.sc.framework.router.RouterResponse;

/**
 * @author ShamsChu
 * @Date 17/8/29 下午7:07
 */
public class DefaultRouterCache implements RouterCache {

    private static final int DEFAULT_CACHE_SIZE = 5;
    private LruCache<RouterRequest, RouterResponse> mCache;

    public DefaultRouterCache() {
        mCache = new LruCache<>(getCacheSize());
    }

    @Override
    public void putCache(RouterRequest request, RouterResponse response) {
        if (request == null || response == null) {
            return;
        }
        MemoryCacheStrategy strategy = request.getCacheStrategy();
        if (strategy == MemoryCacheStrategy.NONE) {
            return;
        }
        if (!response.isSuccess()) {
            return;
        }
        mCache.put(request, response);
    }

    @Override
    public RouterResponse getCache(RouterRequest request) {
        MemoryCacheStrategy strategy = request.getCacheStrategy();
        if (strategy == MemoryCacheStrategy.NONE) {
            return null;
        }
        return mCache.get(request);
    }

    /**
     * clear all cache
     */
    @Override
    public void clear() {
        mCache.evictAll();
    }

    /**
     * remove the specified cache with its key
     *
     * @param key cache key
     */
    @Override
    public void remove(RouterRequest key) {
        mCache.remove(key);
    }

    /**
     * Generate the specified cache key
     *
     * @param process  Process
     * @param provider Provider
     * @param action   Action
     * @return specified cache key, is a RouterRequest
     */
    public RouterRequest generateCacheKey(String process, String provider, String action) {
        return new RouterRequest.Builder<>()
            .process(process)
            .provider(provider)
            .action(action)
            .build();
    }

    public int getCacheSize() {
        return DEFAULT_CACHE_SIZE;
    }

}
