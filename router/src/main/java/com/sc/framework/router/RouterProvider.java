package com.sc.framework.router;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ShamsChu
 * @Date 17/5/10 下午3:52
 */
public abstract class RouterProvider {

    private static final String TAG = RouterProvider.class.getName();
    private final Map<String, RouterAction> mActions;

    public RouterProvider() {
        mActions = new HashMap<>();
    }

    public RouterAction findAction(String actionName) {
        if (!mActions.containsKey(actionName)) {
            Log.v(TAG, "Cannot find the specified action: " + actionName + ", please check the action name and make sure that your use the @Action in correct way.");
            return null;
        }
        return mActions.get(actionName);
    }

    public void addAction(RouterAction action) {
        String name = action.getName();
        if (mActions.containsKey(name)) {
            Log.e(TAG, "Repeat to add the Action! action name: " + name + ", please check your code.");
        }
        mActions.put(name, action);
    }

    public abstract String getName();

}
