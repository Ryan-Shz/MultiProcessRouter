package com.sc.framework.router;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * @author ShamsChu
 * @Date 17/8/30 下午5:54
 */
public class InitializeCompleteReceiver extends BroadcastReceiver {

    private static final String ACTION_ROUTER_SERVICE_COMPLETED = "action.router.service.completed";
    private Callback mCallback;

    private InitializeCompleteReceiver(Callback callback) {
        mCallback = callback;
    }

    public static BroadcastReceiver register(Context context, Callback callback) {
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_ROUTER_SERVICE_COMPLETED);
        InitializeCompleteReceiver receiver = new InitializeCompleteReceiver(callback);
        context.registerReceiver(receiver, filter);
        return receiver;
    }

    public static void send(Context context) {
        Intent intent = new Intent(ACTION_ROUTER_SERVICE_COMPLETED);
        context.sendBroadcast(intent);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (ACTION_ROUTER_SERVICE_COMPLETED.equals(action)) {
            doCallback();
        }
    }

    private void doCallback() {
        if (mCallback != null) {
            mCallback.onRouterServiceInitCompleted();
        }
    }

    public interface Callback {
        void onRouterServiceInitCompleted();
    }

}
