package com.qqdhelper.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.telephony.TelephonyManager;

/**
 * 获取设备信息 工具
 * Created by sdash on 2016/1/14.
 */
public class PhoneUitls {
    /**
     * get IMEI of phone 手机唯一标识
     *
     * @return
     */
    public static String GetIMEI(Context context) {
        TelephonyManager manager = (TelephonyManager) context
                .getSystemService(Activity.TELEPHONY_SERVICE);
        // check if has the permission
        if (PackageManager.PERMISSION_GRANTED == context.getPackageManager().checkPermission(Manifest.permission.READ_PHONE_STATE, context.getPackageName())) {
            return manager.getDeviceId();
        } else {
            return null;
        }
    }
}
