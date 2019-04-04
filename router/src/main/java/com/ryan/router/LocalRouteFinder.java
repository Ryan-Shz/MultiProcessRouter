package com.ryan.router;

import android.content.Context;
import android.text.TextUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Ryan
 * create by 2018/10/11
 */
public class LocalRouteFinder {

    private Map<String, RouteProvider> mProviders;

    public LocalRouteFinder() {
        mProviders = new ConcurrentHashMap<>();
    }

    @SuppressWarnings("unchecked")
    public RouteResponse findLocal(Context context, RouteRequest request) {
        String process = request.getProcess();
        String action = request.getAction();
        String providerName = request.getProvider();
        String currentProcess = ProcessUtils.getCurrentProcessName(context);
        if (currentProcess == null) {
            return RouteResponse.error("");
        }
        if (process == null || process.equals(currentProcess)) {
            RouteProvider provider = findProvider(providerName);
            if (provider == null) {
                return RouteResponse.error("can not find provider: " + providerName);
            }
            RouteAction routerAction = provider.findAction(action);
            if (routerAction == null) {
                return RouteResponse.error("can not find action: " + action + " by provider: " + providerName);
            }
            return routerAction.invoke(context, request);
        }
        return null;
    }

    private RouteProvider findProvider(String providerName) {
        if (TextUtils.isEmpty(providerName)) {
            throw new IllegalArgumentException();
        }
        if (!mProviders.containsKey(providerName)) {
            throw new IllegalStateException();
        }
        return mProviders.get(providerName);
    }

    void registerProvider(RouteProvider provider) {
        if (provider == null || TextUtils.isEmpty(provider.getName())) {
            return;
        }
        mProviders.put(provider.getName(), provider);
    }
}
