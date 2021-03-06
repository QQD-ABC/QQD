package com.qqdhelper.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.qqdhelper.BaseApplication;
import com.qqdhelper.Constants;
import com.qqdhelper.handler.AutoExchangeProduct;
import com.qqdhelper.handler.QueryProduct;

import java.util.List;

/**
 * 广播接收器 ，响应发送广播的操作
 * Created by sdash on 2016/1/14.
 */
public class QQDBroadcastReceiver extends BroadcastReceiver {

    /* 覆写该方法，对广播事件执行响应的动作  */
    public void onReceive(Context context, Intent intent) {

        /* 获取Intent对象中的数据 */
        String msg = intent.getStringExtra("msg");
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();

        List<String> keys = intent.getStringArrayListExtra(Constants.INTENT_query_list);

        int fv = intent.getIntExtra(Constants.INTENT_FV, 0);

        int pastDay = intent.getIntExtra(Constants.INTENT_NEW, 0);

        boolean auto_flag = intent.getBooleanExtra(Constants.INTENT_AUTO,false);

        if (auto_flag) {//自动兑换标记
            System.out.println("进入广播，开始准备自动兑换......");
            AutoExchangeProduct.start(context);
            System.out.println("进入广播，开始准备自动兑换......");
        } else {
            System.out.println("进入广播，开始准备查询......");
            if (fv > 0) {
                QueryProduct.start(context, null, fv , pastDay);
            }else {
                BaseApplication.getApplication().initQueryKeys(keys);//初始化查询keys
                QueryProduct.start(context, BaseApplication.getApplication().keys, fv, pastDay);
            }
            System.out.println("查询进程已启动，正在查询中......");
        }
    }
}
