package com.sc.sample.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author ShamsChu
 * @Date 17/8/28 下午6:49
 */
public class TestResult implements Parcelable {

    private String rtnMessage;

    public TestResult() {

    }

    protected TestResult(Parcel in) {
        rtnMessage = in.readString();
    }

    public static final Creator<TestResult> CREATOR = new Creator<TestResult>() {
        @Override
        public TestResult createFromParcel(Parcel in) {
            return new TestResult(in);
        }

        @Override
        public TestResult[] newArray(int size) {
            return new TestResult[size];
        }
    };

    public String getRtnMessage() {
        return rtnMessage;
    }

    public void setRtnMessage(String rtnMessage) {
        this.rtnMessage = rtnMessage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(rtnMessage);
    }
}
