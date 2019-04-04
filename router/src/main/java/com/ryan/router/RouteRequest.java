package com.ryan.router;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Ryan
 * @Date 17/5/10 下午3:51
 */
public class RouteRequest<T> implements Parcelable {

    private volatile int hashCode = 0;

    private String mProcess;
    private String mProvider;
    private String mAction;
    // 缓存策略
    private MemoryCacheStrategy mCacheStrategy;
    // 请求参数
    private T mParameter;
    // 只在本地进程寻找
    private boolean inJustRouteLocal;

    protected RouteRequest(Builder<T> builder) {
        mProcess = builder.mProcess;
        mProvider = builder.mProvider;
        mAction = builder.mAction;
        mParameter = builder.mParameter;
        mCacheStrategy = builder.mCacheStrategy;
        inJustRouteLocal = builder.inJustRouteLocal;
    }

    @SuppressWarnings("unchecked")
    protected RouteRequest(Parcel in) {
        mProcess = in.readString();
        mProvider = in.readString();
        mAction = in.readString();
        mParameter = (T) in.readValue(this.getClass().getClassLoader());
    }

    public static final Creator<RouteRequest> CREATOR = new Creator<RouteRequest>() {
        @Override
        public RouteRequest createFromParcel(Parcel in) {
            return new RouteRequest(in);
        }

        @Override
        public RouteRequest[] newArray(int size) {
            return new RouteRequest[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mProcess);
        dest.writeString(mProvider);
        dest.writeString(mAction);
        dest.writeValue(mParameter);
    }

    public static class Builder<T> {

        private String mProcess;
        private String mProvider;
        private String mAction;
        private T mParameter;
        private MemoryCacheStrategy mCacheStrategy = MemoryCacheStrategy.NONE;
        private boolean inJustRouteLocal;

        public Builder<T> process(String process) {
            mProcess = process;
            return this;
        }

        public Builder<T> provider(String provider) {
            mProvider = provider;
            return this;
        }

        public Builder<T> action(String action) {
            mAction = action;
            return this;
        }

        public Builder<T> parameter(T parameter) {
            mParameter = parameter;
            return this;
        }

        public Builder<T> cacheStrategy(MemoryCacheStrategy cacheStrategy) {
            mCacheStrategy = cacheStrategy;
            return this;
        }

        public Builder<T> inJustRouteLocal(boolean inJustLocal) {
            this.inJustRouteLocal = inJustLocal;
            return this;
        }

        public RouteRequest<T> build() {
            return new RouteRequest<>(this);
        }

    }

    public String getAction() {
        return mAction;
    }

    public String getProvider() {
        return mProvider;
    }

    public String getProcess() {
        return mProcess;
    }

    public T getParameter() {
        return mParameter;
    }

    public MemoryCacheStrategy getCacheStrategy() {
        return mCacheStrategy;
    }

    public boolean isInJustRouteLocal() {
        return inJustRouteLocal;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof RouteRequest)) {
            return false;
        }
        RouteRequest request = (RouteRequest) obj;
        String process = request.getProcess();
        String provider = request.getProvider();
        String action = request.getAction();
        return mProcess.equals(process) && mProvider.equals(provider) && mAction.equals(action);
    }

    public void setInJustRouteLocal(boolean inJustRouteLocal) {
        this.inJustRouteLocal = inJustRouteLocal;
    }

    @Override
    public int hashCode() {
        int result = hashCode;
        if (result == 0) {
            result = mProcess.hashCode() + mProvider.hashCode() + mAction.hashCode();
            hashCode = result;
        }
        return result;
    }

}
