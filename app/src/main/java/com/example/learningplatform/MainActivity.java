package com.example.learningplatform;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button; // 引入button组件
import android.widget.EditText;
import android.widget.Toast;

import com.example.learningplatform.listview.ListViewActivity;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private final String TAG = "MainActivity";

    private EditText login;
    private EditText password;

    // 登录按钮
    private Button loginBtn;
    // 注册按钮
    private Button registerBtn;
    // 登录框
    private EditText loginPic;
    // 密码框
    private EditText pwdPic;

    private String loginName, pwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        login = findViewById(R.id.login);
        password = findViewById(R.id.password);

        // 登录按钮
        loginBtn = findViewById(R.id.btn_login);

        // 注册按钮
        registerBtn = findViewById(R.id.btn_register);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转到个人页面
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        // 图片大小设置
        loginPic = findViewById(R.id.login);
        pwdPic = findViewById(R.id.password);
        setPicSize();

        // 点击登录按钮进行验证
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getEditString();
                if (TextUtils.isEmpty(loginName)) {
                    Toast.makeText(MainActivity.this, "请输入用户名", Toast.LENGTH_SHORT).show();
                    return;

                }else if (TextUtils.isEmpty(pwd)) {
                    Toast.makeText(MainActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                    return;

                } else {
                    postData();
                }

            }
        });

    }

    private void postData() {
        getEditString();
        OkHttpClient okHttpClient = new OkHttpClient();
        String url = "http://192.168.1.104:3000/lp/v1/user/login";
        RequestBody requestBody = new FormBody.Builder()
                .add("username", loginName)
                .add("password", pwd)
                .build();// add的name和后台读取参数的名字一致

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "onFailure: "+e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d(TAG, "onResponse: " + response.message().toString());
                if(response.code() == 200) {
                    // 跳转到登录页面
                    Intent intent = new Intent(MainActivity.this, BottomBarActivity.class);
                    Looper.prepare();
                    Toast.makeText(MainActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                    Looper.loop();
                } else if (response.code() == 500) {
                    Looper.prepare();
                    Toast.makeText(MainActivity.this, "用户名与密码不匹配，请重新输入", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }
            }
        });
    }

    private void getEditString() {
        loginName = login.getText().toString().trim();
        pwd = password.getText().toString().trim();
    }

    private void setPicSize() {
        Drawable drawableLogin = getResources().getDrawable(R.drawable.icon_login_user);
        drawableLogin.setBounds(0,0,50,50);//第一0是距左边距离，第二0是距上边距离，50、50分别是长宽
        loginPic.setCompoundDrawables(drawableLogin,null,null,null);

        Drawable drawableReg = getResources().getDrawable(R.drawable.icon_pwd);
        drawableReg.setBounds(0,0,50,50);//第一0是距左边距离，第二0是距上边距离，50、50分别是长宽
        pwdPic.setCompoundDrawables(drawableReg,null,null,null);
    }


}
