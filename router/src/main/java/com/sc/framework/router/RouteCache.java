package com.sc.framework.router;

/**
 * @author shamschu
 * @Date 17/9/1 下午4:51
 */
public interface RouteCache {

    void save(RouteRequest request, RouteResponse response);

    RouteResponse getCache(RouteRequest request);

    void clear();

    void remove(RouteRequest key);

}
