package com.qqdhelper.handler;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.qqdhelper.BaseApplication;
import com.qqdhelper.Constant;
import com.qqdhelper.Constants;
import com.qqdhelper.bean.CityData;
import com.qqdhelper.bean.prouder.ProuderItem;
import com.qqdhelper.bean.prouder.ProuderList;
import com.qqdhelper.net.HttpHelperPost;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class QueryProuder implements Runnable {
    private final List<String> mKeys = new ArrayList<>();
    private String current_Time;
    Context mContext;

    public QueryProuder(Context context, List<String> key) {
        mContext = context;
        mKeys.clear();
        if (key != null) {
            mKeys.addAll(key);
        }
    }

    public int getRodm() {
        java.util.Random random = new java.util.Random();// 定义随机类
        int result = random.nextInt(6) + 5;// 返回[5,10)集合中的整数，注意不包括10
        return (result + 1) * 1000;              // +1后，[5,10)集合变为[6,11)集合，满足要求
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
                    BaseApplication.getApplication().setCityCode(dataJson.getJSONObject(i).getString("a"));
                    BaseApplication.getApplication().setCityName(dataJson.getJSONObject(i).getString("b"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                for (final String key : mKeys) {
                    try {
                        Thread.sleep(getRodm());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Date d = new Date();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//年-月-日 时:分:秒:毫秒
                    current_Time = sdf.format(d).toString();
                    System.out.println("当前时间：" + sdf.format(d));
                    HashMap<String, String> param = new HashMap<>();
                    //param.put("a", "71901");
                    param.put("a", BaseApplication.getApplication().getLogin_Int(Constants.USER_B) + "");
                    param.put("c", "1");
                    param.put("d", "15");
                    param.put("e", "0");
                    param.put("f", key);
                    final String city = BaseApplication.getApplication().getCityCode();
                    final String cityName = BaseApplication.getApplication().getCityName();
                    Log.e("xx", "开始查询:" + cityName + "的" + key);
                    HttpHelperPost.Post(mContext, "http://4.everything4free.com/c/ae", param, new RequestCallBack<Object>() {
                        @Override
                        public void onSuccess(ResponseInfo<Object> responseInfo) {
//                            Log.e("xx", city + "responseInfo" + responseInfo);
                            Gson a = new Gson();
                            ProuderList prouderList = a.fromJson(responseInfo.result.toString(), ProuderList.class);
                            Log.e("xx", "查询结果:" + cityName + "的" + key + "===" + responseInfo.result.toString());
                            if (prouderList.getCode() == 0) {
//                          Toast.makeText(mContext, prouderList.getHint(), Toast.LENGTH_SHORT).show();
                                List<ProuderItem> list = prouderList.getA();
                                if (list != null) {
                                    for (ProuderItem prouderItem : list) {
                                        if (prouderItem.getF() > 0) {
                                            SendMail sm = new SendMail();
                                            for (String receicveer : Constant.receiveer) {
                                                boolean city_Flag;
                                                if (prouderItem.getK().toString().indexOf(prouderItem.getK().toString().valueOf('市')) == -1) {
                                                    //字符串中不存在 市
                                                    if (prouderItem.getK().toString().indexOf(prouderItem.getK().toString().valueOf('县')) != -1 || prouderItem.getK().toString().indexOf(prouderItem.getK().toString().valueOf('区')) != -1) {
                                                        city_Flag = true;
                                                    } else {
                                                        city_Flag = false;
                                                    }
                                                } else {
                                                    city_Flag = false;
                                                }

                                                if (city_Flag) {
                                                    sm.sendMails(receicveer, "Teemo提醒您：" + cityName + prouderItem.getK() + "的 " + key + " 有货啦！！！", new StringBuffer(
                                                            "赶快打开QQD，去 <U>" + cityName + prouderItem.getK() + "</U> 兑换 <U>" + key + "</U> FV："+ prouderItem.getB() +"  当前数量：<U>" + prouderItem.getF() + "</U>  数量有限，先到先得！<br>各位加油~  么么哒~<br><p align='right'>Teemo  " + current_Time + "</p>"));
                                                } else {
                                                    sm.sendMails(receicveer, "Teemo提醒您：" + prouderItem.getK() + "的 " + key + " 有货啦！！！", new StringBuffer(
                                                            "赶快打开QQD，去 <U>" + prouderItem.getK() + "</U> 兑换 <U>" + key + "</U> FV："+ prouderItem.getB() +"  当前数量：<U>" + prouderItem.getF() + "</U>  数量有限，先到先得！<br>各位加油~  么么哒~<br><p align='right'>Teemo  " + current_Time + "</p>"));
                                                }
                                            }
                                        }
                                    }
                                }
                            } else {
//                            mBaseBean = a.fromJson(responseInfo.result.toString(), BaseBean.class);
                                Toast.makeText(mContext, prouderList.getHint(), Toast.LENGTH_SHORT).show();
//                                try {
//                                    QueryProuder.this.wait();
//                                } catch (InterruptedException e) {
//                                    e.printStackTrace();
//                                }
                            }
                        }

                        @Override
                        public void onFailure(HttpException error, String msg) {
                            Log.e("xx", msg);
                        }
                    }

                            , null);
                }
            }
            try {
                Thread.sleep(getRodm() * 60);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (mKeys != null && !mKeys.isEmpty()) {
                new Thread(new QueryProuder(mContext, mKeys)).start();
            }
        }
    }
}
