package com.qqdhelper.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.qqdhelper.BaseApplication;
import com.qqdhelper.Constants;
import com.qqdhelper.R;
import com.qqdhelper.bean.BaseBean;
import com.qqdhelper.bean.LoginBean;
import com.qqdhelper.net.HttpHelperPost;
import com.qqdhelper.widgt.CountDownButton;

import java.util.HashMap;

/**
 * 更换手机登录的验证
 *
 * @author sdash
 *         2015-12-10上午11:34:29
 */
public class ValidatePhoneActivity extends AppCompatActivity implements OnClickListener {
    private static final String TAG = ValidatePhoneActivity.class.getSimpleName();
    private CountDownButton btnCode;// 发送验证码按钮
    private EditText etPhoneCode;// 验证码

    private TextView tv_phone_tip;
    private Button btn_sure;
    private int userId;
    private String userName;


    private BaseBean mBaseBean;
    private LoginBean mLoginbean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validate_phone);
        Intent localIntent = getIntent();
        this.userId = localIntent.getIntExtra("userID", 0);
        this.userName = localIntent.getStringExtra("phone");
        initView();
        setText();
        btnCode.onCreate(savedInstanceState);
    }

    /**
     * 初始化组件
     */
    private void initView() {
        btnCode = (CountDownButton) findViewById(R.id.btn_phone_code);
        etPhoneCode = (EditText) findViewById(R.id.et_phone_code);
        tv_phone_tip = (TextView) findViewById(R.id.tv_phone_tip);
        btn_sure = (Button) findViewById(R.id.btn_sure);
        btnCode.setOnClickListener(this);
        btn_sure.setOnClickListener(this);
    }

    public void setText() {
        String tip = getResources().getString(R.string.changephone_validate_tip);
        tip = tip.replaceAll("%s", userName);
        tv_phone_tip.setText(tip);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_phone_code:
                btnCode.setSatr(true);// 启动计时器
                getIdentification();
                break;

            case R.id.btn_sure:
                if (!TextUtils.isEmpty(etPhoneCode.getText().toString())) {
                    HashMap<String, String> param = new HashMap<>();
                    param.put("a", this.userId + "");
                    param.put("b", etPhoneCode.getText().toString());

                    doValidate("http://4.everything4free.com/a/ao", param);
                } else {
                    Toast.makeText(this, "验证码不能为空", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void getIdentification() {
        String str = "http://4.everything4free.com/a/ah";
        HashMap localHashMap = new HashMap();
        localHashMap.put("a", this.userName + "");
        localHashMap.put("b", "5");
        doGetIdentification(str, localHashMap);
    }

    protected <T> void doGetIdentification(String paramString, HashMap<String, String> paramHashMap) {
        HttpHelperPost.Post(ValidatePhoneActivity.this, paramString, paramHashMap, new RequestCallBack() {
            @Override
            public void onSuccess(ResponseInfo responseInfo) {
                Log.e("xx", "responseInfo" + responseInfo);

                Gson a = new Gson();

                mBaseBean = a.fromJson(responseInfo.result.toString(), BaseBean.class);

                Toast.makeText(ValidatePhoneActivity.this, mBaseBean.getHint(), Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onFailure(HttpException e, String s) {
                Log.e("xx", s);
            }
        }, null);
    }

    protected <T> void doValidate(String paramString, HashMap<String, String> paramHashMap) {
        HttpHelperPost.Post(ValidatePhoneActivity.this, paramString, paramHashMap, new RequestCallBack() {
            @Override
            public void onSuccess(ResponseInfo responseInfo) {
                Log.e("xx", "responseInfo" + responseInfo);

                Gson a = new Gson();
                mLoginbean = a.fromJson(responseInfo.result.toString(), LoginBean.class);
                System.out.println("code：" + mLoginbean.getCode());
                if (mLoginbean.getCode() == 0) {
                    System.out.println("验证返回数据：" + mLoginbean.toString());
                    Toast.makeText(ValidatePhoneActivity.this, "验证成功", Toast.LENGTH_SHORT).show();
                    BaseApplication.getApplication().saveUserInfo(mLoginbean);

                    Intent intent = new Intent();
                    /*  设置Intent对象的action属性  */
                    intent.setAction(Constants.QQDACTION);
                    /* 为Intent对象添加附加信息 */
                    intent.putExtra("msg", "发送验证广播测试成功.....");
                    /* 发布广播 */
                    sendBroadcast(intent);

                    System.out.println("验证getLoginInt：" + BaseApplication.getApplication().getLogin_Int(Constants.USER_B));
                    System.out.println("验证getLoginString：" + BaseApplication.getApplication().getLogin_String(Constants.USER_A));
                } else {
                    Toast.makeText(ValidatePhoneActivity.this, mLoginbean.getHint(), Toast.LENGTH_SHORT).show();
                    System.out.println("验证返回数据：" + mLoginbean.toString());
                }

            }

            @Override
            public void onFailure(HttpException e, String s) {
                Log.e("xx", s);
            }
        }, null);
    }
}
