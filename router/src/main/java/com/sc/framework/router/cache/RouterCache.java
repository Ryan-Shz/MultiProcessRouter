package com.sc.framework.router.cache;

import com.sc.framework.router.RouterRequest;
import com.sc.framework.router.RouterResponse;

/**
 * @author ShamsChu
 * @Date 17/9/1 下午4:51
 */
public interface RouterCache {

    void putCache(RouterRequest request, RouterResponse response);

    RouterResponse getCache(RouterRequest request);

    void clear();

    void remove(RouterRequest key);

}
