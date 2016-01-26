package com.qqdhelper.handler;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.qqdhelper.BaseApplication;
import com.qqdhelper.Constants;
import com.qqdhelper.bean.BaseBean;
import com.qqdhelper.bean.prouder.ProuderItem;
import com.qqdhelper.net.HttpHelperPost;
import com.qqdhelper.net.z;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * 自动兑换
 * 定点兑换
 * */
public class AutoExchangeProduct implements Runnable {
    private static AutoExchangeProduct mAutoExchangeProduct;
    private static Thread mMainThread;
    private static boolean isRunning = false;


    private String current_Time;
    private Context mContext;

    private int count = 0;

    private Date d;

    private String pro_Id = "9579";

    public static void start(Context context) {

        if(mAutoExchangeProduct == null){
            mAutoExchangeProduct = new AutoExchangeProduct(context);
            mMainThread = new Thread(mAutoExchangeProduct);
        }

        if (isRunning) {
            mMainThread.interrupt();
            new Handler(context.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    mMainThread = new Thread(mAutoExchangeProduct);
                    mMainThread.start();
                }
            }, 1000);//原值1500
        } else {
            mMainThread.start();
        }
    }

    private AutoExchangeProduct(Context context) {
        mContext = context;
    }

    @Override
    public void run() {
        isRunning = true;

        d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//年-月-日 时:分:秒:毫秒
        current_Time = sdf.format(d).toString();
        System.out.println("当前时间：" + sdf.format(d));
        autoExchange();//查询商家兑换限额，完成自动兑换

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
            isRunning = false;
            return;
        }
        start(mContext);
    }


    private void autoExchange() {
        if (TextUtils.isEmpty(BaseApplication.PAY_PWD)) {
            Log.e("xx", "没有兑换密码");
            return;
        }

        HashMap<String, String> param = new HashMap<>();
        param.put("a", BaseApplication.getApplication().getLogin_Int(Constants.USER_B) + "");
        param.put("b", pro_Id);//商品代号
        param.put("c", "1");
        param.put("d", "363");//商家代号
        param.put("e", z.getRSA(mContext, BaseApplication.PAY_PWD));
        System.out.println("自动完成兑换一次");
        HttpHelperPost.Post(mContext, "http://4.everything4free.com/c/ag", param, new RequestCallBack<Object>() {
            @Override
            public void onSuccess(ResponseInfo<Object> responseInfo) {
                Log.e("xx", "兑换结果:" + responseInfo.result.toString());
                Gson ma = new Gson();
                BaseBean baseBean1 = ma.fromJson(responseInfo.result.toString(), BaseBean.class);
                if (baseBean1.getCode() == 0) {
                    if (pro_Id.equals("9579")) {
                        ProuderItem a = new ProuderItem();
                        a.setA("苹果 6sp 128g");
                        a.setK("古塔区姜涛通讯器材经销处");
                        autoES_SendMail("锦州市", "苹果 6sp 128g", a);
                    } else {
                        ProuderItem b = new ProuderItem();
                        b.setA("苹果 ipad pro 128g");
                        b.setK("古塔区姜涛通讯器材经销处");
                        autoES_SendMail("锦州市", "苹果 ipad pro 128g", b);
                    }
                    pro_Id = "9567";
                    System.out.println("ID 改变一次:" + pro_Id);
                } else {
                    count++;
                    Toast.makeText(mContext,"自动兑换失败"+ count +"次,"+responseInfo.result.toString() ,Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                Log.e("xx", msg);
                count++;
                Toast.makeText(mContext,"失败"+ count +"次,"+msg ,Toast.LENGTH_SHORT).show();
            }
        }, null);
    }

    private void autoES_SendMail(String cityName, String temp_key, ProuderItem prouderItem) {
        SendMail sm = new SendMail();
        System.out.println("自动兑换成功邮件发送程序开始执行......");
        sm.sendMails("13235809610@163.com", "Teemo提醒您：" + cityName + " 的 " + prouderItem.getK() + " 的 " + temp_key + " 自动兑换成功！！！", new StringBuffer(
                "您在QQD里的 <U>" + cityName + "</U> 的 <U>" + prouderItem.getK() + "</U> 自动兑换 <U>" + prouderItem.getA() + "</U> 处理成功。<br>赶快打开QQD与商家联系发货事宜！<br>各位加油~  么么哒~<br><p align='right'>Teemo  " + current_Time + "</p>"));
        System.out.println("自动兑换成功邮件发送程序执行完毕！");
    }
}


