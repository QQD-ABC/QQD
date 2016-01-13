package com.qqdhelper;

import android.app.Application;

/**
 * Created by sdash on 2016/1/13.
 */
public class BaseApplication extends  Application{

    public static BaseApplication appContext;
    private String UUID;
    private int userId;
    private String userPhone;

    public static BaseApplication getApplication() {
        return appContext;
    }

    public String getUUID() {
        return this.UUID;
    }

    public int getUserId() {
        return this.userId;
    }

    public String getUserPhone() {
        return this.userPhone;
    }

    public void onCreate() {
        super.onCreate();
        appContext = this;


    }

    public void setUUID(String paramString) {
        this.UUID = paramString;
    }

    public void setUserId(int paramInt) {
        this.userId = paramInt;
    }

    public void setUserPhone(String paramString) {
        this.userPhone = paramString;
    }
}
