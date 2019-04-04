package com.ryan.router;

import android.app.ActivityManager;
import android.content.Context;

/**
 * @author zhushuhang
 * @Date 17/8/28 下午3:55
 */
public class ProcessUtils {

    public static final String COLON = ":";
    private static final String PROCESS_NAME_ROUTER = "router";

    public static String getRouterProcess(Context context) {
        return getMainProcess(context) + COLON + PROCESS_NAME_ROUTER;
    }

    public static String getMainProcess(Context context) {
        return context.getPackageName();
    }

    public static String getCurrentProcessName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager mActivityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        if (mActivityManager != null) {
            for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager
                    .getRunningAppProcesses()) {
                if (appProcess.pid == pid) {
                    return appProcess.processName;
                }
            }
        }
        return null;
    }

    public static boolean runningInRouterProcess(Context context) {
        String curr = getCurrentProcessName(context);
        return curr != null && curr.equals(getRouterProcess(context));
    }
}
