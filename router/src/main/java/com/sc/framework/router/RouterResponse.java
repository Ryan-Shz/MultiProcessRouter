package com.sc.framework.router;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import java.util.concurrent.Future;

/**
 * @author ShamsChu
 * @Date 17/5/10 下午3:55
 */
public class RouterResponse<T> implements Parcelable {

    public static final int CODE_SUCCESS = 0;
    public static final int CODE_ERROR = 1;
    public static final int CODE_PROVIDER_NO_FOUND = 2;
    public static final int CODE_ACTION_NO_FOUND = 3;
    public static final int CODE_PROCESS_NO_RUNNING = 4;

    private int code;
    private T result;
    private String error;
    /**
     * used by local
     */
    private Future<T> future;

    private RouterResponse(Builder<T> builder) {
        this.code = builder.code;
        this.result = builder.result;
        this.error = builder.error;
        this.future = builder.future;
    }

    protected RouterResponse(Parcel in) {
        this.code = in.readInt();
        this.error = in.readString();
        try {
            String targetClass = in.readString();
            if (!TextUtils.isEmpty(targetClass)) {
                this.result = (T) in.readValue(Class.forName(targetClass).getClassLoader());
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static class Builder<T> {

        private int code;
        private T result;
        private String error;
        /**
         * used by local
         */
        private Future<T> future;

        Builder<T> code(int code) {
            this.code = code;
            return this;
        }

        public Builder<T> result(T result) {
            this.result = result;
            return this;
        }

        Builder<T> error(String error) {
            this.error = error;
            return this;
        }

        Builder<T> future(Future<T> future) {
            this.future = future;
            return this;
        }

        public RouterResponse<T> build() {
            return new RouterResponse<>(this);
        }

    }

    public static final Creator<RouterResponse> CREATOR = new Creator<RouterResponse>() {
        @Override
        public RouterResponse createFromParcel(Parcel in) {
            return new RouterResponse(in);
        }

        @Override
        public RouterResponse[] newArray(int size) {
            return new RouterResponse[size];
        }
    };

    public void setResult(T result) {
        this.result = result;
    }

    public void setError(String error) {
        this.error = error;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getResult() {
        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(code);
        dest.writeString(error);
        if (result != null) {
            String aClassName = result.getClass().getName();
            dest.writeString(aClassName);
            dest.writeValue(result);
        }
    }

    public String getError() {
        return error;
    }

    public Future<T> getFuture() {
        return future;
    }

    public boolean isSuccess() {
        return code == CODE_SUCCESS;
    }

    public int getCode() {
        return code;
    }

}
