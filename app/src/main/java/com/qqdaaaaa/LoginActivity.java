package com.qqdaaaaa;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import java.util.HashMap;


/**
 * A login screen that offers login via phone/password.
 */
public class LoginActivity extends AppCompatActivity {

    // UI references.
    private AutoCompleteTextView mphoneView;
    private EditText mPasswordView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mphoneView = (AutoCompleteTextView) findViewById(R.id.phone);

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mphoneSignInButton = (Button) findViewById(R.id.phone_sign_in_button);
        mphoneSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid phone, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {

        // Reset errors.
        mphoneView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String phone = mphoneView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid phone address.
        if (TextUtils.isEmpty(phone)) {
            mphoneView.setError(getString(R.string.error_field_required));
            focusView = mphoneView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            HashMap<String, String> param = new HashMap<>();
//            param.put("a", phone);
//            param.put("b", z.getRSA(this, password));
//            System.out.println("手机:" + phone);
//            System.out.println("密码:" + z.getRSA(this, password));

            param.put("a", "13235809610");
            param.put("b", z.getRSA(this, z.getRSA(this, "a1234567")));
            System.out.println("手机:" + "13235809610");
            System.out.println("密码:" + z.getRSA(this, "a1234567"));

            doPost("http://4.everything4free.com/a/aa", param);
//            HttpHelperPost.Post(this, "http://4.everything4free.com/a/aa", param, new RequestCallBack<Object>() {
//                @Override
//                public void onSuccess(ResponseInfo<Object> responseInfo) {
//                    Log.e("xx", "responseInfo" + responseInfo);
//                }
//
//                @Override
//                public void onFailure(HttpException error, String msg) {
//                    Log.e("xx", msg);
//                }
//            }, null);
        }
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    protected <T> void doPost(String paramString, HashMap<String, String> paramHashMap) {
        HttpHelperPost.Post(LoginActivity.this, paramString, paramHashMap, new RequestCallBack() {
            @Override
            public void onSuccess(ResponseInfo responseInfo) {
                Log.e("xx", "responseInfo" + responseInfo);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Log.e("xx", s);
            }
        }, null);
    }


}

