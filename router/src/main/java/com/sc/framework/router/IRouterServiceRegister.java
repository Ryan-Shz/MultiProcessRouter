package com.sc.framework.router;

import java.util.Map;

/**
 * @author ShamsChu
 * @Date 17/8/28 下午3:35
 */
public interface IRouterServiceRegister {

    Map<String, Class<? extends LocalRouterService>> getServices();

}
