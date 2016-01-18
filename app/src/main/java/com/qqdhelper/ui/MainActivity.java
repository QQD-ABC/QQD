package com.qqdhelper.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.qqdhelper.Constant;
import com.qqdhelper.R;
import com.qqdhelper.handler.SendMail;
import com.qqdhelper.net.z;

public class MainActivity extends AppCompatActivity {

    Button btn_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        btn_login = (Button) findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
//        HashMap<String, String> param = new HashMap<>();
//        param.put("a", "71901");
//        param.put("c", "1");
//        param.put("d", "15");
//        param.put("e", "0");
//        param.put("f", "iPad");
//        HttpHelperPost.Post(this, "http://4.everything4free.com/c/ae", param, new RequestCallBack<Object>() {
//            @Override
//            public void onSuccess(ResponseInfo<Object> responseInfo) {
//                Log.e("xx", "responseInfo" + responseInfo);
//            }
//
//            @Override
//            public void onFailure(HttpException error, String msg) {
//                Log.e("xx", msg);
//            }
//        }, null);
        z.getRSA(this, "a123456");
        SendMail sm = new SendMail();
        for (String receicveer : Constant.receiveer) {
            System.out.println("邮件发送程序开始执行......");
//            BaseApplication.getApplication().get
            sm.sendMails(receicveer, "Teemo提醒您：开始监听~", new StringBuffer("asd"));
            System.out.println("邮件发送程序执行完毕！");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
