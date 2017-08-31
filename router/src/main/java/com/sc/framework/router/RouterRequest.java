package com.sc.framework.router;

import android.os.Parcel;
import android.os.Parcelable;

import com.sc.framework.router.cache.CacheStrategy;

/**
 * @author ShamsChu
 * @Date 17/5/10 下午3:51
 */
public class RouterRequest<T> implements Parcelable {

    private volatile int hashCode = 0;

    private String mProcess;

    private String mProvider;

    private String mAction;

    private CacheStrategy mCacheStrategy;

    private T mParameter;

    private RouterRequest(Builder<T> builder) {
        mProcess = builder.mProcess;
        mProvider = builder.mProvider;
        mAction = builder.mAction;
        mParameter = builder.mParameter;
        mCacheStrategy = builder.mCacheStrategy;
    }

    protected RouterRequest(Parcel in) {
        mProcess = in.readString();
        mProvider = in.readString();
        mAction = in.readString();
        mParameter = in.readParcelable(this.getClass().getClassLoader());
    }

    public static final Creator<RouterRequest> CREATOR = new Creator<RouterRequest>() {
        @Override
        public RouterRequest createFromParcel(Parcel in) {
            return new RouterRequest(in);
        }

        @Override
        public RouterRequest[] newArray(int size) {
            return new RouterRequest[size];
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
        dest.writeParcelable((Parcelable) mParameter, flags);
    }

    public static class Builder<T> {

        private String mProcess;
        private String mProvider;
        private String mAction;
        private T mParameter;
        private CacheStrategy mCacheStrategy = CacheStrategy.NONE;

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

        public Builder<T> cacheStrategy(CacheStrategy cacheStrategy) {
            mCacheStrategy = cacheStrategy;
            return this;
        }

        public RouterRequest<T> build() {
            return new RouterRequest<>(this);
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

    public CacheStrategy getCacheStrategy() {
        return mCacheStrategy;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof RouterRequest)) {
            return false;
        }
        RouterRequest request = (RouterRequest) obj;
        String process = request.getProcess();
        String provider = request.getProvider();
        String action = request.getAction();
        return mProcess.equals(process) && mProvider.equals(provider) && mAction.equals(action);
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
