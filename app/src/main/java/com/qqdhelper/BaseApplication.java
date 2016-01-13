package com.qqdhelper;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.qqdhelper.bean.LoginBean;

import java.util.Map;

/**
 * Created by sdash on 2016/1/13.
 */
public class BaseApplication extends Application {

    public static BaseApplication appContext;
    private String UUID;
    private int userId;
    private String userPhone;
    private String cityCode;
    private String cityName;

    private SharedPreferences mSharedPreferences;//保存本地资料

    public static BaseApplication getApplication() {
        return appContext;
    }

    public void onCreate() {
        super.onCreate();
        appContext = this;
        mSharedPreferences = appContext.getSharedPreferences(Constants.USERINFO, Context.MODE_PRIVATE);
    }

    // 用于按钮存放倒计时时间
    public static Map<String, Long> countDown_map;

    public static Map<String, Long> getCountDown_map() {
        return countDown_map;
    }

    public static void setCountDown_map(Map<String, Long> countDown_map) {
        BaseApplication.countDown_map = countDown_map;
    }

    /**
     * 登录保存数据
     */
    public void saveUserInfo(LoginBean mLoginData) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(Constants.USER_A, mLoginData.getA());
        editor.putInt(Constants.USER_B, mLoginData.getB());
        editor.putString(Constants.USER_C, mLoginData.getC());
        editor.putInt(Constants.USER_D, mLoginData.getD());
        editor.putString(Constants.USER_E, mLoginData.getE());
        editor.putString(Constants.USER_F, mLoginData.getF());
        editor.putInt(Constants.USER_G, mLoginData.getG());
        editor.putString(Constants.USER_H, mLoginData.getH());
        editor.putString(Constants.USER_I, mLoginData.getI());
        editor.putString(Constants.USER_J, mLoginData.getJ());
        editor.putString(Constants.USER_K, mLoginData.getK());
        editor.putString(Constants.USER_L, mLoginData.getL());
        editor.putString(Constants.USER_M, mLoginData.getM());
        editor.putString(Constants.USER_N, mLoginData.getN());
        editor.putString(Constants.USER_O, mLoginData.getO());
        editor.commit();
    }

    public int getLogin_Int(String param) {
        return mSharedPreferences.getInt(param, 0);
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getLogin_String(String param) {
        return mSharedPreferences.getString(param, "1234");
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

    public String getUUID() {
        return this.UUID;
    }

    public int getUserId() {
        return this.userId;
    }

    public String getUserPhone() {
        return this.userPhone;
    }
}
