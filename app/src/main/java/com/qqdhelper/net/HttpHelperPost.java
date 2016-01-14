package com.qqdhelper.net;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.qqdhelper.BaseApplication;
import com.qqdhelper.Constants;
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
//        String str2 = SharedPreferencesManager.getInstance(paramContext).getString("mToken");
//        if (!(TextUtils.isEmpty(str2)))
//            ConstantObj.getuuid = str2;
//        while (true) {
//            String str3 = PhoneUitls.GetIMEI(paramContext);
//            if (TextUtils.isEmpty(str3))
//                str3 = "1234567890";
//            BaseApplication localBaseApplication = (BaseApplication) paramContext.getApplicationContext();
//            String str4 = localBaseApplication.getUserId() + "";
//            if (TextUtils.isEmpty(str4))
//                str4 = "0";
//            ConstantObj.language = getLocaleLanguage(paramContext);
        HashMap localHashMap = new HashMap();
        localHashMap.put("zy", "zh");//运行语言
        localHashMap.put("zx", str1);//当前时间
        localHashMap.put("zw", "868568021407369");//机器imei
        localHashMap.put("zv", RandomIp.getRandomIp());//当前网络ip   我们随机ip
        localHashMap.put("zu", "Xiaomi MI 4LTE");//机器型号
        localHashMap.put("zt", "4.4.4");//系统版本
        localHashMap.put("zs", "2");//不明==========
        localHashMap.put("zr", "1.0.55");//不明==========
        localHashMap.put("zq", BaseApplication.getApplication().getLogin_String(Constants.USER_A));//用户id  默认1234
        final String city = BaseApplication.getApplication().getCityCode();
        localHashMap.put("zp", TextUtils.isEmpty(city) ? "10000" : city);//城市 默认10000
        localHashMap.put("zo", "86");//国际区号 默认中国86
        localHashMap.put("zn", "0");//不明==========
        return localHashMap;
//            ConstantObj.getuuid = "1234";
//    }

    }

    public static String getLocaleLanguage(Context paramContext) {
        return paramContext.getResources().getConfiguration().locale.getLanguage();
    }
}
