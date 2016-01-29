package com.qqdhelper.net;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.qqdhelper.BaseApplication;
import com.qqdhelper.Constants;
import com.qqdhelper.util.PhoneUitls;
import com.qqdhelper.util.RandomIp;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class HttpHelperPost {

    public static void Post(Context paramContext, String paramString, HashMap<String, String> paramHashMap, RequestCallBack<?> paramRequestCallBack, File[] paramArrayOfFile) {
//        if (!(NetUtils.isNetWorkExist(paramContext))) {
//            paramRequestCallBack.onFailure(null, paramContext.getString(2131165278));
//            return;
//        }
        HttpUtils localHttpUtils = new HttpUtils(60000);
        localHttpUtils.configSoTimeout(45000);
        List localList = z.getSortParams(paramContext, paramHashMap, getDefaultParams(paramContext));
        RequestParams localRequestParams = new RequestParams();
        localRequestParams.addBodyParameter(localList);
        if ((paramArrayOfFile != null) && (paramArrayOfFile.length > 0))
            localRequestParams.addBodyParameter("b", paramArrayOfFile[0]);
        Log.e("xx", "访问url：" + paramString + localList.toString().trim().replace(", ", "&").replace("[", "?").replace("]", ""));
        localHttpUtils.configResponseTextCharset("UTF_8");
        localHttpUtils.send(HttpRequest.HttpMethod.POST, paramString, localRequestParams, paramRequestCallBack);
    }

    public static HashMap<String, String> getDefaultParams(Context paramContext) {
        String str1 = (new Date().getTime()) + "";

        String str3 = PhoneUitls.GetIMEI(paramContext);
        if (TextUtils.isEmpty(str3)) {
            str3 = "1234567890";
        }

        HashMap localHashMap = new HashMap();
        localHashMap.put("zy", getLocaleLanguage(paramContext));//运行语言
        localHashMap.put("zx", str1);//当前时间
        localHashMap.put("zw", str3);//机器imei
        localHashMap.put("zv", RandomIp.getRandomIp());//当前网络ip   我们随机ip
        localHashMap.put("zu", Build.MANUFACTURER + " " + Build.MODEL);//机器型号
        localHashMap.put("zt", Build.VERSION.RELEASE);//系统版本
        localHashMap.put("zs", "2");//设备代码 1：ios 2：Android==========
        localHashMap.put("zr", "2.000");//QQD 程序版本==========
        final String uuid = BaseApplication.getApplication().getLogin_String(Constants.USER_A);
        localHashMap.put("zq", TextUtils.isEmpty(uuid) ? "1234" : uuid);//uuid  默认1234
        final String city = BaseApplication.getApplication().getCityCode();
        localHashMap.put("zp", TextUtils.isEmpty(city) ? "10000" : city);//城市 默认10000
        //localHashMap.put("zp", "121000");//城市 默认10000
        localHashMap.put("zo", "86");//国际区号 默认中国86
        final String userid = BaseApplication.getApplication().getLogin_Int(Constants.USER_B) + "";
        localHashMap.put("zn", TextUtils.isEmpty(userid) ? "0" : userid);//UserId 默认0
        return localHashMap;
    }

    public static String getLocaleLanguage(Context paramContext) {
        return paramContext.getResources().getConfiguration().locale.getLanguage();
    }
}
