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

import java.util.HashMap;
import java.util.List;

public class QueryProuder implements Runnable {
    private final String mKey;
    Context mContext;

    public QueryProuder(Context context, String key) {
        mContext = context;
        mKey = key;
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
                    BaseApplication.getApplication().setCityCode(dataJson.getJSONObject(i).getString("a"));
                    BaseApplication.getApplication().setCityName(dataJson.getJSONObject(i).getString("b"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                HashMap<String, String> param = new HashMap<>();
                param.put("a", "71901");
                param.put("c", "1");
                param.put("d", "15");
                param.put("e", "0");
                param.put("f", mKey);
                final String city = BaseApplication.getApplication().getCityCode();
                final String cityName = BaseApplication.getApplication().getCityName();
                HttpHelperPost.Post(mContext, "http://4.everything4free.com/c/ae", param, new RequestCallBack<Object>() {
                    @Override
                    public void onSuccess(ResponseInfo<Object> responseInfo) {
                        Log.e("xx", city + "responseInfo" + responseInfo);
                        Gson a = new Gson();
                        ProuderList prouderList = a.fromJson(responseInfo.result.toString(), ProuderList.class);
                        if (prouderList.getCode() == 0) {
//                            System.out.println("登录返回数据：" + mLoginbean.toString());
//                            Toast.makeText(mContext, prouderList.getHint(), Toast.LENGTH_SHORT).show();
                            List<ProuderItem> list = prouderList.getA();
                            if (list != null) {
                                for (ProuderItem prouderItem : list) {
                                    if (prouderItem.getF() > 0) {
                                        SendMail sm = new SendMail();
                                        for (String receicveer : Constant.receiveer) {
                                            sm.sendMails(receicveer, "啊啊啊啊去抢吧~" + cityName + prouderItem.getK() + "的" + mKey + "商品的数量:" + prouderItem.getF(), new StringBuffer(
                                                    "各位加油~  么么哒~"));
                                        }
                                    }
                                }
                            }
                            System.out.println("登录getLoginInt：" + BaseApplication.getApplication().getLogin_Int(Constants.USER_B));
                            System.out.println("登录getLoginString：" + BaseApplication.getApplication().getLogin_String(Constants.USER_A));
                        } else {
//                            mBaseBean = a.fromJson(responseInfo.result.toString(), BaseBean.class);
                            Toast.makeText(mContext, prouderList.getHint(), Toast.LENGTH_SHORT).show();
                            try {
                                QueryProuder.this.wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
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
    }
}
