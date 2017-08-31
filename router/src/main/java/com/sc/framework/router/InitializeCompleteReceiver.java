package com.sc.framework.router;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * @author ShamsChu
 * @Date 17/8/30 下午5:54
 */
public abstract class InitializeCompleteReceiver extends BroadcastReceiver {

    public static final String ACTION_ROUTER_SERVICE_COMPLETED = "action.router.service.completed";

    public static void send(Context context) {
        Intent intent = new Intent(ACTION_ROUTER_SERVICE_COMPLETED);
        context.sendBroadcast(intent);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(ACTION_ROUTER_SERVICE_COMPLETED)) {
            onRouterServiceInitCompleted();
        }
    }

    protected abstract void onRouterServiceInitCompleted();

}
