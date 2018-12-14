package com.sc.framework.router;

import android.util.SparseArray;

/**
 * @author shamschu
 * @Date 17/8/29 下午7:07
 */
public class DefaultRouteCache implements RouteCache {

    private SparseArray<RouteResponse> mCacheMap;

    public DefaultRouteCache() {
        mCacheMap = new SparseArray<>();
    }

    @Override
    public void save(RouteRequest request, RouteResponse response) {
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
        mCacheMap.put(generateCacheKey(request), response);
    }

    @Override
    public RouteResponse getCache(RouteRequest request) {
        MemoryCacheStrategy strategy = request.getCacheStrategy();
        if (strategy == MemoryCacheStrategy.NONE) {
            return null;
        }
        return mCacheMap.get(generateCacheKey(request));
    }

    /**
     * clear all cache
     */
    @Override
    public void clear() {
        mCacheMap.clear();
    }

    /**
     * remove the specified cache with its key
     *
     * @param key cache key
     */
    @Override
    public void remove(RouteRequest key) {
        mCacheMap.remove(generateCacheKey(key));
    }

    /**
     * Generate the specified cache key
     *
     * @return specified cache key, is a RouteRequest
     */
    private int generateCacheKey(RouteRequest request) {
        return request.hashCode();
    }

}
