package com.sc.framework.router;

import java.util.HashMap;
import java.util.Map;

/**
 * @author shamschu
 * @Date 17/5/10 下午3:52
 */
public abstract class RouteProvider {

    private final Map<String, RouteAction> mActions;

    public RouteProvider() {
        mActions = new HashMap<>();
    }

    public RouteAction findAction(String actionName) {
        if (mActions.containsKey(actionName)) {
            return mActions.get(actionName);
        }
        return null;
    }

    public void addAction(RouteAction action) {
        String name = action.getName();
        mActions.put(name, action);
    }

    public abstract String getName();

}
