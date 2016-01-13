package com.qqdhelper.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by sdash on 2016/1/13.
 */
public class BaseBean implements Parcelable {

    private int code;
    private String hint;
    private String url;

    protected BaseBean() {}

    protected BaseBean(Parcel paramParcel)
    {
        this.code = paramParcel.readInt();
        this.hint = paramParcel.readString();
        this.url = paramParcel.readString();
    }

    public static final Creator<BaseBean> CREATOR = new Creator<BaseBean>() {
        @Override
        public BaseBean createFromParcel(Parcel in) {
            return new BaseBean(in);
        }

        @Override
        public BaseBean[] newArray(int size) {
            return new BaseBean[size];
        }
    };

    public int describeContents()
    {
        return 0;
    }

    public int getCode()
    {
        return this.code;
    }

    public String getHint()
    {
        return this.hint;
    }

    public String getUrl()
    {
        return this.url;
    }

    public void setCode(int paramInt)
    {
        this.code = paramInt;
    }

    public void setHint(String paramString)
    {
        this.hint = paramString;
    }

    public void setUrl(String paramString)
    {
        this.url = paramString;
    }

    public String toString()
    {
        return "BaseBean{code=" + this.code + ", hint='" + this.hint + '\'' + ", url='" + this.url + '\'' + '}';
    }

    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
        paramParcel.writeInt(this.code);
        paramParcel.writeString(this.hint);
        paramParcel.writeString(this.url);
    }
}
