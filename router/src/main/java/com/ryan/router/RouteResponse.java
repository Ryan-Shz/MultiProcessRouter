package com.ryan.router;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

/**
 * @author Ryan
 * @Date 17/5/10 下午3:55
 */
public class RouteResponse<T> implements Parcelable {

    private static final int CODE_SUCCESS = 0;
    public static final int CODE_ERROR = 1;

    private int code;
    private T result;
    private String error;

    public static RouteResponse error(String error) {
        ERROR.error = error;
        return ERROR;
    }

    private static final RouteResponse ERROR = new Builder().code(CODE_ERROR).build();

    private RouteResponse(Builder<T> builder) {
        this.code = builder.code;
        this.result = builder.result;
        this.error = builder.error;
    }

    @SuppressWarnings("unchecked")
    protected RouteResponse(Parcel in) {
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

        public Builder<T> code(int code) {
            this.code = code;
            return this;
        }

        public Builder<T> result(T result) {
            this.result = result;
            return this;
        }

        public Builder<T> error(String error) {
            this.error = error;
            return this;
        }

        public RouteResponse<T> build() {
            return new RouteResponse<>(this);
        }

    }

    public static final Creator<RouteResponse> CREATOR = new Creator<RouteResponse>() {
        @Override
        public RouteResponse createFromParcel(Parcel in) {
            return new RouteResponse(in);
        }

        @Override
        public RouteResponse[] newArray(int size) {
            return new RouteResponse[size];
        }
    };

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

    public boolean isSuccess() {
        return code == CODE_SUCCESS;
    }

    public int getCode() {
        return code;
    }

}
