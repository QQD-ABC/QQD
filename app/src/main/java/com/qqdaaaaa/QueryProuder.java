package com.qqdaaaaa;

import android.content.Context;
import android.util.Log;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class QueryProuder implements Runnable {
    Context mContext;

    public QueryProuder(Context context) {
        mContext = context;
    }

    public int getRodm() {
        java.util.Random random = new java.util.Random();// 定义随机类
        int result = random.nextInt(6);// 返回[0,10)集合中的整数，注意不包括10
        return (result + 10) * 1000;              // +1后，[0,10)集合变为[1,11)集合，满足要求
    }

    @Override
    public void run() {
        JSONArray dataJson = null;
        try {
            JSONObject jsonObject = new JSONObject(CityData.city);
            dataJson = (JSONArray) jsonObject.get("a");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (dataJson != null) {
            for (int i = 0, j = dataJson.length(); i < j; i++) {
                try {
                    Thread.sleep(getRodm());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    MainActivity.cityCode = dataJson.getJSONObject(i).getString("a");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                HashMap<String, String> param = new HashMap<>();
                param.put("a", "71901");
                param.put("c", "1");
                param.put("d", "15");
                param.put("e", "0");
                param.put("f", "iPad");
                final String city = MainActivity.cityCode;
                HttpHelperPost.Post(mContext, "http://4.everything4free.com/c/ae", param, new RequestCallBack<Object>() {
                    @Override
                    public void onSuccess(ResponseInfo<Object> responseInfo) {
                        Log.e("xx", city + "responseInfo" + responseInfo);
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        Log.e("xx", msg);
                    }
                }, null);
            }
        }
    }
}
