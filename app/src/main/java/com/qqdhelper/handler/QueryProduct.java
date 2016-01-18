package com.qqdhelper.handler;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.qqdhelper.BaseApplication;
import com.qqdhelper.Constant;
import com.qqdhelper.Constants;
import com.qqdhelper.bean.prouder.ProuderItem;
import com.qqdhelper.bean.prouder.ProuderList;
import com.qqdhelper.callback.SnscityRequestCallBack;
import com.qqdhelper.net.HttpHelperPost;
import com.qqdhelper.net.z;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

public class QueryProduct implements Runnable {
    private static QueryProduct mQueryProduct;
    private static Thread mMainThread;
    private static boolean isRunning = false;

    private final List<String> mKeys = new ArrayList<>();
    private List<ProuderItem> mCitys = new ArrayList<>();

    private String current_Time;
    private Context mContext;

    private static int FV;

    private static int PastDay;

    private Date d;

    public static void start(Context context, final List<String> key, int fv,int pastDay) {

        if (mQueryProduct == null) {
            FV = fv;
            PastDay = pastDay;
            mQueryProduct = new QueryProduct(context, key);
            mMainThread = new Thread(mQueryProduct);
        }
        if (isRunning) {
            mMainThread.interrupt();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    //if (key != null && !key.isEmpty()) {
                    mMainThread = new Thread(mQueryProduct);
                    mMainThread.start();
                    //}
                }
            }, 1500);
        } else {
            //if (key != null && !key.isEmpty())
            mMainThread.start();
        }
    }

    private QueryProduct(Context context, List<String> key) {
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
        isRunning = true;
        List<ProuderItem> citys = mCitys.isEmpty() ? BaseApplication.getApplication().getCityData() : mCitys;
        if (citys != null) {
            for (int i = 0, j = citys.size(); i < j; i++) {//城市==========
                BaseApplication.getApplication().setCityCode(citys.get(i).getA());
                BaseApplication.getApplication().setCityName(citys.get(i).getB());
                if (mKeys.size() > 0) {
                    for (final String key : mKeys) {//关键字===========
                        doQuery(key);//根据 关键词 查所商品
                    }
                } else {
                    doQuery("");//根据 FV数量 查所有商品
                }
            }

            try {
                Thread.sleep(getRodm() * 60);
            } catch (InterruptedException e) {
                e.printStackTrace();
                isRunning = false;
                return;
            }
            start(mContext, mKeys, 0 , 0);
        }
    }

    private void doQuery(final String key) {
        try {
            Thread.sleep(getRodm());
        } catch (InterruptedException e) {
            e.printStackTrace();
            isRunning = false;
            return;
        }
        d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//年-月-日 时:分:秒:毫秒
        current_Time = sdf.format(d).toString();
        System.out.println("当前时间：" + sdf.format(d));
        HashMap<String, String> param = new HashMap<>();
        param.put("a", BaseApplication.getApplication().getLogin_Int(Constants.USER_B) + "");
        param.put("c", "1");
        param.put("d", "15");
        param.put("e", "0");
        param.put("f", key);
        final String city = BaseApplication.getApplication().getCityCode();
        final String cityName = BaseApplication.getApplication().getCityName();
        final ProuderItem cityItem = new ProuderItem();
        cityItem.setA(city);
        cityItem.setB(cityName);
        Log.e("xx", "开始查询:" + cityName + "的" + key);
        Log.e("xx", "Thread:" + Thread.currentThread().getName());
        HttpHelperPost.Post(mContext, "http://4.everything4free.com/c/ae", param, new RequestCallBack<Object>() {
            @Override
            public void onSuccess(ResponseInfo<Object> responseInfo) {
                Gson a = new Gson();
                ProuderList prouderList = a.fromJson(responseInfo.result.toString(), ProuderList.class);
                Log.e("xx", "查询结果:" + cityName + "的" + key + "===" + responseInfo.result.toString());
                if (prouderList.getCode() == 0) {
                    List<ProuderItem> list = prouderList.getA();
                    if (list != null) {
                        for (ProuderItem prouderItem : list) {
                            System.out.println("商家编号：" + prouderItem.getP());
                            dealMerchantsNo(prouderItem.getP());
                            if (!mCitys.contains(cityItem)) {
                                mCitys.add(cityItem);
                            }

                            String temp_key = key;

                            if (TextUtils.isEmpty(temp_key)) {
                                temp_key = "少于FV" + FV + "的商品";
                            }
                            if (FV > 0) {
                                if (prouderItem.getF() > 0 && Integer.parseInt(prouderItem.getB()) <= FV && Integer.parseInt(prouderItem.getB()) >= 1000) {
                                    if (PastDay < 0) {//判断是否为新店
                                        if (Integer.parseInt(dealMerchantsNo(prouderItem.getP())) >= Integer.parseInt(getPastDay(d, PastDay))) {
                                            SendNewMail(cityName, temp_key, prouderItem);
                                        }
                                    } else {
                                        SendMail(cityName, temp_key, prouderItem);
                                    }
                                }
                            } else {
                                if (prouderItem.getF() > 0) {
                                    getStoreExcLimit(cityName, temp_key, prouderItem);//查询商家兑换限额，完成自动兑换
                                    if (PastDay < 0) {//判断是否为新店
                                        if (Integer.parseInt(dealMerchantsNo(prouderItem.getP())) >= Integer.parseInt(getPastDay(d, PastDay))) {
                                            SendNewMail(cityName, temp_key, prouderItem);
                                        }
                                    } else {
                                        SendMail(cityName, temp_key, prouderItem);
                                    }
                                }
                            }
                        }
                    }
                } else {
                    Log.e("xx", "查询结果:" + prouderList.getHint());
                    Toast.makeText(mContext, prouderList.getHint(), Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                Log.e("xx", msg);
            }
        }, null);
    }

    private void getStoreExcLimit(final String cityName, final String temp_key,final ProuderItem prouderItem){

        HashMap<String, String> param = new HashMap<>();
        param.put("a", BaseApplication.getApplication().getLogin_Int(Constants.USER_B) + "");
        param.put("b", prouderItem.getJ() + "");
        HttpHelperPost.Post(mContext, "http://4.everything4free.com/c/ln", param, new RequestCallBack<Object>() {
            @Override
            public void onSuccess(ResponseInfo<Object> responseInfo) {
                Log.e("xx", "可兑换结果:" + responseInfo.result.toString());
                Gson ma = new Gson();
                ProuderItem prouderItem1 = ma.fromJson(responseInfo.result.toString(), ProuderItem.class);
                if(Integer.parseInt(prouderItem1.getB()) > 0 && (Integer.parseInt(prouderItem1.getB()) - Integer.parseInt(prouderItem.getB())) >= 0){
                    autoExchange(cityName,temp_key,prouderItem);//执行自动兑换程序
                } else {
                    Toast.makeText(mContext,"商家每天最多可接受10w FV",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                Log.e("xx", msg);
            }
        }, null);
    }

    private void autoExchange(final String mcityName, final String mcityKey, final ProuderItem mprouderItem) {
        if (TextUtils.isEmpty(BaseApplication.PWD)) {
            Log.e("xx", "没有兑换密码");
            return;
        }
        if (!TextUtils.isEmpty(BaseApplication.MSG) && !mprouderItem.getA().contains(BaseApplication.MSG)) {
            Log.e("xx", mprouderItem.getA() + "-不包含关键字-" + BaseApplication.MSG);
            return;
        }
        HashMap<String, String> param = new HashMap<>();
        param.put("a", BaseApplication.getApplication().getLogin_Int(Constants.USER_B) + "");
        param.put("b", mprouderItem.getH() + "");
        param.put("c", "1");
        param.put("d", mprouderItem.getJ() + "");
        param.put("e", z.getRSA(mContext, BaseApplication.PWD));
        HttpHelperPost.Post(mContext, "http://4.everything4free.com/c/ag", param, new RequestCallBack<Object>() {
            @Override
            public void onSuccess(ResponseInfo<Object> responseInfo) {
                Log.e("xx", "兑换结果:" + responseInfo.result.toString());
                autoES_SendMail(mcityName,mcityKey,mprouderItem);
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                Log.e("xx", msg);
            }
        }, null);
    }

    private void SendMail(String cityName, String temp_key, ProuderItem prouderItem) {
        SendMail sm = new SendMail();
        for (String receicveer : Constant.receiveer) {
            System.out.println("邮件发送程序开始执行......");
            sm.sendMails(receicveer, "Teemo提醒您：" + cityName + " 的 " + prouderItem.getK() + " 的 " + temp_key + " 有货啦！！！", new StringBuffer(
                    "赶快打开QQD，去 <U>" + cityName + "</U> 的 <U>" + prouderItem.getK() + "</U> (开店时间：<U>" + dealDate(dealMerchantsNo(prouderItem.getP())) + "</U>) 兑换 <U>" + prouderItem.getA() + "</U>， FV：<U>" + prouderItem.getB() + "</U>，  当前数量：<U>" + prouderItem.getF() + "</U>。  数量有限，先兑先得！<br>各位加油~  么么哒~<br><p align='right'>Teemo  " + current_Time + "</p>"));
            System.out.println("邮件发送程序执行完毕！");
        }
    }

    private void SendNewMail(String cityName, String temp_key, ProuderItem prouderItem) {
        SendMail sm = new SendMail();
        for (String receicveer : Constant.receiveer) {
            System.out.println("邮件发送程序开始执行......");
            sm.sendMails(receicveer, "新店提醒~~ Teemo提醒您：" + cityName + " 的 " + prouderItem.getK() + " 的 " + temp_key + " 有货啦！！！", new StringBuffer(
                    "新店号外！新店号外！新店号外！<br>赶快打开QQD，去 <U>" + cityName + "</U> 的 <U>" + prouderItem.getK() + "</U> (开店时间：<U>" + dealDate(dealMerchantsNo(prouderItem.getP())) + "</U>) 兑换 <U>" + prouderItem.getA() + "</U>， FV：<U>" + prouderItem.getB() + "</U>，  当前数量：<U>" + prouderItem.getF() + "</U>。  新店开张，数量有限，先兑先得！<br>各位加油~  么么哒~<br><p align='right'>Teemo  " + current_Time + "</p>"));
            System.out.println("邮件发送程序执行完毕！");
        }
    }

    private void autoES_SendMail(String cityName, String temp_key, ProuderItem prouderItem) {
        SendMail sm = new SendMail();
        System.out.println("自动兑换成功邮件发送程序开始执行......");
        sm.sendMails("13235809610@163.com", "Teemo提醒您：" + cityName + " 的 " + prouderItem.getK() + " 的 " + temp_key + " 自动兑换成功！！！", new StringBuffer(
                "您在QQD里的 <U>" + cityName + "</U> 的 <U>" + prouderItem.getK() + "</U> 自动兑换 <U>" + prouderItem.getA() + "</U> 处理成功。<br>赶快打开QQD与商家联系发货事宜！<br>各位加油~  么么哒~<br><p align='right'>Teemo  " + current_Time + "</p>"));
        System.out.println("自动兑换成功邮件发送程序执行完毕！");
    }

    public String getPastDay(Date d,int key) {
        GregorianCalendar gc =new GregorianCalendar();
        gc.setTime(d);
        gc.add(Calendar.DAY_OF_MONTH, key);
        gc.set(gc.get(Calendar.YEAR), gc.get(Calendar.MONTH), gc.get(Calendar.DATE));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");//年月日
        return sdf.format(gc.getTime());
    }

    public String dealMerchantsNo(String no) {
        if (TextUtils.isEmpty(no)) {
              return "";
        }
        if (no.contains("QQD")) {
             no = no.replace("QQD","").substring(0,8);
        }
        System.out.println("商家开店时间：" + no);
        return no;
    }

    /**
     * yyyyMMdd To yyyy年MM月dd日
     * */
    private static String dealDate(String date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy年MM月dd日");
        Date date2 = null;
        try {
            date2 = sdf.parse(date);
            date = sdf2.format(date2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
}


