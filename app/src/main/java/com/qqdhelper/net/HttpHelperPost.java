package com.qqdhelper.net;

import android.content.Context;
import android.util.Log;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

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
        Log.e("xx","访问url：" + paramString + localList.toString().trim().replace(", ", "&").replace("[", "?").replace("]", ""));
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
        localHashMap.put("zy", "zh");
        localHashMap.put("zx", str1);
        localHashMap.put("zw", "868568021407363");
        localHashMap.put("zv", "113.116.62.159");
        localHashMap.put("zu", "Xiaomi MI 4LTE");
        localHashMap.put("zt", "4.4.4");
        localHashMap.put("zs", "2");
        localHashMap.put("zr", "1.0.55");
        localHashMap.put("zq", "1234");
        localHashMap.put("zp", "10000");
        localHashMap.put("zo", "86");
        localHashMap.put("zn", "0");
        return localHashMap;
//            ConstantObj.getuuid = "1234";
//    }

    }

    public static String getLocaleLanguage(Context paramContext) {
        return paramContext.getResources().getConfiguration().locale.getLanguage();
    }
}
